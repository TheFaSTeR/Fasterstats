package config;

public class Config {
	public static String PLUGIN_VERSION = "0.0.0";		
	
	public static String DATABASE_DRIVER = "mysql";
	
	public static String getDbUser() {
		return "fasterstats";
	}
	
	public static String getDbHost() {
		return "localhost";
	}
	
	public static String getDbName() {
		return "fasterstats";
	}
	
	public static String getDbPassword() {
		return "1234";
	}	
}
