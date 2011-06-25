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
import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.thefaster.bukkit.Fasterstats.driver.PluginDriver;
import com.thefaster.bukkit.Fasterstats.model.PlayerStateModel;

import config.Config;

public class MySQLDriver implements PluginDriver {
	
	private Connection connect = null;
	
	static final ClassLoader loader = MySQLDriver.class.getClassLoader();

	@Override
	public void save(PlayerStateModel ps) {		 
		try {
			String query = null;			
			
			PreparedStatement statement = connect.prepareStatement(
					"UPDATE playerstats SET deaths = ?, move_range = ?, time_played = ? WHERE player_id = ?"
			);
			statement.setInt(1, ps.deaths);			
			statement.setDouble(2, ps.moveRange);
			statement.setInt(3, ps.timePlayed);
			statement.setInt(4, ps.playerId);
			statement.executeUpdate();															
			
			Iterator<Material> iterPlace = ps.blockPlace.keySet().iterator();
			while (iterPlace.hasNext()) {
				Material mat = iterPlace.next();
				Integer count = ps.blockPlace.get(mat);
				Integer matId = mat.getId();
				
				query = String.format("SELECT * FROM block_place WHERE player_id = '%d' AND material = '%d'", ps.playerId, matId);
				ResultSet result = connect.createStatement().executeQuery(query);
				if (result.next()) {
					query = String.format("UPDATE block_place SET count = '%d' WHERE player_id = '%d' AND material = '%d'", count, ps.playerId, matId);
					connect.createStatement().executeUpdate(query);
				} else {
					query = String.format(
							"INSERT INTO block_place SET player_id = '%d', material = '%d', count = '%d'",
							ps.playerId,
							matId,
							count
							);
					connect.createStatement().executeUpdate(query);
				}
			}
			
			Iterator<Material> iterBreak = ps.blockBreak.keySet().iterator();
			while (iterBreak.hasNext()) {
				Material mat = iterBreak.next();
				Integer count = ps.blockBreak.get(mat);
				Integer matId = mat.getId();
				
				query = String.format("SELECT * FROM block_break WHERE player_id = '%d' AND material = '%d'", ps.playerId, matId);
				ResultSet result = connect.createStatement().executeQuery(query);
				if (result.next()) {
					query = String.format("UPDATE block_break SET count = '%d' WHERE player_id = '%d' AND material = '%d'", count, ps.playerId, matId);
					connect.createStatement().executeUpdate(query);
				} else {
					query = String.format(
							"INSERT INTO block_break SET player_id = '%d', material = '%d', count = '%d'",
							ps.playerId,
							matId,
							count
							);
					connect.createStatement().executeUpdate(query);
				}
			}
			
			Iterator<String> iterCreature = ps.creatureKill.keySet().iterator();
			while (iterCreature.hasNext()) {
				String creature = iterCreature.next();
				Integer count = ps.creatureKill.get(creature);
				
				query = String.format("SELECT * FROM creature_kill WHERE player_id = '%d' AND creature = '%s'", ps.playerId, creature);
				ResultSet result = connect.createStatement().executeQuery(query);
				if (result.next()) {
					query = String.format("UPDATE creature_kill SET count = '%d' WHERE player_id = '%d' AND creature = '%s'", count, ps.playerId, creature);
					connect.createStatement().executeUpdate(query);
				} else {
					query = String.format(
							"INSERT INTO creature_kill SET player_id = '%d', creature = '%s', count = '%d'",
							ps.playerId,
							creature,
							count
							);
					connect.createStatement().executeUpdate(query);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
	
	public PlayerStateModel getPlayerStateFromDatabase(Player player) {
		PlayerStateModel state = new PlayerStateModel(player);
		
		try {
			String query = String.format("SELECT * FROM player WHERE name = '%s'", player.getName());
			ResultSet result = connect.createStatement().executeQuery(query);
			
			if (result.next()) {
				this.PrintDebugMessage(player.getName() + " founded in database. Getting state.");	
				
				state.playerId = result.getInt("player_id");
				state.lastLogin = result.getDate("last_login");								
				
				query = String.format("SELECT * FROM playerstats WHERE player_id = '%d'", state.playerId);
				result = connect.createStatement().executeQuery(query);
				
				if (result.next()) {
					state.deaths = result.getInt("deaths");					
					state.moveRange = result.getDouble("move_range");
					state.timePlayed = result.getInt("time_played");
				} else {
					query = String.format("INSERT INTO playerstats SET player_id = '%d'", state.playerId);
					connect.createStatement().executeUpdate(query);
				}
				
				query = String.format("SELECT * FROM block_place WHERE player_id = '%d'", state.playerId);
				result = connect.createStatement().executeQuery(query);
				
				if (result.isBeforeFirst()) {
					while (result.next()) {
						Material stateMat = Material.getMaterial(result.getInt("material"));
						Integer count = result.getInt("count");
						
						state.setPlaceBlock(stateMat, count);
					}
				}				
				
				query = String.format("SELECT * FROM block_break WHERE player_id = '%d'", state.playerId);
				result = connect.createStatement().executeQuery(query);
				
				if (result.isBeforeFirst()) {
					while (result.next()) {
						Material stateMat = Material.getMaterial(result.getInt("material"));
						Integer count = result.getInt("count");
						
						state.setBreakBlock(stateMat, count);
					}
				}
				
				query = String.format("SELECT * FROM creature_kill WHERE player_id = '%d'", state.playerId);
				result = connect.createStatement().executeQuery(query);
				
				if (result.isBeforeFirst()) {
					while (result.next()) {
						String creature = result.getString("creature");
						Integer count = result.getInt("count");
						
						state.setPlayerKill(creature, count);
					}
				}
			} else {
				this.PrintDebugMessage(player.getName() + " not founded in database. Creating new record.");
				
				query = String.format("INSERT INTO player SET name = '%s'", player.getName());
				connect.createStatement().executeUpdate(query); 
				
				query = String.format("SELECT player_id FROM player WHERE name = '%s'", player.getName());
				result = connect.createStatement().executeQuery(query);
				
				Integer playerId = 0;
				if (result.next()) {
					playerId = result.getInt("player_id");
				}				
				
				if (playerId != 0) {
					query = String.format("INSERT INTO playerstats SET player_id = '%d'", playerId);
					connect.createStatement().executeUpdate(query);
				} else {
					throw new SQLException("Can't create new player in database.");
				}
				
				
				state.playerId = playerId;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}								
		
		return state;
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
