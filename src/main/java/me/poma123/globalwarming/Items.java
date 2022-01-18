package me.poma123.globalwarming;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineTier;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineType;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;

public final class Items {
    public static final SlimefunItemStack THERMOMETER = new SlimefunItemStack("THERMOMETER", "24fa511f2628d56a8c8691ac5df3e3f82716384514a5ea5bae3eda86f48ad6e1", "&e溫度計", "", "&7顯示當前溫度", "", "&e右鍵 &7切換溫度類型");
    public static final SlimefunItemStack AIR_QUALITY_METER = new SlimefunItemStack("AIR_QUALITY_METER", "179adc3d2dfda05497bb904bd6651922510ce2139a71c10eae3b27565292ebf0", "&b空氣質量儀", "", "&7顯示由於氣候變化", "&7所導致的當前溫度上升", "", "&e右鍵 &7切換溫度類型");
    public static final SlimefunItemStack AIR_COMPRESSOR = new SlimefunItemStack("AIR_COMPRESSOR", Material.DISPENSER, "&b空氣壓縮機", "", "&a壓縮二氧化碳", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE), LoreBuilder.powerBuffer(512), LoreBuilder.powerPerSecond(16));
    public static final SlimefunItemStack EMPTY_CANISTER = new SlimefunItemStack("EMPTY_CANISTER", Material.GLASS_BOTTLE, "&7空罐");
    public static final SlimefunItemStack CO2_CANISTER;
    public static final SlimefunItemStack CINNABARITE = new SlimefunItemStack("CINNABARITE", "d67a8a3d7d5aa5db00dff5c82f846ea0aeb7d645f0e467d7e9d9a18e9fa5b012", "&c硃砂石");
    public static final SlimefunItemStack MERCURY = new SlimefunItemStack("MERCURY", Material.GRAY_DYE, "&7汞");
    public static final SlimefunItemStack FILTER = new SlimefunItemStack("AIR_COMPRESSOR_FILTER", Material.GUNPOWDER, "&7濾網");

    static {
        ItemStack item = new ItemStack(Material.POTION);
        ItemMeta meta = item.getItemMeta();
        ((PotionMeta) meta).setColor(Color.fromRGB(61, 61, 61));
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        item.setItemMeta(meta);

        CO2_CANISTER = new SlimefunItemStack("CO2_CANISTER", item, "&7二氧化碳罐", "", "&8&o壓縮二氧化碳");
    }
}