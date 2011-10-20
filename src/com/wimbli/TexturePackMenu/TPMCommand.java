package com.wimbli.TexturePackMenu;

import org.bukkit.command.*;
import org.bukkit.entity.Player;


public class TPMCommand implements CommandExecutor
{
	TexturePackMenu plugin;

	public TPMCommand(TexturePackMenu plugin)
	{
		this.plugin = plugin;
	}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] split)
	{
		// reload command?
		if (split.length == 1 && split[0].equals("reload"))
		{
			if (!sender.hasPermission("texturepackmenu.reload"))
			{
				sender.sendMessage("You do not have permission to reload the texture pack list.");
				return true;
			}

			plugin.log("Reloading texture pack list from config.yml at the command of \""+sender.getName()+"\".");
			Config.loadTexturePackList();
			return true;
		}

		// reset command?
		if (split.length == 2 && split[0].equals("reset"))
		{
			if (!sender.hasPermission("texturepackmenu.reset"))
			{
				sender.sendMessage("You do not have permission to reset player texture packs.");
				return true;
			}

			String playerName = split[1];
			String packName = Config.getPack(playerName);

			if (packName == null || packName.isEmpty())
			{
				sender.sendMessage("The specified player \""+playerName+"\" does not have a texture pack set.");
				return true;
			}

			Config.resetPlayerTexturePack(playerName);
			sender.sendMessage("The player \""+playerName+"\" is now reset back to the default texture pack. They did have \""+packName+"\" chosen.");
			if (sender instanceof Player)
				plugin.log("The player \""+sender.getName()+"\" has reset \""+playerName+"\" back to the default texture pack. They did have \""+packName+"\" chosen.");

			return true;
		}

		// just the regular command, then
		if (!(sender instanceof Player))
		{
			sender.sendMessage("The base command \"texture\" can only be run by in-game players.");
			return false;
		}

		// pop up the texture pack menu for them
		TPMPopup.create(plugin, (Player)sender);

		return true;
	}
}