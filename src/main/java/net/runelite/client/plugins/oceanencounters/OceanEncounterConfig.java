package net.runelite.client.plugins.oceanencounters;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("oceanencounters")
public interface OceanEncounterConfig extends Config
{
    @ConfigItem(
            keyName = "showClams",
            name = "Giant Clams",
            description = "Show hint arrow for giant clam encounters",
            position = 1
    )
    default boolean showClams() { return true; }

    @ConfigItem(
            keyName = "showCastaways",
            name = "Castaways & Ocean Man",
            description = "Show hint arrow for castaways and Ocean Man",
            position = 2
    )
    default boolean showCastaways() { return true; }

    @ConfigItem(
            keyName = "showTurtles",
            name = "Clue Turtles",
            description = "Show hint arrow for clue turtles",
            position = 3
    )
    default boolean showTurtles() { return true; }

    @ConfigItem(
            keyName = "showCratesAndCaskets",
            name = "Lost Crates & Caskets",
            description = "Show hint arrow for lost crates and lost caskets",
            position = 4
    )
    default boolean showCratesAndCaskets() { return true; }

    @ConfigItem(
            keyName = "showWindsAndGlows",
            name = "Strong Winds & Mysterious Glows",
            description = "Show hint arrow for strong winds, atmospheric glows, or lights",
            position = 5
    )
    default boolean showWindsAndGlows() { return true; }
}