package com.matrobot.gha.archive.repouser;

import java.util.HashMap;

import com.matrobot.gha.archive.event.EventRecord;
import com.matrobot.gha.archive.event.IEventReader;


/**
 * This reader will filter events based on given parameters
 * 
 * @author Krzysztof Langner
 */
public class RepoUserReader implements IRepoUserReader{

	private IEventReader eventReader;
	private HashMap<String, RepoUserRecord> repoData = null;
	
	
	public RepoUserReader(IEventReader reader){
		this.eventReader = reader;
	}
	
	
	@Override
	public RepoUserRecord next(){

		if(repoData == null){
			initRepositoryData();
		}
		
		RepoUserRecord record = null;
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

		repoData = new HashMap<String, RepoUserRecord>();
		EventRecord	recordData;
		while((recordData = eventReader.next()) != null){
			updateRepositoryData(recordData);
		}
	}
	
	private void updateRepositoryData(EventRecord event) {
		
		String repoName = event.getRepositoryId();
		String userName = event.getActorLogin();
		if(repoName != null && userName != null){
			
			String key = repoName + "#" + userName;
			RepoUserRecord record = repoData.get(key);
			if(record == null){
				record = new RepoUserRecord(key);
				record.repoName = repoName;
				record.userName = userName;
			}

			record.eventCount += 1;
			repoData.put(key, record);
			
		}
	}

}
