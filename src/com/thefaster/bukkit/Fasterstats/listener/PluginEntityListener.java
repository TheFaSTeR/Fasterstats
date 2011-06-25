package com.thefaster.bukkit.Fasterstats.listener;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;

import com.thefaster.bukkit.Fasterstats.Fasterstats;
import com.thefaster.bukkit.Fasterstats.controller.PlayerController;
import com.thefaster.bukkit.Fasterstats.model.CreatureModel;

public class PluginEntityListener extends EntityListener {
	
	public static Fasterstats core;	
	   
    public PluginEntityListener(Fasterstats instance) {
            core = instance;
    }
    
    public void onEntityDeath(EntityDeathEvent event) {
    	if (event.getEntity() instanceof Player) {    		
    		PlayerController controller = core.getOnlinePlayer((Player)event.getEntity());
    		controller.playerDeath();
    	}    	
    	EntityDamageEvent damageEvent = event.getEntity().getLastDamageCause();
    	if(!damageEvent.isCancelled()) {
    		if(damageEvent instanceof EntityDamageByEntityEvent) {
    			EntityDamageByEntityEvent hostileEvent = (EntityDamageByEntityEvent) damageEvent;    			
    	    	
    	    	if(hostileEvent.getEntity() instanceof LivingEntity && hostileEvent.getDamager() instanceof Player) {
    	    		LivingEntity livingEntity = (LivingEntity)hostileEvent.getEntity();
    	    		String entityName = CreatureModel.getCreatureNameByEntity(livingEntity); 
    	    		System.out.println("[Fasterstats] was killed: " + entityName);
    	    		if (entityName != null) {
    	    			Player damager = (Player)hostileEvent.getDamager();
        				
        				PlayerController controller = core.getOnlinePlayer(damager);
        				controller.playerKill(entityName);
    	    		}    				
    			}
    		}    			    	
    	}
    }
    
}
