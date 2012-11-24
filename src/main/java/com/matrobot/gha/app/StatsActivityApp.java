package com.matrobot.gha.app;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.matrobot.gha.dataset.ActivityDataset;
import com.matrobot.gha.dataset.ActivityRecord;

public class StatsActivityApp {

	private HashMap<String, ActivityRecord> firstDataset;
	private HashMap<String, ActivityRecord> secondDataset;
	
	
	public static void main(String[] args) throws IOException {

		long time = System.currentTimeMillis();
		StatsActivityApp app = new StatsActivityApp(
				Settings.DATASET_PATH+"2012/9/", Settings.DATASET_PATH+"2012/10/");
		app.printStats(Settings.MIN_ACTIVITY);

		time = (System.currentTimeMillis()-time)/1000;
		System.out.println("Time: " + time + "sec.");
		
	}
	
	protected StatsActivityApp(String firstPath, String secondPath) throws IOException{
		
		ActivityDataset datasetReader = new ActivityDataset();
		firstDataset = datasetReader.loadData(firstPath);
		secondDataset = datasetReader.loadData(secondPath);
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
		double mean = Math.floor(stats.getMean()*1000)/10;
		double std = Math.floor(stats.getStandardDeviation()*100)/10;
		
		System.out.println("Mean: " + mean + "% SD: " + std + "% records: " + count);
	}

}
