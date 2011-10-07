package com.wimbli.TexturePackMenu;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.reader.UnicodeReader;

import org.bukkit.Material;

import org.getspout.spoutapi.player.SpoutPlayer;


public class Config
{
	// private stuff used within this class
	private static TexturePackMenu plugin;
	private static Yaml yaml;
	private static File configFile;
	private static File playerFile;

	private static Map<String, String> texturePacks = new LinkedHashMap<String, String>();  // (Pack Name, URL)
	private static Map<String, String> playerPacks = new HashMap<String, String>();  // (Player Name, Pack Name)


	// load config
	public static void load(TexturePackMenu master)
	{
		plugin = master;
		configFile = new File(plugin.getDataFolder(), "config.yml");
		playerFile = new File(plugin.getDataFolder(), "players.yml");

		// make our yml output more easily human readable, instead of compact and ugly
		DumperOptions options = new DumperOptions();
		options.setPrettyFlow(true);
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		options.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);
		yaml = new Yaml(options);

		try
		{
			FileInputStream in = new FileInputStream(configFile);
			texturePacks = (Map<String, String>)yaml.load(new UnicodeReader(in));
		}
		catch (FileNotFoundException e)
		{
		}

		if (texturePacks == null || texturePacks.isEmpty())
			useDefaults();

		loadPlayerPacks();
	}



	public static String[] texPackNames()
	{
		return texturePacks.keySet().toArray(new String[0]);
	}
	public static String[] texPackURLs()
	{
		return texturePacks.values().toArray(new String[0]);
	}
	public static int texturePackCount()
	{
		return texturePacks.size();
	}

	public static String getPack(String playerName)
	{
		if (!playerPacks.containsKey(playerName))
			return "";

		return playerPacks.get(playerName);
	}

	public static void setPack(SpoutPlayer sPlayer, String packName)
	{
		if (!texturePacks.containsKey(packName))
			setPack(sPlayer, 0);
		else
			setPlayerTexturePack(sPlayer, packName);
	}
	public static void setPack(SpoutPlayer sPlayer, int index)
	{
		if (texturePacks.size() < index - 1)
			index = 0;
		setPlayerTexturePack(sPlayer, texPackNames()[index]);
	}

	private static void setPlayerTexturePack(SpoutPlayer sPlayer, String packName)
	{
		String packURL = texturePacks.get(packName);

		if (packURL == null || packURL.isEmpty())
			sPlayer.resetTexturePack();
		else
			sPlayer.setTexturePack(packURL);

		sPlayer.sendNotification("Texture pack selected:", packName, Material.PAINTING);
		playerPacks.put(sPlayer.getName(), packName);
	}



	// load player data
	private static void loadPlayerPacks()
	{
		if (!playerFile.getParentFile().exists() || !playerFile.exists())
			return;

		try
		{
			FileInputStream in = new FileInputStream(playerFile);
			playerPacks = (Map<String, String>)yaml.load(new UnicodeReader(in));
		}
		catch (FileNotFoundException e)
		{
		}
	}

	// save player data
	public static void savePlayerPacks()
	{
		if (!playerFile.getParentFile().exists())
			return;

		try
		{
			BufferedWriter out = new BufferedWriter(new FileWriter(playerFile, false));
			out.write(yaml.dump(playerPacks));
			out.close();
		}
		catch (IOException e)
		{
			plugin.logWarn("ERROR SAVING PLAYER DATA: " + e.getLocalizedMessage());
		}
	}

	// set default values, and save new config file
	private static void useDefaults()
	{
		plugin.logConfig("Configuration not present, creating new default config.yml file. YOU WILL NEED TO EDIT IT MANUALLY.");

		texturePacks = new LinkedHashMap<String, String>();
		texturePacks.put("Player Choice", "");
		texturePacks.put("Minecraft Default", "https://github.com/Brettflan/TexturePackMenu/raw/master/packs/default.zip");
		texturePacks.put("Default Pack Copy", "https://github.com/Brettflan/TexturePackMenu/raw/master/packs/default.zip");

		if (!configFile.getParentFile().exists())
		{
			if (!configFile.getParentFile().mkdirs())
			{
				plugin.logWarn("FAILED TO CREATE PLUGIN FOLDER.");
				return;
			}
		}

		String newLine = System.getProperty("line.separator");

		try
		{
			BufferedWriter out = new BufferedWriter(new FileWriter(configFile, false));
			out.write("# Below is a default config which you will need to update to contain your own texture pack choices. You will need to restart your server after making changes." + newLine);
			out.write("# Each entry consists of the displayed name of the texture pack, followed by the download URL that will be used. There is no limit to the number of entries." + newLine);
			out.write("# The first entry will always be the default, which players new to the server will be set to." + newLine);
			out.write("# Be sure to test each one you add to make sure it works. An invalid URL will cause an error, and an URL which fails will make that entry do nothing." + newLine);
			out.write("# If you do want to allow players to use their own texture pack, you can leave a blank URL as seen below for \"Player Choice\"." + newLine);
			out.write("# Note that if you want to use quotation marks (\") in texture pack names, you will need to add them with a backslash like so to prevent parsing errors: \\\"" + newLine);
			out.write("#     example: \"The \\\"Silly\\\" Pack\": \"http://fake-server.net/sillypack.zip\"" + newLine);
			out.write("#     would display as:    The \"Silly\" Pack" + newLine);
			out.write("#" + newLine);
			out.write("# For more information, head here: http://dev.bukkit.org/server-mods/texturepackmenu/" + newLine);
			out.write(newLine);
			out.write(yaml.dump(texturePacks));
			out.close();
		}
		catch (IOException e)
		{
			plugin.logWarn("ERROR SAVING DEFAULT CONFIG: " + e.getLocalizedMessage());
		}
	}
}
