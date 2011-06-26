package com.thefaster.bukkit.Fasterstats.controller;

import org.bukkit.entity.Player;

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
	
	public void playerJoin() {		
		this.playerState = this.core.playerModel.getPlayerStateFromDatabase(this.player.getName());	
		this.core.playerModel.setPlayerOnline(player);
		this.playerState.startTime();
	}
	
	public void playerQuit() {		
		this.playerState.endTime();
		this.core.playerModel.Save(this.playerState);
		this.core.playerModel.setPlayerOffline(player);		
	}		
	
	public PlayerStateModel getPlayerState() {
		return this.playerState;
	}		
}
