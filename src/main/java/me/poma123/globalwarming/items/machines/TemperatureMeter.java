package me.poma123.globalwarming.items.machines;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun4.utils.holograms.SimpleHologram;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.poma123.globalwarming.api.TemperatureType;

public abstract class TemperatureMeter extends SlimefunItem {

    @ParametersAreNonnullByDefault
    protected TemperatureMeter(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        SlimefunItem.registerBlockHandler(getId(), (p, b, stack, reason) -> {
            SimpleHologram.remove(b);
            return true;
        });
    }

    @Nonnull
    private BlockPlaceHandler onPlace() {
        return new BlockPlaceHandler(false) {

            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                Block b = e.getBlockPlaced();
                BlockStorage.addBlockInfo(b,"type", TemperatureType.CELSIUS.name());
                SimpleHologram.update(b, "&7測量...");
            }
        };
    }

    @Nonnull
    private BlockUseHandler onRightClick() {
        return e -> {
            Player p = e.getPlayer();
            Block b = e.getClickedBlock().get();

            TemperatureType saved = TemperatureType.valueOf(BlockStorage.getLocationInfo(b.getLocation(), "type"));

            if (saved == TemperatureType.CELSIUS) {
                saved = TemperatureType.FAHRENHEIT;
            } else if (saved == TemperatureType.FAHRENHEIT) {
                saved = TemperatureType.KELVIN;
            } else {
                saved = TemperatureType.CELSIUS;
            }

            BlockStorage.addBlockInfo(b, "type", saved.name());
            p.sendMessage("§7溫度類型: §e" + saved.getName());

            e.cancel();
        };
    }

    @Override
    public void preRegister() {
        addItemHandler(onPlace());
        addItemHandler(onRightClick());
        addItemHandler(new BlockTicker() {

            @Override
            public boolean isSynchronized() {
                return false;
            }

            @Override
            public void tick(Block b, SlimefunItem item, Config data) {
                TemperatureMeter.this.tick(b);
            }
        });
    }

    public void tick(@Nonnull Block b) {
    }
}
