package com.matrobot.gha.app;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matrobot.gha.dataset.ActivityRecord;

public class AnalizeActivityApp {

	private static final String DATASET_PATH = "/home/klangner/datasets/github/";
	private HashMap<String, ActivityRecord> firstDataset;
	private HashMap<String, ActivityRecord> secondDataset;
	
	
	public static void main(String[] args) throws IOException {

		long time = System.currentTimeMillis();
		AnalizeActivityApp app = new AnalizeActivityApp(DATASET_PATH+"2012/2/", DATASET_PATH+"2012/3/");
		app.printStats(10);
		app.printStats(100);

		time = (System.currentTimeMillis()-time)/1000;
		System.out.println("Time: " + time + "sec.");
		
	}
	
	protected AnalizeActivityApp(String firstPath, String secondPath) throws IOException{
		
		firstDataset = loadData(firstPath);
		secondDataset = loadData(secondPath);
	}
	
	/**
	 * Load json dataset created by ParseActivityApp
	 * 
	 * @param filename
	 * @throws IOException
	 */
	private HashMap<String, ActivityRecord> loadData(String filePath) throws IOException{
		
		List<ActivityRecord> rows = new ArrayList<ActivityRecord>();
		Gson gson = new Gson();
		Type datasetType = new TypeToken<Collection<ActivityRecord>>(){}.getType();

		FileInputStream fis = new FileInputStream(filePath+"activity.json");
		Reader reader = new InputStreamReader(fis, "UTF-8");
		rows = gson.fromJson(reader, datasetType);
		
		reader.close();

		HashMap<String, ActivityRecord> dataset = new HashMap<String, ActivityRecord>();
		for(ActivityRecord row : rows){
			dataset.put(row.repository, row);
		}
		
		return dataset;
	}

	
	private void printStats(int minActivity) {
		
		DescriptiveStatistics stats = new DescriptiveStatistics();
		for(ActivityRecord record : firstDataset.values()){
			ActivityRecord nextRecord = secondDataset.get(record.repository); 
			if(record.activity > minActivity && nextRecord != null){
				double diff = (nextRecord.activity-record.activity)/record.activity;
				stats.addValue(diff);
			}
		}

		// Compute some statistics
		int count = (int) stats.getN();
		double mean = Math.floor(stats.getMean()*1000)/1000;
		double std = Math.floor(stats.getStandardDeviation()*100)/1000;
		
		System.out.println("Mean: " + mean + " SD: " + std + " records: " + count);
	}

}
