package com.thefaster.bukkit.Fasterstats;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import com.thefaster.bukkit.Fasterstats.controller.ConfigController;
import com.thefaster.bukkit.Fasterstats.controller.PlayerController;
import com.thefaster.bukkit.Fasterstats.listener.PluginBlockListener;
import com.thefaster.bukkit.Fasterstats.listener.PluginCommandListener;
import com.thefaster.bukkit.Fasterstats.listener.PluginEntityListener;
import com.thefaster.bukkit.Fasterstats.listener.PluginPlayerListener;
import com.thefaster.bukkit.Fasterstats.model.PlayerModel;
import com.thefaster.bukkit.Fasterstats.model.PlayerStateModel;
import com.thefaster.bukkit.Fasterstats.model.PluginModel;

public class Fasterstats extends JavaPlugin {
	
	public Logger log = Logger.getLogger("Minecraft");				
	
	public Map<Player, PlayerController> onlinePlayers = new HashMap<Player, PlayerController>();
	public PluginModel model = new PluginModel();
	public PlayerModel playerModel = null;
	public boolean isPluginEnabled = false;	
	public ConfigController config = null;
	
	public Fasterstats() {				
	}
	
	@Override
    public void onLoad() {
        this.config = new ConfigController("config.yml", this);        
    }	
	
	public void onEnable(){
		log.info("[Fasterstats] version " + this.config.getPluginVersion() + " enabled");
		log.info("[Fasterstats] backend driver: " + this.config.getDriverType());
										
		try {
			this.model.setDriver(this.config.getDriverType());
			this.model.db.init(this);
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
		log.info("[Fasterstats] version " + this.config.getPluginVersion() + " disabled");
		
		if (this.isPluginEnabled) {
			Collection<PlayerController> players = this.onlinePlayers.values();
			Iterator<PlayerController> iter = players.iterator();
			
			while(iter.hasNext()) {
				iter.next().playerQuit();
			}
		}						
	}
	
	public void SetListeners() {
		PluginManager pm = this.getServer().getPluginManager();				
		
		PluginBlockListener blockListener = new PluginBlockListener(this);
		pm.registerEvent(Event.Type.BLOCK_PLACE, blockListener, Event.Priority.Monitor, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Event.Priority.Monitor, this);				
		
		PluginPlayerListener playerListener = new PluginPlayerListener(this);
		pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Event.Priority.Monitor, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Event.Priority.Monitor, this);
		pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Event.Priority.Monitor, this);
				
		PluginEntityListener entityListener = new PluginEntityListener(this);
		pm.registerEvent(Event.Type.ENTITY_DEATH, entityListener, Event.Priority.Monitor, this); 		
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, entityListener, Event.Priority.Monitor, this);
	}
	
	public PlayerController getOnlinePlayer(Player player) {
		if (!this.onlinePlayers.containsKey(player)) {
			this.onlinePlayers.put(player, new PlayerController(player, this));    		
    	}
		
		return this.onlinePlayers.get(player);
	}
	
	public PlayerStateModel requestPlayerStateByName(String playerName) {
		Iterator<Player> iterPlayer = this.onlinePlayers.keySet().iterator();
		while (iterPlayer.hasNext()) {
			Player player = iterPlayer.next();
			if (player.getName().equals(playerName)) {
				PlayerController controller = this.getOnlinePlayer(player);
				return controller.getPlayerState();
			}
		}
		
		return this.model.db.getPlayerStateFromDatabase(playerName);
	}
	
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {		
		return new PluginCommandListener(this, sender, command, commandLabel, args).init();		
	}
	
	public static void main(String[] args) {
		
	}

}
