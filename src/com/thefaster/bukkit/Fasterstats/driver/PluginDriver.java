package com.thefaster.bukkit.Fasterstats.driver;

import org.bukkit.entity.Player;

import com.thefaster.bukkit.Fasterstats.model.PlayerState;

public interface PluginDriver {
	
	void save();
	void connection();
	void init() throws Exception;	
	
	PlayerState getPlayerStateFromDatabase(Player player);
	void setPlayerOnline(Player player);
	void setPlayerOffline(Player player);
	
}
