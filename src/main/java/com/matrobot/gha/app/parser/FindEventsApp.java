package com.matrobot.gha.app.parser;

import java.io.IOException;
import java.util.HashMap;

import com.matrobot.gha.app.Settings;
import com.matrobot.gha.archive.EventRecord;
import com.matrobot.gha.archive.FolderArchiveReader;
import com.matrobot.gha.archive.repo.RepositoryRecord;

public class FindEventsApp {

	private String datasetPath;
	HashMap<String, RepositoryRecord> repos = new HashMap<String, RepositoryRecord>();
	
	
	public FindEventsApp(int year, int month) throws IOException{
		
		datasetPath = Settings.DATASET_PATH + year + "-" + month; 
	}

	protected void findEventByRepositoryName(String name) throws IOException{
		
		FolderArchiveReader datasetReader = new FolderArchiveReader(datasetPath);
		EventRecord	record;
		
		while((record = datasetReader.readNextRecord()) != null){

			String repoId = record.getRepositoryId(); 
			if(repoId != null && repoId.equals(name)){
				System.out.println(record.created_at + ": " + record.type);
			}
		}
	}

	
	protected void findEventByUser(String name) throws IOException{
		
		FolderArchiveReader datasetReader = new FolderArchiveReader(datasetPath);
		EventRecord	record;
		
		while((record = datasetReader.readNextRecord()) != null){
			
			if(record.getCommitters().contains(name)){
				System.out.println(record.created_at + ": " + record.type + " " + record.getRepositoryId());
			}
		}
	}

	
	public static void main(String[] args) throws IOException {

		FindEventsApp app = new FindEventsApp(2012, 11);
		app.findEventByRepositoryName("ironbee/ironbee");
	}
}
