package com.wimbli.TexturePackMenu;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


public class TPMCommand implements CommandExecutor
{
	Plugin plugin;

	public TPMCommand(Plugin plugin)
	{
		this.plugin = plugin;
	}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] split)
	{
		if (!(sender instanceof Player))
		{
			sender.sendMessage("This command can only be run by players.");
			return false;
		}

		// pop up the texture pack menu for them
		TPMPopup.create(plugin, (Player)sender);

		return true;
	}
}