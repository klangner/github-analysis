package com.matrobot.gha.app.repo;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.matrobot.gha.app.Settings;
import com.matrobot.gha.category.ActivityRating;
import com.matrobot.gha.dataset.ActivityDataset;
import com.matrobot.gha.dataset.ActivityRecord;

public class StatsActivityApp {

	private HashMap<String, ActivityRecord> prevDataset;
	private HashMap<String, ActivityRecord> currentDataset;
	private HashMap<String, ActivityRecord> nextDataset;
	private int[][] activity2category = new int[10][6];
	private int[][] category2category = new int[10][6];
	
	
	protected StatsActivityApp(String firstPath, String secondPath, String thirdPath) throws IOException{
		
		ActivityDataset datasetReader = new ActivityDataset();
		prevDataset = datasetReader.loadData(firstPath);
		currentDataset = datasetReader.loadData(secondPath);
		nextDataset = datasetReader.loadData(thirdPath);
	}
	
	private void printStats(int minActivity) {
		
		DescriptiveStatistics stats = new DescriptiveStatistics();
		int before = 0;
		int after = 0;
		double normalized = 0;
		int counter = 0;
		initCategoryProbabilities();
		for(ActivityRecord record : currentDataset.values()){
			ActivityRecord nextRecord = nextDataset.get(record.repository); 
			double currentActivity = record.activity;
			double nextActivity = (nextRecord != null) ? nextRecord.activity : 0;
			if(currentActivity > minActivity){
				double diff = (nextActivity-currentActivity)/currentActivity;
				stats.addValue(diff);
				updateCategoryProbability(record, nextActivity);
				before += currentActivity;
				after += nextActivity;
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
		
		printCategoryProbabilities();
	}

	private void initCategoryProbabilities() {
		
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 6; j++){
				activity2category[i][j] = 0;
				category2category[i][j] = 0;
			}
		}
	}

	private void updateCategoryProbability(ActivityRecord current, double nextActivity) {
		
		int category = ActivityRating.estimateCategory(current.activity, nextActivity);
		int activity = (int) Math.log(current.activity);
		activity2category[activity][category] += 1;
		
		int oldCategory = getOldActivityRating(current);
		
		category2category[oldCategory][category] += 1;
	}

	private int getOldActivityRating(ActivityRecord current) {
		int oldCategory;
		ActivityRecord prevRecord = prevDataset.get(current.repository);
		if(prevRecord == null){
			oldCategory = ActivityRating.UNKNOWN;
		}
		else{
			oldCategory = ActivityRating.estimateCategory(
					prevRecord.activity, current.activity);
		}
		return oldCategory;
	}

	
	private void printCategoryProbabilities() {

//		for(int i = 1; i < 7; i++){
//			for(int j = 0; j < 6; j++){
//				System.out.println("Activity: " + i + " category: " + j + " count: " + activity2category[i][j]);
//			}
//		}
		
		for(int i = 0; i < 6; i++){
			for(int j = 0; j < 6; j++){
				System.out.println("From: " + i + " to: " + j + 
						" count: " + category2category[i][j]);
			}
		}
		
	}

	public static void main(String[] args) throws IOException {

		long time = System.currentTimeMillis();
		StatsActivityApp app = new StatsActivityApp(
				Settings.DATASET_PATH+"2012/1/", 
				Settings.DATASET_PATH+"2012/2/",
				Settings.DATASET_PATH+"2012/3/");
		app.printStats(Settings.MIN_ACTIVITY);

		time = (System.currentTimeMillis()-time)/1000;
		System.out.println("Time: " + time + "sec.");
		
	}
	
}
