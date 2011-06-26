package com.thefaster.bukkit.Fasterstats.controller;

import java.io.File;
import java.io.IOException;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.util.config.Configuration;

import com.thefaster.bukkit.Fasterstats.Fasterstats;

public class ConfigController {
	
	private String name = null;
	private Fasterstats core = null;
	private Configuration config = null;
	private PluginDescriptionFile pdf = null;
	
	public ConfigController(String name, Fasterstats instance) {
		this.name = name;
		this.core = instance;
		this.init();
	}
	
	protected void init() {
		this.pdf = this.core.getDescription();
		
		File dataFolder = this.core.getDataFolder();
		
		if (dataFolder == null) {
			dataFolder = new File("plugins/fasterstats");			
		}
		
		File configFile = new File(dataFolder, this.name);
		if (!configFile.exists()) {
			try {
				if (!dataFolder.exists()) {
					dataFolder.mkdirs();
				}
				
				configFile.createNewFile();
				this.config = new Configuration(configFile);
				this.config.setProperty("fasterstats.driver.type", "mysql");
				this.config.setProperty("fasterstats.driver.host", "localhost");
				this.config.setProperty("fasterstats.driver.user", "fasterstats");
				this.config.setProperty("fasterstats.driver.dbname", "fasterstats");
				this.config.setProperty("fasterstats.driver.password", "1234");
				this.config.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			this.config = new Configuration(configFile);
			this.config.load();
		}
	}
	
	public String getDriverType() {
		return (String)this.config.getProperty("fasterstats.driver.type");
	}
	
	public String getPluginVersion() {
		return this.pdf.getVersion();
	}
	
	public String getDriverHost() {
		return (String)this.config.getProperty("fasterstats.driver.host");
	}
	
	public String getDriverUser() {
		return (String)this.config.getProperty("fasterstats.driver.user");
	}
	
	public String getDriverDatabaseName() {
		return (String)this.config.getProperty("fasterstats.driver.dbname");
	}
	
	public String getDriverPassword() {
		return (String)this.config.getProperty("fasterstats.driver.password");
	}
	
}
