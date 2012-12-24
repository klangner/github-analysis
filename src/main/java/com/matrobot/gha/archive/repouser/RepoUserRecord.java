package com.matrobot.gha.archive.repouser;


public class RepoUserRecord {

	public String key;
	public String repoName;
	public String userName;
	public int eventCount = 0;
	
	public RepoUserRecord(String key){
		this.key = key;
	}
	
	
	/**
	 * @return Header for CSV file
	 */
	public static String getCSVHeaders(){
		return "key,repository,user,event_count";
	}
	
	/**
	 * @return CSV
	 */
	public String toCSV(){
		return key + "," + repoName + "," + userName + "," + eventCount; 
	}
}
