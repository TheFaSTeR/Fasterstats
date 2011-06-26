package com.thefaster.bukkit.Fasterstats.helper;

public class TimeHelper {
	
	public static String getSecondsAsPlayedTimeString(long secondsPlayed) {
		String result = "";
		long days, hours, minutes, seconds;
		long remain = secondsPlayed;
		
		if (remain >= 86400) {
			days = (long) Math.floor(remain / 86400);
			remain = (remain - (days * 86400)); 			
			result += days + "d ";			
		}
		if (remain >= 3600) {
			hours = (long) Math.floor(remain / 3600);
			remain = (remain - (hours * 3600));
			result += hours + "h ";
		}
		if (remain >= 60) {
			minutes = (long) Math.floor(remain / 60);
			remain = (remain - (minutes * 60));
			result += minutes + "m ";
		}
		if (remain > 0) {
			seconds = remain;
			result += seconds + "s ";
		}
		
		return result;
	}
	
}
