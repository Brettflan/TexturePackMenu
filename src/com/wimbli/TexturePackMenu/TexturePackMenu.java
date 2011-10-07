package com.wimbli.TexturePackMenu;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;


public class TexturePackMenu extends JavaPlugin
{
	private static final Logger mcLog = Logger.getLogger("Minecraft");
	private final TPMListener eventListener = new TPMListener();
	private String logName;

	public void onEnable()
	{
		logName = this.getDescription().getName();
		log("version " + this.getDescription().getVersion() + " loading...");
		Config.load(this);
		getCommand("texture").setExecutor(new TPMCommand(this));
		this.getServer().getPluginManager().registerEvent(Event.Type.CUSTOM_EVENT, eventListener, Event.Priority.Normal, this);
	}

	public void onDisable()
	{
		log("version " + this.getDescription().getVersion() + " shutting down...");
		Config.savePlayerPacks();
	}


	public void log(Level lvl, String text)
	{
		mcLog.log(lvl, String.format("[%s] %s", logName, text));
	}
	public void log(String text)
	{
		log(Level.INFO, text);
	}
	public void logWarn(String text)
	{
		log(Level.WARNING, text);
	}
	public void logConfig(String text)
	{
		log(Level.INFO, "[CONFIG] " + text);
	}
}
