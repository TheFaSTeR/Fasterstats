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
    		controller.getPlayerState().addPlayerDeath();
    	}    	
    	EntityDamageEvent damageEvent = event.getEntity().getLastDamageCause();
    	if (damageEvent != null) {
    		if(!damageEvent.isCancelled()) {
        		if(damageEvent instanceof EntityDamageByEntityEvent) {
        			EntityDamageByEntityEvent hostileEvent = (EntityDamageByEntityEvent) damageEvent;    			
        	    	
        	    	if(hostileEvent.getEntity() instanceof LivingEntity && hostileEvent.getDamager() instanceof Player) {
        	    		LivingEntity livingEntity = (LivingEntity)hostileEvent.getEntity();
        	    		String entityName = CreatureModel.getCreatureNameByEntity(livingEntity);     	    		
        	    		if (entityName != null) {
        	    			Player damager = (Player)hostileEvent.getDamager();
            				
            				PlayerController controller = core.getOnlinePlayer(damager);
            				controller.getPlayerState().addPlayerKill(entityName);
        	    		}    				
        			}
        		}    			    	
        	}
    	}    	
    }
    
    public void onEntityDamage(EntityDamageEvent event) {
    	if (!event.isCancelled()) {    		
    		Integer damage = event.getDamage();
    		if (event.getEntity() instanceof Player) {    			
    			PlayerController controller = core.getOnlinePlayer((Player)event.getEntity());
    			controller.getPlayerState().addDamageGet(damage);    			    			
    		}
			if (event instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent hostileEvent = (EntityDamageByEntityEvent) event;
				
				if (hostileEvent.getEntity() instanceof LivingEntity && hostileEvent.getDamager() instanceof Player) {
					PlayerController damagerController = core.getOnlinePlayer((Player)hostileEvent.getDamager());
					damagerController.getPlayerState().addDamageDone(damage);
				}
			}    		
    	}
    }
    
}
