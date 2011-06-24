package com.thefaster.bukkit.Fasterstats.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;

public class PlayerState {
	
	public String name = null;
	
	public Date lastLogin = null;
	
	public Integer deaths = 0;
	
	public Integer kills = 0;
	
	public Integer moveRange = 0;
	
	public Integer timePlayed = 0;
	
	public Map<Material, Integer> blockPlace = new HashMap<Material, Integer>();
	
	public Map<Material, Integer> blockBreak = new HashMap<Material, Integer>();
	
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

}
