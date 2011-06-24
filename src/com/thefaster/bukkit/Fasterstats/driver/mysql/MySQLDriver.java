package com.thefaster.bukkit.Fasterstats.driver.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.bukkit.entity.Player;

import com.thefaster.bukkit.Fasterstats.driver.PluginDriver;
import com.thefaster.bukkit.Fasterstats.model.PlayerState;

import config.Config;

public class MySQLDriver implements PluginDriver {
	
	private Connection connect = null;
	
	static final ClassLoader loader = MySQLDriver.class.getClassLoader();

	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() throws Exception { 
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			connect = DriverManager.getConnection(String.format(
					"jdbc:mysql://%s/%s?" + "user=%s&password=%s", 
					Config.getDbHost(),
					Config.getDbName(),
					Config.getDbUser(),
					Config.getDbPassword()
					));
		
			if (!this.isDatabaseExist()) {
				this.createNewDatabase();
			}
						
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	private boolean isDatabaseExist() throws SQLException {
		Statement statement = connect.createStatement();
		boolean founded = false;
		
		ResultSet resultSet = statement.executeQuery("SHOW DATABASES");
		while (resultSet.next()) {
			if (resultSet.getString("Database").equals(Config.getDbName())) {
				founded = true;
				System.out.println("[Fasterstats] Database founded!");
				break;
			}
		}
		
		return founded;
	}
	
	private void createNewDatabase() {
		
	}
	
	public PlayerState getPlayerStateFromDatabase(Player player) {				
		try {
			String query = String.format("SELECT * FROM player WHERE name = '%s'", player.getName());
			ResultSet result = connect.createStatement().executeQuery(query);
			
			if (result.next()) {
				this.PrintDebugMessage(player.getName() + " founded in database. Getting state.");			
			} else {
				this.PrintDebugMessage(player.getName() + " not founded in database. Creating new record.");
				
				query = String.format("INSERT INTO player SET name = '%s'", player.getName());
				connect.createStatement().executeUpdate(query); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}								
		
		return new PlayerState();
	}
	
	public void setPlayerOnline(Player player) {
		try {
			Date now = Calendar.getInstance().getTime();
			
			String query = "UPDATE player SET online = 1, last_login = ? WHERE name = ?";
			
			PreparedStatement ps = connect.prepareStatement(query);
			
			ps.setTimestamp(1, new Timestamp(now.getTime()));
			ps.setString(2, player.getName());			
			ps.executeUpdate();		
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setPlayerOffline(Player player) {
		try {
			String query = String.format("UPDATE player SET online = 0 WHERE name = '%s'", player.getName());
			connect.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void PrintDebugMessage(String value) {
		System.out.println("[Fasterstats] db message: " + value);
	}

}
