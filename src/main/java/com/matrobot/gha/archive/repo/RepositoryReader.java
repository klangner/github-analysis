package com.matrobot.gha.archive.repo;

import java.util.HashMap;
import java.util.Iterator;

import com.matrobot.gha.archive.event.EventReader;
import com.matrobot.gha.archive.event.EventRecord;


/**
 * This reader will filter events based on given parameters
 * 
 * @author Krzysztof Langner
 */
public class RepositoryReader {

	private EventReader eventReader;
	private HashMap<String, RepositoryRecord> repoData = null;
	private Iterator<RepositoryRecord> dataIterator;
	
	
	public RepositoryReader(EventReader reader){
		this.eventReader = reader;
	}
	
	
	public RepositoryRecord next(){

		if(repoData == null){
			initRepositoryData();
		}
		
		RepositoryRecord record = null;
		if(dataIterator.hasNext()){
			record = dataIterator.next();
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
		
		dataIterator = repoData.values().iterator();
	}
	
	private void updateRepositoryData(EventRecord event) {
		
		String url = event.getRepositoryId();
		if(url != null){
			
			RepositoryRecord record = repoData.get(url);
			if(record == null){
				record = new RepositoryRecord();
				record.repository = url;
			}

			if(event.isCreateRepository()){
				record.isNew = true;
			}
			else if(event.type.equals("PushEvent")){
				addPushEventToRepository(event, record);
			}
			else if(event.type.equals("IssuesEvent")){
				record.issueOpenEventCount += 1;
			}
			else if(event.type.equals("ForkEvent")){
				record.forkEventCount += 1;
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
				record.community.add(committer);
			}
		}
	}
	
}