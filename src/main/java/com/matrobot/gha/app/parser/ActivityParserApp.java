package com.matrobot.gha.app.parser;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.matrobot.gha.app.Settings;
import com.matrobot.gha.dataset.DatasetRecord;
import com.matrobot.gha.dataset.FolderDatasetReader;
import com.matrobot.gha.dataset.RepositoryRecord;
import com.matrobot.gha.dataset.SummaryRecord;

public class ActivityParserApp {

	private String datasetPath;
	HashMap<String, RepositoryRecord> repos = new HashMap<String, RepositoryRecord>();
	List<String> newRepositoriesThisMonth = new ArrayList<String>();
	private SummaryRecord info = new SummaryRecord();
	
	
	public ActivityParserApp(int year, int month) throws IOException{
		
		datasetPath = Settings.DATASET_PATH + year + "-" + month; 
		parseFolder();
		removeNewRepos();
	}

	private void parseFolder() throws IOException{
		
		FolderDatasetReader datasetReader = new FolderDatasetReader(datasetPath);
		DatasetRecord	recordData;
		
		info.eventCount = 0;
		while((recordData = datasetReader.readNextRecord()) != null){
			
			String url = recordData.getRepositoryId();
			if(url != null){
				
				if(recordData.type.equals("CreateEvent")){
					newRepositoriesThisMonth.add(url);
				}
				else if(recordData.type.equals("PushEvent")){
					RepositoryRecord data = repos.get(url);
					if(data == null){
						data = new RepositoryRecord();
						data.repository = url;
					}
					
					data.activity += 1;
					repos.put(url, data);
				}
				info.eventCount ++;
			}
		}
		
		info.repositoryCount = repos.size();
		info.newRepositoryCount = newRepositoriesThisMonth.size();
	}

	private void removeNewRepos() {

		for(String repositoryName : newRepositoriesThisMonth){
			repos.remove(repositoryName);
		}
	}

	public void saveAsJson() {
	
		FileWriter writer;
		String json;
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		try{
			writer = new FileWriter(datasetPath+"/repositories.json");
			json = gson.toJson(repos.values());
			writer.write(json);
			writer.close();
			
			writer = new FileWriter(datasetPath+"/summary.json");
			json = gson.toJson(info);
			writer.write(json);
			writer.close();
			
		}catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}
	}

	public static void main(String[] args) throws IOException {

		long time = System.currentTimeMillis();
		ActivityParserApp app = new ActivityParserApp(2012, 10);
		app.saveAsJson();
		time = (System.currentTimeMillis()-time)/1000;
		System.out.println("Push events: " + app.newRepositoriesThisMonth.size() + " Repos: " + app.repos.size());
		System.out.println("Events: " + app.info.eventCount + " parse time: " + time + "sec.");
	}
}
