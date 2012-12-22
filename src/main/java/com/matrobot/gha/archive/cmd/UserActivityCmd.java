package com.matrobot.gha.archive.cmd;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import com.matrobot.gha.Configuration;
import com.matrobot.gha.ICommand;
import com.matrobot.gha.archive.event.EventReader;
import com.matrobot.gha.archive.event.IEventReader;
import com.matrobot.gha.archive.user.FilteredUserReader;
import com.matrobot.gha.archive.user.IUserReader;
import com.matrobot.gha.archive.user.UserReader;
import com.matrobot.gha.archive.user.UserRecord;

/**
 * Parse archive and create reports with repository info by months
 * 
 * @author Krzysztof Langner
 */
public class UserActivityCmd implements ICommand{

	private IUserReader reader;
	private HashMap<String, String> staticCSVFields = new HashMap<String, String>();
	
	
	@Override
	public void run(Configuration params) throws IOException {

		IEventReader eventReader = createEventReader(params);
		createUserReader(params, eventReader);
		saveAsCSV(params.getOutputStream());
	}

	/**
	 * Filter by repository if repository param provided
	 */
	private IEventReader createEventReader(Configuration params) {
		IEventReader eventReader = new EventReader(params.getMonthFolders());
		return eventReader;
	}

	
	/**
	 * Create repository reader. 
	 * Add:
	 * - min activity filter
	 * - ordered reader
	 */
	private void createUserReader(Configuration params, IEventReader eventReader) {
		UserReader userReader = new UserReader(eventReader);
		reader = userReader;
		if(params.getMinActivity() > 0){
			FilteredUserReader filteredReader = new FilteredUserReader(reader);
			filteredReader.setMinActivity(params.getMinActivity());
			reader = filteredReader;
		}
	}

	private void saveAsCSV(PrintStream printStream) throws UnsupportedEncodingException, IOException {

		for(String key : staticCSVFields.keySet()){
			printStream.print(key + ",");
		}
		printStream.println(UserRecord.getCSVHeaders());
		
		UserRecord record;
		while((record = reader.next()) != null){
			for(String value : staticCSVFields.values()){
				printStream.print(value + ",");
			}
			printStream.println(record.toCSV());
		}
	}

	
	/**
	 * Export data for matrobot.com website
	 */
	public static void main(String[] args) throws IOException {

		UserActivityCmd app = new UserActivityCmd();
		Configuration params = new Configuration("configs/export_users.yaml");
		
		String[] date = params.getStartDate().split("-");
		if(date.length == 2){
			app.staticCSVFields.put("year", date[0]);
			app.staticCSVFields.put("month", date[1]);
		}
		app.run(params);
	}
	
}
