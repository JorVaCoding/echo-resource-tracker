package com.echoresourcetracker;

import lombok.Getter;
import lombok.Setter;
import net.runelite.client.ui.overlay.infobox.InfoBox;
import net.runelite.client.util.QuantityFormatter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.Instant;

public class ResourceInfoBox extends InfoBox {
    private final String itemName;
    @Setter
    private int amount;

    @Getter
    @Setter
    private Instant lastUpdated;

    @Getter
    @Setter
    private boolean hidden = true;

    public ResourceInfoBox(BufferedImage image, ERTPlugin plugin, String itemName) {
        super(image, plugin);
        this.itemName = itemName;
    }


    @Override
    public String getText() {
        return QuantityFormatter.quantityToRSDecimalStack(amount);
    }

    @Override
    public Color getTextColor() {
        return Color.WHITE;
    }

    @Override
    public String getTooltip() {
        return itemName;
    }
}
