package com.matrobot.gha.archive.event;


/**
 * This reader will filter events based on given parameters
 * 
 * @author Krzysztof Langner
 */
public class FilteredEventReader implements IEventReader{

	private IEventReader reader;
	private String repoName;
	private String actor;
	
	
	public FilteredEventReader(IEventReader reader){
		this.reader = reader;
	}
	
	
	/**
	 * Filter events to this repository
	 * @param name
	 */
	public void setRepoName(String name){
		repoName = name;
	}
	
	
	@Override
	public EventRecord next(){

		EventRecord record;
		
		while((record=reader.next()) != null){
			
			if((repoName == null || repoName.equals(record.getRepositoryId())) &&
				(actor == null || actor.equals(record.getActorLogin())) )
			{
				break;
			}
		}
		
		return record;
	}


	public void setActor(String actor) {
		this.actor = actor;
	}
}
