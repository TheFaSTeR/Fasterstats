package com.thefaster.bukkit.Fasterstats.listener;


import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.thefaster.bukkit.Fasterstats.Fasterstats;
import com.thefaster.bukkit.Fasterstats.controller.PlayerController;

public class PluginPlayerListener extends PlayerListener {
	
	public static Fasterstats core;	
	   
    public PluginPlayerListener(Fasterstats instance) {
            core = instance;
    }
    
    public void onPlayerJoin(PlayerJoinEvent event) {
    	Player player = event.getPlayer();    	
    	System.out.println("[Fasterstats] player " + player.getName() + " has joined.");
    	
    	PlayerController controller = core.getOnlinePlayer(player);
    	controller.playerJoin();
    }
    
    public void onPlayerQuit(PlayerQuitEvent event) {
    	Player player = event.getPlayer();
    	
    	PlayerController controller = core.getOnlinePlayer(player);
    	controller.playerQuit();
    }
    
    public void onPlayerMove(PlayerMoveEvent event) {
    	Player player = event.getPlayer();
    	Location from = event.getFrom();
    	Location to = event.getTo();
    	
    	PlayerController controller = core.getOnlinePlayer(player);
    	controller.moveRange(from.distance(to));    	    	    	
    }
        
}
