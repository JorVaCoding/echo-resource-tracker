package com.echoresourcetracker;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("echoresourcetracker")
public interface ERTConfig extends Config
{
	@ConfigItem(
		keyName = "cooldown",
		name = "Hide after",
		description = "How long should the widget stay on screen (seconds)"
	)
	default int cooldown()
	{
		return 300;
	}
}
