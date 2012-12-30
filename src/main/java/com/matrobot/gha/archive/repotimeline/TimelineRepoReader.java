package com.matrobot.gha.archive.repotimeline;

import java.util.HashMap;
import java.util.Iterator;

import com.matrobot.gha.archive.event.EventRecord;
import com.matrobot.gha.archive.event.IEventReader;


/**
 * This reader will get time series for repositories
 * Repositories which are not available in the first month will not be included.
 * 
 * @author Krzysztof Langner
 */
public class TimelineRepoReader implements ITimelineRepoReader{

	private IEventReader eventReader;
	private HashMap<String, RepoTimeline> repoData = null;
	private Iterator<RepoTimeline> dataIterator;
	private String lastDate = null; 
	private boolean canAddRepo = true;
	
	
	public TimelineRepoReader(IEventReader reader){
		this.eventReader = reader;
	}
	
	
	@Override
	public RepoTimeline next(){

		if(repoData == null){
			initRepositoryData();
		}
		
		RepoTimeline record = null;
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

		repoData = new HashMap<String, RepoTimeline>();
		EventRecord	event;
		while((event = eventReader.next()) != null){
			String label = getLabelFromDate(event.getCreatedAt());
			if(!label.equals(lastDate)){
				updateLabel(label);
			}
		
			if(event.type.equals("PushEvent")){
				updateTimeSeries(event);
			}
		}
		
		dataIterator = repoData.values().iterator();
	}


	private String getLabelFromDate(String createdAt) {

		String label = "";
		String[] tokens = createdAt.split("-");
		if(tokens.length > 2){
			label = tokens[0]+tokens[1];
		}
		
		return label;
	}


	/**
	 * Add data point for each repo.
	 */
	private void updateLabel(String label) {

		if(lastDate != null){
			for(RepoTimeline record : repoData.values()){
				record.addDataPoint(label);
			}
			canAddRepo = false;
		}
		
		lastDate = label;
	}


	private void updateTimeSeries(EventRecord event) {

		RepoTimeline record = repoData.get(event.getRepositoryId());
		
		if(record == null){
			if(canAddRepo){
				record = new RepoTimeline(event.getRepositoryId());
				record.addDataPoint(lastDate);
				repoData.put(event.getRepositoryId(), record);
			}
			else{
				return;
			}
		}
		
		record.incrementLastPointValue();
	}
	
}
