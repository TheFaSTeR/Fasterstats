package com.thefaster.bukkit.Fasterstats.driver;

import org.bukkit.entity.Player;

import com.thefaster.bukkit.Fasterstats.Fasterstats;
import com.thefaster.bukkit.Fasterstats.model.PlayerStateModel;

public interface PluginDriver {
	
	void save(PlayerStateModel ps);
	void init(Fasterstats instance) throws Exception;	
	
	PlayerStateModel getPlayerStateFromDatabase(String playerName);
	void setPlayerOnline(Player player);
	void setPlayerOffline(Player player);	
	
}
