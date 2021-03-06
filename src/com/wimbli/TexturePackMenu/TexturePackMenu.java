package com.wimbli.TexturePackMenu;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;


public class TexturePackMenu extends JavaPlugin
{
	private static final Logger mcLog = Logger.getLogger("Minecraft");
	private String logName;

	@Override
	public void onEnable()
	{
		logName = this.getDescription().getName();
		Config.load(this);
		getCommand("texture").setExecutor(new TPMCommand(this));
		getServer().getPluginManager().registerEvents(new TPMListener(), this);
	}

	@Override
	public void onDisable()
	{
		Config.savePlayerPacks();
	}


	protected final void log(Level lvl, String text)
	{
		mcLog.log(lvl, String.format("[%s] %s", logName, text));
	}
	protected final void log(String text)
	{
		log(Level.INFO, text);
	}
	protected final void logWarn(String text)
	{
		log(Level.WARNING, text);
	}
	protected final void logConfig(String text)
	{
		log(Level.INFO, "[CONFIG] " + text);
	}
}
