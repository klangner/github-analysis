package com.matrobot.gha.archive.event;


/**
 * This reader will filter events based on given parameters
 * 
 * @author Krzysztof Langner
 */
public class EventFilterReader {

	private EventReader reader;
	private String repoName;
	
	
	public EventFilterReader(EventReader reader){
		this.reader = reader;
	}
	
	
	/**
	 * Filter events to this repository
	 * @param name
	 */
	public void setRepoName(String name){
		repoName = name;
	}
	
	
	public EventRecord next(){

		EventRecord record;
		
		while((record=reader.next()) != null){
			
			if(repoName == null || repoName.equals(record.getRepositoryId()) ){
				break;
			}
		}
		
		return record;
	}
}