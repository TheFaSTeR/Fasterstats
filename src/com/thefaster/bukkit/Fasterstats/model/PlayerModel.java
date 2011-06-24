package com.thefaster.bukkit.Fasterstats.model;

import org.bukkit.entity.Player;

import com.thefaster.bukkit.Fasterstats.driver.PluginDriver;

public class PlayerModel {
	
	private PluginDriver driver = null;
	
	public PlayerModel(PluginDriver driverInstance) {
		this.driver = driverInstance;
	}
	
	public PlayerState getPlayerStateFromDatabase(Player player) {
		return driver.getPlayerStateFromDatabase(player);
	}
	
	public void setPlayerOnline(Player player) {
		driver.setPlayerOnline(player);
	}
	
	public void setPlayerOffline(Player player) {
		driver.setPlayerOffline(player);
	}

}
