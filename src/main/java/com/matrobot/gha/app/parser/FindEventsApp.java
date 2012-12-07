package com.matrobot.gha.app.parser;

import java.io.IOException;
import java.util.HashMap;

import com.matrobot.gha.app.Settings;
import com.matrobot.gha.dataset.EventRecord;
import com.matrobot.gha.dataset.FolderDatasetReader;
import com.matrobot.gha.dataset.repo.RepositoryRecord;

public class FindEventsApp {

	private String datasetPath;
	HashMap<String, RepositoryRecord> repos = new HashMap<String, RepositoryRecord>();
	
	
	public FindEventsApp(int year, int month) throws IOException{
		
		datasetPath = Settings.DATASET_PATH + year + "-" + month; 
	}

	protected void findEventByRepositoryName(String name) throws IOException{
		
		FolderDatasetReader datasetReader = new FolderDatasetReader(datasetPath);
		EventRecord	record;
		
		while((record = datasetReader.readNextRecord()) != null){
			
			if(record.getRepositoryId().equals(name)){
				System.out.println(record.created_at + ": " + record.type);
			}
		}
	}

	
	protected void findEventByUser(String name) throws IOException{
		
		FolderDatasetReader datasetReader = new FolderDatasetReader(datasetPath);
		EventRecord	record;
		
		while((record = datasetReader.readNextRecord()) != null){
			
			if(record.getCommitters().contains(name)){
				System.out.println(record.created_at + ": " + record.type + " " + record.getRepositoryId());
			}
		}
	}

	
	public static void main(String[] args) throws IOException {

		FindEventsApp app = new FindEventsApp(2012, 11);
		app.findEventByUser("igrigorik");
	}
}
