package com.matrobot.gha.archive.cmd;

import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;

import com.matrobot.gha.Configuration;
import com.matrobot.gha.ICommand;
import com.matrobot.gha.archive.event.EventReader;
import com.matrobot.gha.archive.event.EventRecord;
import com.matrobot.gha.archive.repo.RepositoryRecord;

/**
 * Find all events for given repository or user
 * 
 * @author Krzysztof Langner
 */
public class FindEventsCmd implements ICommand{

	private EventReader eventReader;
	HashMap<String, RepositoryRecord> repos = new HashMap<String, RepositoryRecord>();
	private PrintStream outputStream;
	
	
	@Override
	public void run(Configuration params) throws IOException {

		outputStream = params.getOutputStream();
		outputStream.print(EventRecord.getCSVHeaders());
		eventReader = new EventReader(params.getMonthFolders());
		findEventByRepositoryName(params.getRepositoryName());
	}


	protected void findEventByRepositoryName(String name) throws IOException{
		
		EventRecord	record;
		
		while((record = eventReader.next()) != null){

			String repoId = record.getRepositoryId(); 
			if(repoId != null && repoId.equals(name)){
				outputStream.print(record.toCSV());
			}
		}
	}

	
	protected void findEventByUser(String name) throws IOException{
		
		EventRecord	record;
		
		while((record = eventReader.next()) != null){
			
			if(record.getCommitters().contains(name)){
				outputStream.print(record.toCSV());
			}
		}
	}
	
	/**
	 * for local testing
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		Configuration params = new Configuration("configs/events.yaml");
		
		FindEventsCmd app = new FindEventsCmd();
		app.run(params);
	}
	
}
