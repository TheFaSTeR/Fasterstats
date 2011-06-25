package com.thefaster.bukkit.Fasterstats.driver;

import org.bukkit.entity.Player;

import com.thefaster.bukkit.Fasterstats.model.PlayerStateModel;

public interface PluginDriver {
	
	void save(PlayerStateModel ps);
	void init() throws Exception;	
	
	PlayerStateModel getPlayerStateFromDatabase(Player player);
	void setPlayerOnline(Player player);
	void setPlayerOffline(Player player);	
	
}
