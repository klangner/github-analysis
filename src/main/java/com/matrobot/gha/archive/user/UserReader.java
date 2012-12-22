package com.matrobot.gha.archive.user;

import java.util.HashMap;

import com.matrobot.gha.archive.event.EventRecord;
import com.matrobot.gha.archive.event.IEventReader;


/**
 * This reader will filter events based on given parameters
 * 
 * @author Krzysztof Langner
 */
public class UserReader implements IUserReader{

	private IEventReader eventReader;
	private HashMap<String, UserRecord> userData = null;
	
	
	public UserReader(IEventReader reader){
		this.eventReader = reader;
	}
	
	
	@Override
	public UserRecord next(){

		if(userData == null){
			initRepositoryData();
		}
		
		UserRecord record = null;
		if(userData.size() > 0){
			String key = userData.keySet().iterator().next();
			record = userData.remove(key);
		}
		
		return record;
	}
	

	/**
	 * Parse all event to get information about repository
	 * This is time consuming function.
	 */
	private void initRepositoryData(){

		userData = new HashMap<String, UserRecord>();
		EventRecord	recordData;
		while((recordData = eventReader.next()) != null){
			updateUserData(recordData);
		}
	}
	
	private void updateUserData(EventRecord event) {
		
		String userLogin = event.getActorLogin();
		if(userLogin != null){
			
			UserRecord record = userData.get(userLogin);
			if(record == null){
				record = new UserRecord();
				record.name = userLogin;
			}

			record.eventCount += 1;
			userData.put(userLogin, record);
		}
	}

}
