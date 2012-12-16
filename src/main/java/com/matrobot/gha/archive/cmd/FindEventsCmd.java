package com.matrobot.gha.archive.cmd;

import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;

import com.matrobot.gha.ICommand;
import com.matrobot.gha.ParamParser;
import com.matrobot.gha.archive.EventRecord;
import com.matrobot.gha.archive.FolderArchiveReader;
import com.matrobot.gha.archive.repo.RepositoryRecord;

/**
 * Find all events for given repository or user
 * 
 * @author Krzysztof Langner
 */
public class FindEventsCmd implements ICommand{

	private String datasetPath;
	HashMap<String, RepositoryRecord> repos = new HashMap<String, RepositoryRecord>();
	private PrintStream outputStream;
	
	
	@Override
	public void run(ParamParser params) throws IOException {

		outputStream = params.getOutputStream();
		outputStream.print(EventRecord.getCSVHeaders());
		
		for(String path : params.getMonthFolders()){
			datasetPath = path;
			System.out.println("Parse: " + datasetPath);
			findEventByRepositoryName(params.getRepositoryName());
		}
	}


	protected void findEventByRepositoryName(String name) throws IOException{
		
		FolderArchiveReader datasetReader = new FolderArchiveReader(datasetPath);
		EventRecord	record;
		
		while((record = datasetReader.readNextRecord()) != null){

			String repoId = record.getRepositoryId(); 
			if(repoId != null && repoId.equals(name)){
				outputStream.print(record.toCSV());
			}
		}
	}

	
	protected void findEventByUser(String name) throws IOException{
		
		FolderArchiveReader datasetReader = new FolderArchiveReader(datasetPath);
		EventRecord	record;
		
		while((record = datasetReader.readNextRecord()) != null){
			
			if(record.getCommitters().contains(name)){
				outputStream.print(record.toCSV());
			}
		}
	}
}
