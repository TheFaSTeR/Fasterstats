package com.thefaster.bukkit.Fasterstats.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PlayerStateModel {
	
	public String name = null;
	
	public Integer playerId = null;
	
	public Date lastLogin = null;
	
	public Integer deaths = 0;		
	
	public double moveRange = 0;
	
	public Integer timePlayed = 0;
	
	public Map<Material, Integer> blockPlace = new HashMap<Material, Integer>();
	
	public Map<Material, Integer> blockBreak = new HashMap<Material, Integer>();
	
	public Map<String, Integer> creatureKill = new HashMap<String, Integer>();
	
	public void addPlaceBlock(Material mat) {
		if (this.blockPlace.containsKey(mat)) {
			Integer blocksCounter = this.blockPlace.get(mat);
			this.blockPlace.put(mat, blocksCounter + 1);
		} else {
			this.blockPlace.put(mat, 1);
		}
	}
	
	public void addBreakBlock(Material mat) {
		if (this.blockBreak.containsKey(mat)) {
			Integer counter = this.blockBreak.get(mat);
			this.blockBreak.put(mat, counter + 1);
		} else {
			this.blockBreak.put(mat, 1);
		}
	}
	
	public void setPlaceBlock(Material mat, Integer count) {					
		this.blockPlace.put(mat, count);		
	}
	
	public void setBreakBlock(Material mat, Integer count) {
		this.blockBreak.put(mat, count);
	}
	
	public void setPlayerKill(String creature, Integer count) {
		this.creatureKill.put(creature, count);
	}
	
	public void addMoveRange(double distance) {
		this.moveRange += distance;		
	}
	
	public void addPlayerDeath() {
		this.deaths += 1;
	}
	
	public void addPlayerKill(String entity) {
		if (this.creatureKill.containsKey(entity)) {
			Integer count = this.creatureKill.get(entity);
			this.creatureKill.put(entity, count + 1);
			System.out.println("[Fasterstats] Player " + this.name + " kill " + entity + " " + (count + 1) + " times.");
		} else {
			this.creatureKill.put(entity, 1);
			System.out.println("[Fasterstats] Player " + this.name + " kill " + entity + " 1 times.");
		}		
	}
	
	public PlayerStateModel(Player player) {
		this.name = player.getName();
	}
	
	public String toString() {
		return "Player: " + this.name + "\n" +
				"PlayerId: " + this.playerId + "\n" +
				"Last login: " + this.lastLogin + "\n" +
				"Deaths: " + this.deaths + "\n" +				
				"Move range: " + this.moveRange + "\n" +
				"Time played: " + this.timePlayed + "\n" +
				"Place blocks: " + this.blockPlace.toString() + "\n" +
				"Break blocks: " + this.blockBreak.toString();
	}

}
