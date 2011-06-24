package com.thefaster.bukkit.Fasterstats;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import com.thefaster.bukkit.Fasterstats.controller.PlayerLogger;
import com.thefaster.bukkit.Fasterstats.controller.PluginBlockListener;
import com.thefaster.bukkit.Fasterstats.controller.PluginPlayerListener;
import com.thefaster.bukkit.Fasterstats.model.PlayerModel;
import com.thefaster.bukkit.Fasterstats.model.PluginModel;

import config.Config;

public class Fasterstats extends JavaPlugin {
	
	public Logger log = Logger.getLogger("Minecraft");			
	
	public Map<Player, PlayerLogger> onlinePlayers = new HashMap<Player, PlayerLogger>();
	public PluginModel model = new PluginModel();
	public PlayerModel playerModel = null;
	public boolean isPluginEnabled = false;
	
	public Fasterstats() {				
	}
	
	public void onEnable(){
		log.info("[Fasterstats] version " + Config.PLUGIN_VERSION + " enabled");
										
		try {
			this.model.setDriver(Config.DATABASE_DRIVER);
			this.model.db.init();
			this.playerModel = new PlayerModel(this.model.db);
			this.SetListeners();
			this.isPluginEnabled = true;
		} catch (Exception e) {
			log.info("[Fasterstats] ");
			e.printStackTrace();
			this.isPluginEnabled = false;
		}						
		
	}
 
	public void onDisable(){
		log.info("[Fasterstats] version " + Config.PLUGIN_VERSION + " disabled");
		
		if (this.isPluginEnabled) {
			Collection<PlayerLogger> players = this.onlinePlayers.values();
			Iterator<PlayerLogger> iter = players.iterator();
			
			while(iter.hasNext()) {
				iter.next().playerQuit();
			}
		}						
	}
	
	public void SetListeners() {
		PluginManager pm = this.getServer().getPluginManager();
		
		PluginBlockListener blockListener = new PluginBlockListener(this);
		pm.registerEvent(Event.Type.BLOCK_PLACE, blockListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Event.Priority.Normal, this);
		
		PluginPlayerListener playerListener = new PluginPlayerListener(this);
		pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Event.Priority.Normal, this);		
	}
	
	public PlayerLogger getOnlinePlayer(Player player) {
		if (!this.onlinePlayers.containsKey(player)) {
			this.onlinePlayers.put(player, new PlayerLogger(player, this));
    		System.out.println("[Fasterstats] player " + player.getName() + " login.");
    	}
		
		return this.onlinePlayers.get(player);
	}
	
	public static void main(String[] args) {
		
	}

}
