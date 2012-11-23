package com.matrobot.gha.cmd;

import java.io.BufferedWriter;
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

	private static final String DATASET_PATH = "/home/klangner/datasets/github/2012/";
	HashMap<String, ActivityRecord> repos = new HashMap<String, ActivityRecord>();
	
	
	public static void main(String[] args) throws IOException {

		long time = System.currentTimeMillis();
		ParseActivityApp app = new ParseActivityApp();
		int feb = app.scanMonth(2, true);
		int march = app.scanMonth(3, false);
		
		app.saveAsJson();
//		app.saveAsCSV();

		time = (System.currentTimeMillis()-time)/1000;
		System.out.println("Feb: " + feb + " Mar: " + march + " in: " + time + "sec.");
	}
	
	private int scanMonth(int month, boolean isFirst) throws IOException{
		
		IDatasetReader datasetReader = new FolderDatasetReader(DATASET_PATH+month);
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

	private void saveAsJson() {

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		try{
			FileWriter writer = new FileWriter(DATASET_PATH+"activity.json");
			String json = gson.toJson(repos.values());
			writer.write(json);
			writer.close();
		}catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}
	}


	protected void saveAsCSV() {

		try{
			FileWriter fstream = new FileWriter(DATASET_PATH+"activity.csv");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("Repository URL, february, march\n");
			for(ActivityRecord record : repos.values()){
				String line = record.repository + ", " + 
								record.currentMonth + ", " + 
								record.nextMonth + "\n";
				out.write(line);
			}
			out.close();
		}catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}
	}

}
