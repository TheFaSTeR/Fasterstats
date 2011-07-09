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

import com.thefaster.bukkit.Fasterstats.Fasterstats;
import com.thefaster.bukkit.Fasterstats.driver.PluginDriver;
import com.thefaster.bukkit.Fasterstats.model.PlayerStateModel;

public class MySQLDriver implements PluginDriver {
	
	private Connection connect = null;
	private Fasterstats core = null;
	
	private static Integer DB_VERSION = 1;
	
	static final ClassLoader loader = MySQLDriver.class.getClassLoader();

	@Override
	public void save(PlayerStateModel ps) {		 
		try {
			String query = null;			
			
			PreparedStatement statement = connect.prepareStatement(
					"UPDATE playerstats SET deaths = ?, move_range = ?, time_played = ?, damage_done = ?, damage_get = ? WHERE player_id = ?"
			);
			statement.setInt(1, ps.deaths);			
			statement.setDouble(2, ps.moveRange);
			statement.setLong(3, ps.timePlayed);
			statement.setLong(4, ps.damageDone);
			statement.setLong(5, ps.damageGet);
			statement.setInt(6, ps.playerId);
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
	public void init(Fasterstats instance) throws Exception {
		this.core = instance;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			connect = DriverManager.getConnection(String.format(
					"jdbc:mysql://%s/%s?" + "user=%s&password=%s",
					this.core.config.getDriverHost(),
					this.core.config.getDriverDatabaseName(),
					this.core.config.getDriverUser(),
					this.core.config.getDriverPassword()					
					));
								
			if (!this.isDatabaseExist()) {
				this.core.log.info("[Fasterstats] database not found. Trying to create.");
				this.createNewDatabase();
				this.core.log.info("[Fasterstats] database dump successfully loaded.");
			} else {
				System.out.println("[Fasterstats] checking database version...");
				if (!this.isUpToDateDatabase()) {
					this.updateDatabase();
				}
			}
						
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	private void updateDatabase() {
		System.out.println("[Fasterstats] update database. please wait...");
	}
	
	private boolean isUpToDateDatabase() {
		try {
			ResultSet result = connect.createStatement().executeQuery("SELECT db_version FROM db_version");
			if (result.next()) {
				if (MySQLDriver.DB_VERSION == result.getInt("db_version")) {
					System.out.println("[Fasterstats] database version OK!");
					return true;
				}			
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean isDatabaseExist() throws SQLException {
		Statement statement = connect.createStatement();
		boolean founded = false;
		
		ResultSet resultSet = statement.executeQuery("SHOW DATABASES");
		while (resultSet.next()) {
			if (resultSet.getString("Database").equals(this.core.config.getDriverDatabaseName())) {				
				System.out.println("[Fasterstats] Database founded!");
				ResultSet result = statement.executeQuery("SHOW TABLES FROM " + this.core.config.getDriverDatabaseName());
				if (result.next()) {
					founded = true;
				} else {
					System.out.println("[Fasterstats] Database is empty.");
					founded = false;
				}
				break;
			}
		}
		
		return founded;
	}
	
	private void createNewDatabase() throws Exception {
		String dump = this.core.config.getDatabaseSqlContent();
		if (dump == null) {
			throw new Exception("[Fasterstats] can't load dump.sql");
		}
		
		String query[] = dump.split(";");
		
		for (int i = 0; i < query.length - 1; i ++) {	
			System.out.println(query[i]);
			connect.createStatement().execute(query[i] + ";");
		}								
	}
	
	public PlayerStateModel getPlayerStateFromDatabase(String playerName) {
		PlayerStateModel state = null;
		PreparedStatement stmt = null;		
		
		try {
			state = this.collectPlayerStateFromDatabase(playerName);
			if (state == null) {
				state = new PlayerStateModel(playerName);
				
				String query = "INSERT INTO player SET name = ?";
				stmt = connect.prepareStatement(query);
				stmt.setString(1, state.name);
				stmt.executeUpdate();																	
				
				query = "SELECT player_id FROM player WHERE name = ?";
				stmt = connect.prepareStatement(query);
				stmt.setString(1, state.name);
				ResultSet result = stmt.executeQuery();
				
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
	
	public PlayerStateModel requestPlayerStateFromDatabase(String playerName) {
		return collectPlayerStateFromDatabase(playerName);
	}
	
	private PlayerStateModel collectPlayerStateFromDatabase(String playerName) {
		PreparedStatement stmt = null;
		ResultSet result = null;
		PlayerStateModel state = null;
		
		try {
			String query = "SELECT * FROM player WHERE name = ?";
			stmt = connect.prepareStatement(query);
			stmt.setString(1, playerName);
			result = stmt.executeQuery();						
			
			if (result.next()) {
				state = new PlayerStateModel(playerName);
				state.playerId = result.getInt("player_id");
				state.lastLogin = result.getTimestamp("last_login");								
				
				query = String.format("SELECT * FROM playerstats WHERE player_id = '%d'", state.playerId);
				result = connect.createStatement().executeQuery(query);
				
				if (result.next()) {
					state.deaths = result.getInt("deaths");					
					state.moveRange = result.getDouble("move_range");
					state.timePlayed = result.getLong("time_played");
					state.damageDone = result.getLong("damage_done");
					state.damageGet = result.getLong("damage_get");
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

}
