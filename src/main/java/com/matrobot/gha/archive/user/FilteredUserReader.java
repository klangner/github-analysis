package com.matrobot.gha.archive.user;


/**
 * This reader will filter events based on given parameters
 * 
 * @author Krzysztof Langner
 */
public class FilteredUserReader implements IUserReader{

	private IUserReader reader;
	private int minActivity = 0;
	
	
	public FilteredUserReader(IUserReader reader){
		this.reader = reader;
	}
	
	
	/**
	 * Filter events to this repository
	 * @param name
	 */
	public void setMinActivity(int minActivity){
		this.minActivity = minActivity;
	}
	
	
	@Override
	public UserRecord next(){

		UserRecord record;
		
		while((record=reader.next()) != null){
			
			if(record.eventCount >= minActivity){
				break;
			}
		}
		
		return record;
	}
}
