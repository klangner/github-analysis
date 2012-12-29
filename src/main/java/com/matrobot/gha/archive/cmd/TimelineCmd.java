package com.matrobot.gha.archive.cmd;

import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import com.matrobot.gha.Configuration;
import com.matrobot.gha.ICommand;
import com.matrobot.gha.archive.event.EventReader;
import com.matrobot.gha.archive.event.EventRecord;
import com.matrobot.gha.archive.event.IEventReader;

/**
 * Find repository state by going back in history events.
 * E.g.
 * Find number of forks in 2011-2 by counting back from 
 * 
 * @author Krzysztof Langner
 */
public class TimelineCmd implements ICommand{

	class Record{
		String date;
		int totalActivity = 0;
		int newUsers = 0;
		Set<String> activeUsers = new HashSet<String>();
	}
	
	private Set<String> allUsers = new HashSet<String>();
	private Record currentRecord = null;
	private int index = 1;
	private PrintStream outputStream;
	private int dateLength = 7;
	
	
	@Override
	public void run(Configuration params) throws IOException {

		initDateLength(params);
		initOutputStream(params);
		IEventReader reader;
		reader = new EventReader(params.getMonthFolders());
		parse(reader);
	}


	private void initDateLength(Configuration params) {

		if(params.getDateResolution() != null && params.getDateResolution().equals("day")){
			dateLength = 10;
		}
	}


	private void initOutputStream(Configuration params) {
		outputStream = params.getOutputStream(); 
		outputStream.println("index,date,nu,au,ta");
	}


	private void parse(IEventReader reader) {
		
		EventRecord	event;
		while((event = reader.next()) != null){
			prepareCurrentRecord(event);
			String actor = event.getActorLogin();
			if(actor != null){
				currentRecord.activeUsers.add(actor);
				if(!allUsers.contains(actor)){
					currentRecord.newUsers ++;
					allUsers.add(actor);
				}
			}
			currentRecord.totalActivity ++;
		}
		
		saveRecord(currentRecord);
	}


	private void prepareCurrentRecord(EventRecord event) {

		String date = event.created_at.substring(0, dateLength);
		
		if(currentRecord == null){
			currentRecord = new Record();
			currentRecord.date = date;
		}
		else if(!currentRecord.date.equals(date)){
			System.out.println(date);
			saveRecord(currentRecord);
			currentRecord = new Record();
			currentRecord.date = date;
		}
	}


	private void saveRecord(Record record){
		outputStream.println(
			index + "," + record.date + "," + record.newUsers + "," + 
			record.activeUsers.size() + "," + record.totalActivity 
		);
		index++;
	}

	
	/**
	 * for local testing
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		Configuration params = new Configuration("configs/timeline.yaml");
		
		TimelineCmd app = new TimelineCmd();
		app.run(params);
	}
	
}
