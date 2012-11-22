package com.matrobot.gha.cmd;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

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
		int feb = app.scanMonth(2);
		int march = app.scanMonth(3);
		
		app.saveAsJson();
		app.saveAsCSV();

		time = (System.currentTimeMillis()-time)/1000;
		System.out.println("Feb: " + feb + " Mar: " + march + " in: " + time + "sec.");
		
		app.printStats();
	}
	
	private int scanMonth(int month) throws IOException{
		
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
				
				if(month == 2){
					data.february += 1;
				}
				else if(month == 3){
					data.march += 1;
				}
				repos.put(url, data);
			}
			count ++;
		}
		
		return count;
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

	private void printStats() {
		
		DescriptiveStatistics stats = new DescriptiveStatistics();
		for(ActivityRecord record : repos.values()){
			if(record.february > 0){
				double diff = (record.march-record.february)/record.february;
				stats.addValue(diff);
			}
		}

		// Compute some statistics
		double mean = stats.getMean();
		double std = stats.getStandardDeviation();
		
		System.out.println("Mean: " + mean + " SD: " + std);
	}

}
