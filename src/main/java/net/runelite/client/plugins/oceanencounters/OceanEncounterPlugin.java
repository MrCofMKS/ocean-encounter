package net.runelite.client.plugins.oceanencounters;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.NPC;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
        name = "Ocean Encounter Arrow",
        description = "Points a clue-scroll style arrow toward ocean encounters",
        tags = {"sailing", "ocean", "encounters", "arrow"}
)
public class OceanEncounterPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private OceanEncounterConfig config;

    private WorldPoint currentTargetPath = null;

    @Provides
    OceanEncounterConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(OceanEncounterConfig.class);
    }

    @Override
    protected void startUp() throws Exception
    {
        log.info("Ocean Encounter Arrow plugin started!");
    }

    @Override
    protected void shutDown() throws Exception
    {
        client.clearHintArrow();
        currentTargetPath = null;
        log.info("Ocean Encounter Arrow plugin stopped!");
    }

    private boolean isTargetEncounter(String name)
    {
        if (name == null)
        {
            return false;
        }

        String lowerName = name.toLowerCase();

        boolean isClam = lowerName.contains("giant clam") && config.showClams();
        boolean isCastaway = (lowerName.contains("castaway") || lowerName.contains("ocean man")) && config.showCastaways();
        boolean isTurtle = lowerName.contains("clue turtle") && config.showTurtles();
        boolean isLoot = (lowerName.contains("lost crate") || lowerName.contains("lost casket")) && config.showCratesAndCaskets();
        boolean isWeather = (lowerName.contains("strong wind") || lowerName.contains("mysterious glow") || lowerName.contains("beacon") || lowerName.contains("light")) && config.showWindsAndGlows();

        return isClam || isCastaway || isTurtle || isLoot || isWeather;
    }

    @Subscribe
    public void onNpcSpawned(NpcSpawned event)
    {
        NPC npc = event.getNpc();
        if (isTargetEncounter(npc.getName()))
        {
            currentTargetPath = npc.getWorldLocation();
            client.setHintArrow(currentTargetPath);
        }
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned event)
    {
        NPC npc = event.getNpc();
        if (currentTargetPath != null && npc.getWorldLocation().equals(currentTargetPath))
        {
            client.clearHintArrow();
            currentTargetPath = null;
        }
    }

    @Subscribe
    public void onGameObjectSpawned(GameObjectSpawned event)
    {
        GameObject object = event.getGameObject();
        if (object != null && client.getObjectDefinition(object.getId()) != null)
        {
            String name = client.getObjectDefinition(object.getId()).getName();
            if (isTargetEncounter(name))
            {
                currentTargetPath = object.getWorldLocation();
                client.setHintArrow(currentTargetPath);
            }
        }
    }

    @Subscribe
    public void onGameObjectDespawned(GameObjectDespawned event)
    {
        GameObject object = event.getGameObject();
        if (currentTargetPath != null && object.getWorldLocation().equals(currentTargetPath))
        {
            client.clearHintArrow();
            currentTargetPath = null;
        }
    }
}