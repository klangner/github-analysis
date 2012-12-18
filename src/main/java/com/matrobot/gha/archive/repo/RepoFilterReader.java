package com.matrobot.gha.archive.repo;


/**
 * This reader will filter events based on given parameters
 * 
 * @author Krzysztof Langner
 */
public class RepoFilterReader implements IRepositoryReader{

	private IRepositoryReader reader;
	private int minActivity = 0;
	
	
	public RepoFilterReader(IRepositoryReader reader){
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
	public RepositoryRecord next(){

		RepositoryRecord record;
		
		while((record=reader.next()) != null){
			
			if(record.pushEventCount >= minActivity){
				break;
			}
		}
		
		return record;
	}
}
