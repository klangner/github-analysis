package com.matrobot.gha.cmd;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import com.google.gson.Gson;
import com.matrobot.gha.dataset.ActivityRecord;
import com.matrobot.gha.dataset.DataRecord;
import com.matrobot.gha.dataset.FolderDatasetReader;
import com.matrobot.gha.dataset.IDatasetReader;

public class ActivityApp {

	private static final String DATASET_PATH = "/home/klangner/datasets/github/2012/";
	HashMap<String, ActivityRecord> repos = new HashMap<String, ActivityRecord>();
	
	
	public static void main(String[] args) throws IOException {

		long time = System.currentTimeMillis();
		ActivityApp app = new ActivityApp();
		app.scanMonth(2);
		app.scanMonth(3);
		
		app.saveAsJson();
		app.saveAsCSV();

		time = (System.currentTimeMillis()-time)/1000;
		System.out.println("Found Count: " + app.repos.size() + " in: " + time + "sec.");
	}
	
	private void scanMonth(int month) throws IOException{
		
		IDatasetReader datasetReader = new FolderDatasetReader(DATASET_PATH+month);
		DataRecord	recordData;
		
		while((recordData = datasetReader.readNextRecord()) != null){
			
			if(recordData.repo != null){
				ActivityRecord data = repos.get(recordData.repo.url);
				if(data == null){
					data = new ActivityRecord();
					data.repository = recordData.repo.url;
				}
				
				if(month == 2){
					data.february += 1;
				}
				else if(month == 3){
					data.march += 1;
				}
				repos.put(recordData.repo.url, data);
			}
		}
		
	}

	private void saveAsJson() {

		Gson gson = new Gson();
		
		try{
			FileWriter fstream = new FileWriter(DATASET_PATH+"activity.json");
			BufferedWriter out = new BufferedWriter(fstream);
			for(ActivityRecord record : repos.values()){
				String json = gson.toJson(record);
				out.write(json + "\n");
			}
			out.close();
		}catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}
	}


	private void saveAsCSV() {

		try{
			FileWriter fstream = new FileWriter(DATASET_PATH+"activity.csv");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("Repository URL, february, march\n");
			for(ActivityRecord record : repos.values()){
				String line = record.repository + ", " + 
								record.february + ", " + 
								record.march + "\n";
				out.write(line);
			}
			out.close();
		}catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}
	}

}
