package com.matrobot.gha.archive.user;



public class UserRecord {

	public String name;
	public int eventCount = 0;
	
	/**
	 * @return Header for CSV file
	 */
	public static String getCSVHeaders(){
		return "name,event_count";
	}
	
	/**
	 * @return CSV
	 */
	public String toCSV(){
		return name + "," + eventCount; 
	}
}
