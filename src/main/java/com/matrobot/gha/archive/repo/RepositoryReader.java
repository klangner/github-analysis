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

			record.updateData(event);
			repoData.put(url, record);
			
		}
	}

}
