package com.thefaster.bukkit.Fasterstats.listener;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.thefaster.bukkit.Fasterstats.Fasterstats;

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
    	
    	if (this.cmd.getName().equalsIgnoreCase("fasterstats")) {    		        	
        	if (this.sender instanceof Player) {
        		player = (Player)this.sender;        		
        	}
        	
        	if (this.args.length < 1 && player != null) {        		
        		player.sendMessage(ChatColor.GRAY + "Printing " + player.getName() + " statistic.");
        	}
        	        	
    		return true;
    	} 
		return false;
    }
    
}
