package com.matrobot.gha.archive.app;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import com.matrobot.gha.ICommand;
import com.matrobot.gha.ParamParser;
import com.matrobot.gha.archive.EventRecord;
import com.matrobot.gha.archive.FolderArchiveReader;
import com.matrobot.gha.archive.repo.RepositoryRecord;

public class FindEventsApp implements ICommand{

	Properties prop = new Properties();
	private String datasetPath;
	HashMap<String, RepositoryRecord> repos = new HashMap<String, RepositoryRecord>();
	
	
	@Override
	public void run(ParamParser params) {
		// TODO Auto-generated method stub
		
	}

	public FindEventsApp(int year, int month) throws IOException{
		
		prop.load(new FileInputStream("config.properties"));
		datasetPath = prop.getProperty("data_path") + year + "-" + month; 
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

		FindEventsApp app = new FindEventsApp(2011, 6);
		app.findEventByRepositoryName("rubinius/rubinius");
	}
}
