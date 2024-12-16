package com.echoresourcetracker;

import com.google.inject.Provides;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;

import javax.inject.Inject;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;

@Slf4j
@PluginDescriptor(
        name = "Echo Resource Tracker",
        description = "Track the resources being sent to your bank from your echo tools.",
        tags = {"raging", "echo", "leagues", "tracker"}

)
public class ERTPlugin extends Plugin {
    @Inject
    private Client client;

    @Inject
    @Getter
    private ERTConfig config;

    @Inject
    private ItemManager itemManager;

    @Inject
    private InfoBoxManager infoBoxManager;

    private HashMap<Integer, ResourceInfoBox> boxMap;

    @Override
    protected void startUp() throws Exception {
        boxMap = new HashMap<Integer, ResourceInfoBox>();
    }

    @Subscribe
    public void onChatMessage(ChatMessage chatMessage) {
        if (chatMessage.getType() == ChatMessageType.SPAM) {
            String msg = chatMessage.getMessage();
            if (msg.contains("you would have gained, giving you a total of")) {
                try {
                    String[] split = msg.split(" you would have gained, giving you a total of ");
                    String itemStr = split[0].substring(13);
                    String amountStr = split[1].replace(",", "").replace(".", "");
                    if (UntradableResource.fromString(itemStr) != null) {
                        updateResource(UntradableResource.fromString(itemStr).getItemID(), Integer.parseInt(amountStr));
                    } else {
                        updateResource(itemManager.search(itemStr).get(0).getId(), Integer.parseInt(amountStr));
                    }
                } catch (Exception e) {
                }
            }
        }
    }


    @Subscribe
    public void onGameTick(GameTick gameTick) {
        boxMap.values().stream().filter(box -> !box.isHidden() && Instant.now().isAfter(box.getLastUpdated().plus(Duration.ofSeconds(config.cooldown())))).forEach(box -> {
            box.setHidden(true);
            infoBoxManager.removeInfoBox(box);
        });
    }

    @Provides
    ERTConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(ERTConfig.class);
    }

    public void updateResource(int itemID, int amount) {
        ResourceInfoBox infoBox = boxMap.get(itemID);
        if (infoBox == null) {
            infoBox = new ResourceInfoBox(itemManager.getImage(itemID), this, itemManager.getItemComposition(itemID).getName());
            boxMap.put(itemID, infoBox);
        }

        infoBox.setAmount(amount);
        infoBox.setLastUpdated(Instant.now());
        if (infoBox.isHidden()) {
            infoBox.setHidden(false);
            infoBoxManager.addInfoBox(infoBox);
        }
    }
}