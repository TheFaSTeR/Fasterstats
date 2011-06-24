package com.thefaster.bukkit.Fasterstats.model;

import com.thefaster.bukkit.Fasterstats.driver.PluginDriver;
import com.thefaster.bukkit.Fasterstats.driver.mysql.MySQLDriver;

public class PluginModel {
	
	public PluginDriver db;
	
	public void setDriver(String driverName) throws Exception {
		if (driverName.equals("mysql")) {
			db = new MySQLDriver();
		} else {
			throw new Exception("Can't resolve database driver");
		}
	}

}
