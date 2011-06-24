package com.thefaster.bukkit.Fasterstats.controller;

import org.bukkit.entity.Player;
import org.bukkit.Material;

import com.thefaster.bukkit.Fasterstats.Fasterstats;
import com.thefaster.bukkit.Fasterstats.model.PlayerState;

public class PlayerLogger {
	
	private Player player = null;
	private Fasterstats core = null;
	
	private PlayerState playerState = null;
	
	public PlayerLogger(Player _player, Fasterstats _core) {
		this.player = _player;
		this.core = _core;				
	}
	
	public void placeBlock(Material mat) {
		this.playerState.addPlaceBlock(mat);				
	}
	
	public void breakBlock(Material mat) {
		this.playerState.addBreakBlock(mat);				
	}
	
	public void playerJoin() {
		System.out.println("[Fasterstats] " + player.getName() + " join. Getting player state.");
		this.playerState = this.core.playerModel.getPlayerStateFromDatabase(this.player);	
		this.core.playerModel.setPlayerOnline(player);
	}
	
	public void playerQuit() {
		System.out.println("[Fasterstats] " + player.getName() + " quit. Saving player state.");
		this.core.playerModel.setPlayerOffline(player);
	}		

}
