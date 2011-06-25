package com.thefaster.bukkit.Fasterstats.controller;

import org.bukkit.entity.Player;
import org.bukkit.Material;

import com.thefaster.bukkit.Fasterstats.Fasterstats;
import com.thefaster.bukkit.Fasterstats.model.PlayerStateModel;

public class PlayerController {
	
	private Player player = null;
	private Fasterstats core = null;
	
	private PlayerStateModel playerState = null;
	
	public PlayerController(Player player, Fasterstats core) {
		this.player = player;
		this.core = core;				
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
		this.core.playerModel.Save(this.playerState);
		this.core.playerModel.setPlayerOffline(player);
	}		
	
	public void moveRange(double distance) {
		this.playerState.addMoveRange(distance);
	}
	
	public void playerDeath() {
		this.playerState.addPlayerDeath();
	}
	
	public void playerKill(String entity) {
		this.playerState.addPlayerKill(entity);
	}

}
