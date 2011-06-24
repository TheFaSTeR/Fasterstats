package com.thefaster.bukkit.Fasterstats.controller;


import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.thefaster.bukkit.Fasterstats.Fasterstats;

public class PluginPlayerListener extends PlayerListener {
	
	public static Fasterstats core;	
	   
    public PluginPlayerListener(Fasterstats instance) {
            core = instance;
    }
    
    public void onPlayerJoin(PlayerJoinEvent event) {
    	Player player = event.getPlayer();    	
    	System.out.println("[Fasterstats] player " + player.getName() + " has joined.");
    	
    	PlayerLogger logger = core.getOnlinePlayer(player);
    	logger.playerJoin();
    }
    
    public void onPlayerQuit(PlayerQuitEvent event) {
    	Player player = event.getPlayer();
    	
    	PlayerLogger logger = core.getOnlinePlayer(player);
    	logger.playerQuit();
    }
}
