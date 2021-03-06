package com.matrobot.gha.archive.cmd;

import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.matrobot.gha.Configuration;
import com.matrobot.gha.ICommand;
import com.matrobot.gha.archive.event.EventReader;
import com.matrobot.gha.archive.event.EventRecord;
import com.matrobot.gha.archive.event.IEventReader;

/**
 * Check user activity after time range
 * 
 * @author Krzysztof Langner
 */
public class UserTimelineCmd implements ICommand{

	private Set<String> firstMonthUsers = new HashSet<String>();
	class Record{
		String date;
		int newUsers = 0;
		Set<String> activeUsers = new HashSet<String>();
		Set<String> users = new HashSet<String>();
	}
	
	private Set<String> allUsers = new HashSet<String>();
	private Record currentRecord = null;
	private int index = 1;
	private PrintStream outputStream;
	
	
	@Override
	public void run(Configuration params) throws IOException {

		IEventReader reader;
		List<String> months = params.getMonthFolders();
		reader = new EventReader(months.remove(0));
		initFirstMonth(reader);
		initOutputStream(params);
		reader = new EventReader(months);
		parse(reader);
	}


	private void initFirstMonth(IEventReader reader) {
		EventRecord	event;
		while((event = reader.next()) != null){
			String actor = event.getActorLogin();
			if(actor != null){
				firstMonthUsers.add(actor);
				allUsers.add(actor);
			}
		}
	}


	private void initOutputStream(Configuration params) {
		outputStream = params.getOutputStream(); 
		outputStream.println("index,date,active,new,all");
		int userCount = firstMonthUsers.size();
		outputStream.println("0," + params.getStartDate() + "," + userCount + "," + userCount + "," + userCount);
	}


	private void parse(IEventReader reader) {
		
		EventRecord	event;
		while((event = reader.next()) != null){
			prepareCurrentRecord(event);
			String actor = event.getActorLogin();
			if(firstMonthUsers.contains(actor)){
				currentRecord.activeUsers.add(actor);
			}
			
			if(!allUsers.contains(actor)){
				allUsers.add(actor);
				currentRecord.newUsers ++;
			}
			
			currentRecord.users.add(actor);
		}
		saveRecord();
	}


	private void prepareCurrentRecord(EventRecord event) {

		String date = event.getCreatedAt().substring(0, 7);
		
		if(currentRecord == null){
			System.out.println(date);
			currentRecord = new Record();
			currentRecord.date = date;
		}
		else if(!currentRecord.date.equals(date)){
			System.out.println(date);
			saveRecord();
			currentRecord = new Record();
			currentRecord.date = date;
		}
	}


	private void saveRecord(){
		outputStream.println(index + "," + currentRecord.date + "," + currentRecord.activeUsers.size() +
				"," + currentRecord.newUsers + "," + currentRecord.users.size());
		index++;
	}

	
	/**
	 * for local testing
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		Configuration params = new Configuration("configs/users_timeline.yaml");
		
		UserTimelineCmd app = new UserTimelineCmd();
		app.run(params);
	}
	
}
