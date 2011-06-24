package com.thefaster.bukkit.Fasterstats.controller;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.thefaster.bukkit.Fasterstats.Fasterstats;

public class PluginBlockListener extends BlockListener {
	
    public static Fasterstats plugin;
   
    public PluginBlockListener(Fasterstats instance) {
            plugin = instance;
    }
    //You HAVE to have this!

    public void onBlockPlace(BlockPlaceEvent event){
           
            Player player = event.getPlayer();
            Block block = event.getBlock();
            Material mat = block.getType();            
            
            PlayerLogger logger = plugin.getOnlinePlayer(player);
            logger.placeBlock(mat);

    }
    
    public void onBlockBreak(BlockBreakEvent event) {
    	Player player = event.getPlayer();
    	Block block = event.getBlock();
    	Material mat = block.getType();    	    	
    	
    	PlayerLogger logger = plugin.getOnlinePlayer(player);
    	logger.breakBlock(mat);
    }
}
