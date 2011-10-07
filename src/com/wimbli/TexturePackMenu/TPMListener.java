package com.wimbli.TexturePackMenu;

import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;
import org.getspout.spoutapi.event.spout.SpoutListener;
import org.getspout.spoutapi.player.SpoutPlayer;


public class TPMListener extends SpoutListener
{
	@Override
	public void onSpoutCraftEnable(SpoutCraftEnableEvent event)
	{
		SpoutPlayer sPlayer = event.getPlayer();
		if (sPlayer == null)
			return;

		String texPack = Config.getPack(sPlayer.getName());
		Config.setPack(sPlayer, texPack);
	}
}
