package me.poma123.globalwarming.listeners;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.events.AsyncGeneratorProcessCompleteEvent;
import io.github.thebusybiscuit.slimefun4.api.events.AsyncReactorProcessCompleteEvent;
import io.github.thebusybiscuit.slimefun4.api.events.AsyncMachineProcessCompleteEvent;
import me.mrCookieSlime.Slimefun.cscorelib2.chat.ChatColors;
import me.poma123.globalwarming.GlobalWarmingPlugin;
import me.poma123.globalwarming.TemperatureManager;
import me.poma123.globalwarming.api.events.AsyncWorldPollutionChangeEvent;
import me.poma123.globalwarming.api.TemperatureType;
import me.poma123.globalwarming.api.PollutionManager;

public class PollutionListener implements Listener {

    private static final int BROADCAST_COOLDOWN = 60000;

    private final Map<String, Long> lastWorldBroadcasts = new HashMap<>();
    private final Map<String, Double> tempPollutionValues = new HashMap<>();

    @EventHandler
    public void onMachineProcessComplete(AsyncMachineProcessCompleteEvent e) {
        World world = e.getLocation().getWorld();

        if (!GlobalWarmingPlugin.getRegistry().isWorldEnabled(world.getName())) {
            return;
        }

        risePollutionTry(world, e.getMachine().getId(), e.getMachineRecipe().getInput());
        descendPollutionTry(world, e.getMachine().getId());
    }

    @EventHandler
    public void onGeneratorProcessComplete(AsyncGeneratorProcessCompleteEvent e) {
        World world = e.getLocation().getWorld();

        if (!GlobalWarmingPlugin.getRegistry().isWorldEnabled(world.getName())) {
            return;
        }

        risePollutionTry(world, e.getGenerator().getId(), new ItemStack[]{ e.getMachineFuel().getInput() });
        descendPollutionTry(world, e.getGenerator().getId());
    }

    @EventHandler
    public void onReactorProcessComplete(AsyncReactorProcessCompleteEvent e) {
        World world = e.getLocation().getWorld();

        if (!GlobalWarmingPlugin.getRegistry().isWorldEnabled(world.getName())) {
            return;
        }

        risePollutionTry(world, e.getReactor().getId(), new ItemStack[]{ e.getMachineFuel().getInput() });
        descendPollutionTry(world, e.getReactor().getId());
    }

    @EventHandler
    public void onAnimalBreed(EntityBreedEvent e) {
        World world = e.getMother().getWorld();

        if (!GlobalWarmingPlugin.getRegistry().isWorldEnabled(world.getName())) {
            return;
        }

        double pollutionValue = GlobalWarmingPlugin.getRegistry().getAnimalBreedPollution();

        if (pollutionValue > 0.0) {
            PollutionManager.descendPollutionInWorld(world, pollutionValue);
        }
    }

    @EventHandler
    public void onTreeGrowth(StructureGrowEvent e) {
        World world = e.getWorld();

        if (!GlobalWarmingPlugin.getRegistry().isWorldEnabled(world.getName())) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(GlobalWarmingPlugin.getInstance(), () -> {
            Material type = e.getLocation().getBlock().getType();

            if (Tag.LOGS.isTagged(type)) {
                double pollutionValue = GlobalWarmingPlugin.getRegistry().getTreeGrowthAbsorption();

                if (pollutionValue > 0.0) {
                    PollutionManager.descendPollutionInWorld(world, pollutionValue);
                }
            }
        }, 2);
    }

    @EventHandler
    public void onPollutionChange(AsyncWorldPollutionChangeEvent e) {
        // This delayed task is needed to prevent multiple broadcasts
        Bukkit.getScheduler().runTaskLater(GlobalWarmingPlugin.getInstance(), () -> {
            World world = e.getWorld();

            Long lastBroadcast = lastWorldBroadcasts.get(world.getName());
            if (lastBroadcast != null && (System.currentTimeMillis() - lastBroadcast) < BROADCAST_COOLDOWN) {
                return;
            }
            lastWorldBroadcasts.put(world.getName(), System.currentTimeMillis());

            double amount = TemperatureManager.fixDouble(e.getNewValue() * GlobalWarmingPlugin.getRegistry().getPollutionMultiply());
            if (!tempPollutionValues.containsKey(world.getName())) {
                tempPollutionValues.put(world.getName(), amount);
            } else {
                if (tempPollutionValues.get(world.getName()) == amount) {
                    return;
                }
            }

            tempPollutionValues.replace(world.getName(), amount);

            sendNews(world);

        }, ThreadLocalRandom.current().nextInt(1, 20));
    }

    private void sendNews(World world) {
        TemperatureType messageTempType = TemperatureType.valueOf(GlobalWarmingPlugin.getMessagesConfig().getString("temperature-scale"));
        String difference = GlobalWarmingPlugin.getTemperatureManager().getAirQualityString(world, messageTempType);

        String news = "";
        if (!GlobalWarmingPlugin.getRegistry().getNews().isEmpty()) {
            String base = GlobalWarmingPlugin.getMessagesConfig().getString("messages.breaking-news");
            List<String> newsList = GlobalWarmingPlugin.getRegistry().getNews();
            String random = newsList.get(ThreadLocalRandom.current().nextInt(newsList.size()));

            news = ChatColors.color(base.replace("%news%", random));
        }

        for (Player p : world.getPlayers()) {
            p.sendMessage(ChatColors.color(GlobalWarmingPlugin.getMessagesConfig().getString("messages.climate-change").replace("%value%", difference)));

            if (news.length() > 0) {
                p.sendMessage(news);
            }
        }
    }

    private boolean risePollutionTry(World world, String id, ItemStack[] recipeInput) {
        double pollutionValue = calculatePollutionValue(id, recipeInput);

        if (pollutionValue > 0.0) {
            PollutionManager.risePollutionInWorld(world, pollutionValue);
            return true;
        }

        return false;
    }

    private boolean descendPollutionTry(World world, String id) {
        double absorptionValue = calculateAbsorptionValue(id);

        if (absorptionValue > 0.0) {
            PollutionManager.descendPollutionInWorld(world, absorptionValue);
            return true;
        }

        return false;
    }

    private double calculatePollutionValue(String id, ItemStack[] recipeInput) {
        double pollutionValue = 0.0;
        
        pollutionValue += PollutionManager.isPollutedMachine(id);

        for (ItemStack item : recipeInput) {
            pollutionValue += PollutionManager.isPollutedItem(item);
        }
        
        return pollutionValue;
    }

    private double calculateAbsorptionValue(String id) {
        double absorptionValue = 0.0;

        absorptionValue += PollutionManager.isAbsorbentMachine(id);

        return absorptionValue;
    }
}
