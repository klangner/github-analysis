package com.matrobot.gha.app.parser;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.matrobot.gha.app.Settings;
import com.matrobot.gha.dataset.DatasetRecord;
import com.matrobot.gha.dataset.FolderDatasetReader;
import com.matrobot.gha.dataset.RepositoryRecord;
import com.matrobot.gha.dataset.SummaryRecord;

public class RepositoryParserApp {

	private String datasetPath;
	HashMap<String, RepositoryRecord> repos = new HashMap<String, RepositoryRecord>();
	private SummaryRecord info = new SummaryRecord();
	
	
	public RepositoryParserApp(int year, int month) throws IOException{
		
		datasetPath = Settings.DATASET_PATH + year + "-" + month; 
		parseFolder();
	}

	private void parseFolder() throws IOException{
		
		FolderDatasetReader datasetReader = new FolderDatasetReader(datasetPath);
		DatasetRecord	recordData;
		
		info.eventCount = 0;
		info.newRepositoryCount = 0;
		while((recordData = datasetReader.readNextRecord()) != null){
			
			updateRepositoryData(recordData);
		}
		
		info.repositoryCount = repos.size();
	}

	
	private void updateRepositoryData(DatasetRecord recordData) {
	
		String url = recordData.getRepositoryId();
		if(url != null){
			
			RepositoryRecord data = repos.get(url);
			if(data == null){
				data = new RepositoryRecord();
				data.repository = url;
			}

			if(recordData.isCreateRepository()){
				data.isNew = true;
				info.newRepositoryCount += 1;
			}
			else if(recordData.type.equals("PushEvent")){
				data.pushEventCount += 1;
			}

			data.eventCount += 1;
			repos.put(url, data);
			
			info.eventCount ++;
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

		// Parse 2011
		for(int i = 10; i <= 12; i++){
			parseMonth(2011, i);
		}

		// Parse 2012
		for(int i = 1; i <= 10; i++){
			parseMonth(2012, i);
		}
	}

	
	private static void parseMonth(int year, int month) throws IOException {
		
		long time = System.currentTimeMillis();
		RepositoryParserApp app = new RepositoryParserApp(year, month);
		app.saveAsJson();
		time = (System.currentTimeMillis()-time)/1000;
		System.out.println("Dataset: " + year + "-" + month);
		System.out.println("Repos: " + app.repos.size() + " Events: " + app.info.eventCount);
		System.out.println("Parse time: " + time + "sec.");
		System.out.println();
	}
}
