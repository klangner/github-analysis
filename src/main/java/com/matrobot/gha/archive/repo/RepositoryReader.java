package com.matrobot.gha.archive.repo;

import java.util.HashMap;

import com.matrobot.gha.archive.event.EventRecord;
import com.matrobot.gha.archive.event.IEventReader;


/**
 * This reader will filter events based on given parameters
 * 
 * @author Krzysztof Langner
 */
public class RepositoryReader implements IRepositoryReader{

	private IEventReader eventReader;
	private HashMap<String, RepositoryRecord> repoData = null;
	
	
	public RepositoryReader(IEventReader reader){
		this.eventReader = reader;
	}
	
	
	@Override
	public RepositoryRecord next(){

		if(repoData == null){
			initRepositoryData();
		}
		
		RepositoryRecord record = null;
		if(repoData.size() > 0){
			String key = repoData.keySet().iterator().next();
			record = repoData.remove(key);
		}
		
		return record;
	}
	

	/**
	 * Parse all event to get information about repository
	 * This is time consuming function.
	 */
	private void initRepositoryData(){

		repoData = new HashMap<String, RepositoryRecord>();
		EventRecord	recordData;
		while((recordData = eventReader.next()) != null){
			updateRepositoryData(recordData);
		}
	}
	
	private void updateRepositoryData(EventRecord event) {
		
		String url = event.getRepositoryId();
		if(url != null){
			
			RepositoryRecord record = repoData.get(url);
			if(record == null){
				record = new RepositoryRecord(url);
			}

			if(event.isCreateRepository()){
				record.isNew = true;
			}
			else if(event.type.equals("PushEvent")){
				addPushEventToRepository(event, record);
			}
			else if(event.type.equals("PullRequestEvent")){
				addPullRequestEventToRepository(event, record);
			}
			else if(event.type.equals("IssuesEvent")){
				record.issueOpenEventCount += 1;
			}
			else if(event.type.equals("ForkEvent")){
				record.forkEventCount += 1;
			}
			
			if(event.getActorLogin() != null){
				record.community.add(event.getActorLogin());
			}
			record.eventCount += 1;
			repoData.put(url, record);
			
		}
	}

	private void addPushEventToRepository(EventRecord event, RepositoryRecord record) {
		
		if(event.payload.size > 0){
			record.pushEventCount += 1;
			for(String committer : event.getCommitters()){
				record.committers.add(committer);
			}
		}
	}
	
	private void addPullRequestEventToRepository(EventRecord event, RepositoryRecord record) {
		
		if(event.payload.action.equals("opened")){
			record.openedPullCount += 1;
		}
		else if(event.payload.action.equals("closed")){
			record.closedPullCount += 1;
		}
	}
	
}
