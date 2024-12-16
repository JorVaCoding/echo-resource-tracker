package com.echoresourcetracker;

import lombok.Getter;
import net.runelite.api.ItemID;

public enum UntradableResource {

    //Mining
    STARDUST("Stardust", ItemID.STARDUST),
    BARRONITE("Barronite shards", ItemID.BARRONITE_SHARDS),
    PAYDIRT("Pay-dirt", ItemID.PAYDIRT),

    //Woodcutting
    BLISTERWOOD_LOGS("Blisterwood logs", ItemID.BLISTERWOOD_LOGS),

    //Fishing
    KARAMBWANJI("Karambwanji", ItemID.KARAMBWANJI),
    MINNOWS("Minnow", ItemID.MINNOW),
    SACRED_EEL("Sacred eel", ItemID.SACRED_EEL),
    INFERNAL_EEL("Infernal eel", ItemID.INFERNAL_EEL);

    @Getter
    final String itemName;
    @Getter
    final int itemID;

    UntradableResource(String itemName, int itemID) {
        this.itemName = itemName;
        this.itemID = itemID;
    }

    public static UntradableResource fromString(String filter) {
        for (UntradableResource res : values()) {
            if (res.getItemName().equalsIgnoreCase(filter))
                return res;
        }
        return null;
    }
}