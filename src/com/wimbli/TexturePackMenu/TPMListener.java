package com.wimbli.TexturePackMenu;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;
import org.getspout.spoutapi.player.SpoutPlayer;


public class TPMListener implements Listener
{
	@EventHandler(priority = EventPriority.NORMAL)
	public void onSpoutCraftEnable(SpoutCraftEnableEvent event)
	{
		SpoutPlayer sPlayer = event.getPlayer();
		if (sPlayer == null)
			return;

		String texPack = Config.getPack(sPlayer.getName());
		Config.setPackDelayed(sPlayer, texPack);
	}
}
