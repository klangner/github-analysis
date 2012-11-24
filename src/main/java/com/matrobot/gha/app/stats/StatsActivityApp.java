package com.matrobot.gha.app.stats;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.matrobot.gha.app.Settings;
import com.matrobot.gha.dataset.ActivityDataset;
import com.matrobot.gha.dataset.ActivityRecord;

public class StatsActivityApp {

	private HashMap<String, ActivityRecord> firstDataset;
	private HashMap<String, ActivityRecord> secondDataset;
	
	
	protected StatsActivityApp(String firstPath, String secondPath) throws IOException{
		
		ActivityDataset datasetReader = new ActivityDataset();
		firstDataset = datasetReader.loadData(firstPath);
		secondDataset = datasetReader.loadData(secondPath);
	}
	
	private void printStats(int minActivity) {
		
		DescriptiveStatistics stats = new DescriptiveStatistics();
		int before = 0;
		int after = 0;
		double normalized = 0;
		int counter = 0;
		for(ActivityRecord record : firstDataset.values()){
			ActivityRecord nextRecord = secondDataset.get(record.repository); 
			if(record.activity > minActivity && nextRecord != null){
				double diff = (nextRecord.activity-record.activity)/(double)record.activity;
				stats.addValue(diff);
				before += record.activity;
				after += nextRecord.activity;
				normalized += diff;
				counter += 1;
			}
		}

		// Compute some statistics
		int count = (int) stats.getN();
		double mean = Math.floor(stats.getMean()*1000)/10;
		double std = Math.floor(stats.getStandardDeviation()*100)/10;
		
		System.out.println("Before: " + (before/counter) + " After: " + (after/counter) + 
				" normalized: " + (normalized/counter));
		System.out.println("Mean: " + mean + "% SD: " + std + "% repositories: " + count);
	}

	
	public static void main(String[] args) throws IOException {

		long time = System.currentTimeMillis();
		StatsActivityApp app = new StatsActivityApp(
				Settings.DATASET_PATH+"2012/2/", Settings.DATASET_PATH+"2012/3/");
		app.printStats(Settings.MIN_ACTIVITY);

		time = (System.currentTimeMillis()-time)/1000;
		System.out.println("Time: " + time + "sec.");
		
	}
	
}
