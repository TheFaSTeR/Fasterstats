package com.thefaster.bukkit.Fasterstats.listener;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.thefaster.bukkit.Fasterstats.Fasterstats;
import com.thefaster.bukkit.Fasterstats.controller.PlayerController;
import com.thefaster.bukkit.Fasterstats.helper.TimeHelper;
import com.thefaster.bukkit.Fasterstats.model.PlayerStateModel;
import com.thefaster.bukkit.Fasterstats.view.PlayerView;

public class PluginCommandListener {
	public static Fasterstats core;			
	
	private CommandSender sender = null;
	private Command cmd = null;
	//private String commandLabel = null;
	private String[] args = null;
	   
    public PluginCommandListener(Fasterstats instance, CommandSender sender, Command cmd, String commandLabel, String[] args) {
    	core = instance;
        this.sender = sender;
        this.cmd = cmd;
        //this.commandLabel = commandLabel;
        this.args = args;
    }        
    
    public boolean init() {    	
    	Player player = null;
    	PlayerController controller = null;
    	PlayerStateModel playerState = null;
    	PlayerView view = null;    	
    	
    	if (this.cmd.getName().equalsIgnoreCase("fasterstats")) {    		        	
        	if (this.sender instanceof Player) {
        		player = (Player)this.sender;        	        		
        		controller = core.getOnlinePlayer(player);    
        		playerState = controller.getPlayerState();
        		view = new PlayerView(player);
        	}        	        	
        	
        	if (this.args.length < 1 && player != null) {        		        		
        		this.printPlayerStatistic(view, playerState);
        	} else if (this.args.length >= 1 && player != null) {
        		if (this.args[0].equalsIgnoreCase("time")) {        			        			
        			view.addToContent(ChatColor.GRAY + "Your time played: " + TimeHelper.getSecondsAsPlayedTimeString(playerState.getPlayedTimeInSeconds()));
        			view.addToContent(ChatColor.GRAY + "Last login: " + playerState.getLastLogin());
        		}
        		
        		if (this.args[0].equalsIgnoreCase("player") && this.args.length >= 2) {
        			if (!this.args[1].isEmpty()) {
        				playerState = core.requestPlayerStateByName(this.args[1]);
        				if (playerState == null) {
        					view.addToContent(ChatColor.GRAY + "Player with name - " + this.args[1] + " not found.");
        				} else {
        					this.printPlayerStatistic(view, playerState);
        					if (this.args.length >= 3) {
        						if (this.args[2].equalsIgnoreCase("blocks")) {
        							this.printPlayerBlocks(view, playerState);
        						}
        						
        						if (this.args[2].equalsIgnoreCase("kills")) {
        							this.printPlayerKills(view, playerState);
        						}
        					}
        				}
        			}
        		}
        		
        		if (this.args[0].equalsIgnoreCase("blocks")) {
        			this.printPlayerBlocks(view, playerState);
        		}
        		
        		if (this.args[0].equalsIgnoreCase("kills")) {
        			this.printPlayerKills(view, playerState);
        		}
        	}
        	
        	view.display();
        	        	
    		return true;
    	} 
		return false;
    }
    
    private void printPlayerKills(PlayerView view, PlayerStateModel playerState) {
    	view.addToContent(ChatColor.GREEN + playerState.name + ChatColor.GRAY + " kills statistic:");
    	view.addToContent(ChatColor.GRAY + "Total kills: " + ChatColor.AQUA + playerState.getKillsCount());
    	Map<String, String> topKill = playerState.getTopKill();
    	if (topKill != null) {
    		view.addToContent(ChatColor.GRAY + "Top kill: " + ChatColor.AQUA + topKill.get("creature") + "(" + topKill.get("count") + ")");
    	}    	
    }
    
    private void printPlayerBlocks(PlayerView view, PlayerStateModel playerState) {
    	view.addToContent(ChatColor.GREEN + playerState.name + ChatColor.GRAY + " blocks statistic:");
    	view.addToContent(ChatColor.GRAY + "Placed blocks: " + ChatColor.AQUA + playerState.getPlaceBlocksCount());
		view.addToContent(ChatColor.GRAY + "Breaked blocks: " + ChatColor.AQUA + playerState.getBreakBlocksCount());
		Map<String, String> topPlaceBlock = playerState.getTopPlacedBlock();
		Map<String, String> topBreakBlock = playerState.getTopBreakedBlock();
		if (topPlaceBlock != null) {
			view.addToContent(ChatColor.GRAY + "Top placed block: " + ChatColor.AQUA + topPlaceBlock.get("material") + "(" + topPlaceBlock.get("count") + ")");
		}        			
		if (topBreakBlock != null) {
			view.addToContent(ChatColor.GRAY + "Top breaked block: " + ChatColor.AQUA + topBreakBlock.get("material") + "(" + topBreakBlock.get("count") + ")");
		}
    }
    
    private void printPlayerStatistic(PlayerView view, PlayerStateModel playerState) {
    	view.addToContent(ChatColor.GREEN + playerState.name + ChatColor.GRAY + " statistic:");
		view.addToContent(ChatColor.GRAY + "Time played: " + ChatColor.AQUA + TimeHelper.getSecondsAsPlayedTimeString(playerState.getPlayedTimeInSeconds()));        		
		view.addToContent(ChatColor.GRAY + "Moved range: " + ChatColor.AQUA + playerState.getMovedRange());
		view.addToContent(ChatColor.GRAY + "Deaths: " + ChatColor.AQUA + playerState.deaths);
		view.addToContent(ChatColor.GRAY + "Kills: " + ChatColor.AQUA + playerState.getKillsCount());
		view.addToContent(ChatColor.GRAY + "Damage done: " + ChatColor.AQUA + playerState.damageDone);
		view.addToContent(ChatColor.GRAY + "Damage get: " + ChatColor.AQUA + playerState.damageGet);
		view.addToContent(ChatColor.GRAY + "Placed blocks: " + ChatColor.AQUA + playerState.getPlaceBlocksCount());
		view.addToContent(ChatColor.GRAY + "Break blocks: " + ChatColor.AQUA + playerState.getBreakBlocksCount());      
    }
    
}
