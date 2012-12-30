package com.matrobot.gha.archive.event;

import java.util.HashSet;
import java.util.Set;


/**
 * This reader will filter events based on given parameters
 * 
 * @author Krzysztof Langner
 */
public class FilteredEventReader implements IEventReader{

	private IEventReader reader;
	private Set<String> repoNames = new HashSet<String>();
	private String actor;
	private Set<String> eventTypes;
	
	
	public FilteredEventReader(IEventReader reader){
		this.reader = reader;
	}
	
	
	/**
	 * Filter events to this repository
	 * @param name
	 */
	public void addRepoFilter(String name){
		repoNames.add(name);
	}
	
	
	@Override
	public EventRecord next(){

		EventRecord event;
		
		while((event=reader.next()) != null){
			
			if(repoNames.contains(event.getRepositoryId()) &&
				(actor == null || actor.equals(event.getActorLogin())) &&
				(eventTypes == null || eventTypes.contains(event.type)))
			{
				break;
			}
		}
		
		return event;
	}


	public void setActor(String actor) {
		this.actor = actor;
	}


	public void addEventType(String type) {

		if(eventTypes == null){
			 eventTypes = new HashSet<String>();
		}
		
		eventTypes.add(type);
	}
}
