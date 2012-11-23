package com.matrobot.gha.cmd;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.matrobot.gha.dataset.ActivityRecord;
import com.matrobot.gha.dataset.DataRecord;
import com.matrobot.gha.dataset.FolderDatasetReader;
import com.matrobot.gha.dataset.IDatasetReader;

public class ParseActivityApp {

	private static final String DATASET_PATH = "/home/klangner/datasets/github/";
	HashMap<String, ActivityRecord> repos = new HashMap<String, ActivityRecord>();
	
	
	public static void main(String[] args) throws IOException {

		long time = System.currentTimeMillis();
		ParseActivityApp app = new ParseActivityApp();
		int beforeCount = app.scanMonth(DATASET_PATH+"2012/2/", true);
		int afterCount = app.scanMonth(DATASET_PATH+"2012/3/", false);
		
		app.saveAsJson(DATASET_PATH+"2012/3/");

		time = (System.currentTimeMillis()-time)/1000;
		System.out.println("Before: " + beforeCount + " After: " + afterCount + " in: " + time + "sec.");
	}
	
	private int scanMonth(String folder, boolean isFirst) throws IOException{
		
		IDatasetReader datasetReader = new FolderDatasetReader(folder);
		DataRecord	recordData;
		
		int count = 0;
		while((recordData = datasetReader.readNextRecord()) != null){
			
			String url = recordData.getRepositoryId();
			if(url != null){
				ActivityRecord data = repos.get(url);
				if(data == null){
					data = new ActivityRecord();
					data.repository = url;
				}
				
				if(isFirst){
					data.currentMonth += 1;
				}
				else{
					data.nextMonth += 1;
				}
				repos.put(url, data);
			}
			count ++;
		}
		
		return count;
	}

	private void saveAsJson(String path) {

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		try{
			FileWriter writer = new FileWriter(path+"activity_changes.json");
			String json = gson.toJson(repos.values());
			writer.write(json);
			writer.close();
		}catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}
	}

}
