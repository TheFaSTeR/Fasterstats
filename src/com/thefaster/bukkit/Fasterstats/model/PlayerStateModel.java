package com.thefaster.bukkit.Fasterstats.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Material;

public class PlayerStateModel {
	
	public String name = null;
	
	public Integer playerId = null;
	
	public Timestamp lastLogin = null;
	
	public Integer deaths = 0;		
	
	public double moveRange = 0;
	
	public long timePlayed = 0;
	
	public long damageDone = 0;
	
	public long damageGet = 0;
	
	public Map<Material, Integer> blockPlace = new HashMap<Material, Integer>();
	
	public Map<Material, Integer> blockBreak = new HashMap<Material, Integer>();
	
	public Map<String, Integer> creatureKill = new HashMap<String, Integer>();
	
	public Date startPlayed = null;
	public Date endPlayed = null;
	
	public PlayerStateModel(String name) {
		this.name = name;
	}
	
	public void addPlaceBlock(Material mat) {		
		this.addBlockToMap(mat, this.blockPlace);
	}
	
	public void addBreakBlock(Material mat) {		
		this.addBlockToMap(mat, this.blockBreak);
	}
	
	private void addBlockToMap(Material mat, Map<Material, Integer> map) {
		if (map.containsKey(mat)) {
			Integer counter = map.get(mat);
			map.put(mat, counter + 1);
		} else {
			map.put(mat, 1);
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
		} else {
			this.creatureKill.put(entity, 1);			
		}		
	}
	
	public void startTime() {
		this.startPlayed = new Date();
	}
	
	public void endTime() {
		this.endPlayed = new Date();
		long dif = (this.endPlayed.getTime() - this.startPlayed.getTime()) / 1000;
		this.timePlayed += dif;
	}
	
	public long getPlayedTimeInSeconds() {
		if (this.startPlayed == null) {
			return this.timePlayed;
		} else {
			Date now = new Date();
			long dif = (now.getTime() - this.startPlayed.getTime()) / 1000;
			return this.timePlayed + dif;
		}		
	}	
	
	public Integer getPlaceBlocksCount() {		
		return this.getMapEntityesCount(this.blockPlace);
	}
	
	public Integer getBreakBlocksCount() {		
		return this.getMapEntityesCount(this.blockBreak);
	}
	
	public Integer getKillsCount() {
		return this.getMapEntityesCount(this.creatureKill);
	}
	
	private Integer getMapEntityesCount(Map<?, Integer> map) {
		Collection<Integer> blocks = map.values();
		Iterator<Integer> iter = blocks.iterator();
		Integer counter = 0;
		
		while(iter.hasNext()) {
			counter += iter.next();
		}
		
		return counter;
	}		
	
	public double getMovedRange() {
		return Math.floor(this.moveRange);
	}
	
	public String getLastLogin() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(this.lastLogin);		
	}
	
	public Map<String, String> getTopPlacedBlock() {
		return this.findTopBlock(this.blockPlace);
	}
	
	public Map<String, String> getTopBreakedBlock() {
		return this.findTopBlock(this.blockBreak);
	}
	
	private Map<String, String> findTopBlock(Map<Material, Integer> map) {
		Material topMaterial = null;
		Integer topCount = 0;
		
		if (map != null) {
			Iterator<Material> iter = map.keySet().iterator();
			while (iter.hasNext()) {
				Material mat = iter.next();
				Integer count = map.get(mat);
				
				if (count > topCount) {
					topMaterial = mat;
					topCount = count;
				}
			}
		}		
		
		Map<String, String> result = new HashMap<String, String>();
		if (topMaterial != null) {
			result.put("material", topMaterial.name());					
			result.put("count", topCount.toString());
		} else {
			return null;
		}
		
		return result;
	}		
	
	public Map<String, String> getTopKill() {
		String topCreature = null;
		Integer topCount = 0;
		
		if (this.creatureKill != null) {
			Iterator<String> iter = this.creatureKill.keySet().iterator();
			while (iter.hasNext()) {
				String creature = iter.next();
				Integer count = this.creatureKill.get(creature);
				
				if (count > topCount) {
					topCreature = creature;
					topCount = count;
				}
			}
		}		
		
		Map<String, String> result = new HashMap<String, String>();
		if (topCreature != null) {
			result.put("creature", topCreature);					
			result.put("count", topCount.toString());
		} else {
			return null;
		}
		
		return result;
	}
	
	public void addDamageGet(Integer damage) {
		this.damageGet += damage;
	}
	
	public void addDamageDone(Integer damage) {
		this.damageDone += damage;
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
