package com.thefaster.bukkit.Fasterstats.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.entity.Player;

public class PlayerView {
	
	Player player = null;
	
	private List<String> content = new ArrayList<String>();
	
	public PlayerView(Player player) {
		this.player = player;
	}
	
	public void addToContent(String value) {
		this.content.add(value);
	}
	
	public void clearContent() {
		this.content.clear();
	}
	
	public void display() {
		if (this.content.size() > 0) {
			Iterator<String> iter = this.content.iterator();
			
			while (iter.hasNext()) {
				this.player.sendMessage((String)iter.next());
			}
		}		
	}
	
}
