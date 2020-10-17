package me.poma123.globalwarming.api;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * This enum holds the different scales of temperature.
 *
 * @author poma123
 *
 */
public enum TemperatureType {

    CELSIUS("攝氏", "°C"),
    FAHRENHEIT("華氏", "°F"),
    KELVIN("克耳文", "K");

    private final String name;
    private final String suffix;

    @ParametersAreNonnullByDefault
    TemperatureType(String name, String suffix) {
        this.name = name;
        this.suffix = suffix;
    }

    public String getName() {
        return this.name;
    }

    public String getSuffix() {
        return this.suffix;
    }
}
