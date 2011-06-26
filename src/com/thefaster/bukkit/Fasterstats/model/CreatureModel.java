package com.thefaster.bukkit.Fasterstats.model;

import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Giant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;

public class CreatureModel {
	
	public static String CREATURE_ZOMBIE 		= "Zombie";
	public static String CREATURE_CREEPER 		= "Creeper";
	public static String CREATURE_CHICKEN 		= "Chicken";
	public static String CREATURE_COW 			= "Cow";
	public static String CREATURE_GHAST 		= "Ghast";
	public static String CREATURE_GIANT 		= "Giant";
	public static String CREATURE_MONSTER 		= "Monster";
	public static String CREATURE_PIG 			= "Pig";
	public static String CREATURE_PIGZOMBIE		= "PigZombie";
	public static String CREATURE_SHEEP			= "Sheep";
	public static String CREATURE_SKELETON 		= "Skeleton";
	public static String CREATURE_SLIME			= "Slime";
	public static String CREATURE_SPIDER		= "Spider";
	public static String CREATURE_WOLF			= "Wolf";
	public static String CREATURE_PLAYER		= "Player";
	
	public static String getCreatureNameByEntity(LivingEntity entity) {
		if (entity instanceof Zombie) {
			return CREATURE_ZOMBIE;
		} else if (entity instanceof Creeper) {
			return CREATURE_CREEPER;
		} else if (entity instanceof Chicken) {
			return CREATURE_CHICKEN;
		} else if (entity instanceof Cow) {
			return CREATURE_COW;
		} else if (entity instanceof Ghast) {
			return CREATURE_GHAST;
		} else if (entity instanceof Monster) {
			return CREATURE_MONSTER;
		} else if (entity instanceof Giant) {
			return CREATURE_GIANT;
		} else if (entity instanceof Pig) {
			return CREATURE_PIG;
		} else if (entity instanceof PigZombie) {
			return CREATURE_PIGZOMBIE;
		} else if (entity instanceof Sheep) {
			return CREATURE_SHEEP;
		} else if (entity instanceof Skeleton) {
			return CREATURE_SKELETON;
		} else if (entity instanceof Slime) {
			return CREATURE_SLIME;
		} else if (entity instanceof Spider) {
			return CREATURE_SPIDER;
		} else if (entity instanceof Wolf) {
			return CREATURE_WOLF;
		} else if (entity instanceof Player) {
			return CREATURE_PLAYER;
		} else {
			return null;
		}
	}
	
}
