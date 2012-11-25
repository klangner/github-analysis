package com.matrobot.gha.app.insight;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.matrobot.gha.app.Settings;
import com.matrobot.gha.category.ActivityRating;
import com.matrobot.gha.dataset.RepositoryRecord;

/**
 * Check repository activity throw 12 month.
 * 1. Select repositories created on month 1
 * 2. For each month add project to the count if there is any activity
 * 3. Show number of active repositories for each month 
 * 4. Show how many repositories stay active after full year
 */
public class RepositoryLifetimeInsight {

	private HashMap<String, RepositoryRecord> prevDataset;
	private HashMap<String, RepositoryRecord> currentDataset;
	private HashMap<String, RepositoryRecord> nextDataset;
	private int[][] activity2category = new int[10][6];
	private int[][] category2category = new int[10][6];
	
	
	protected RepositoryLifetimeInsight(String firstPath, String secondPath, String thirdPath) throws IOException{
		
		prevDataset = RepositoryRecord.loadData(firstPath);
		currentDataset = RepositoryRecord.loadData(secondPath);
		nextDataset = RepositoryRecord.loadData(thirdPath);
	}
	
	private void printStats(int minActivity) {
		
		DescriptiveStatistics stats = new DescriptiveStatistics();
		int before = 0;
		int after = 0;
		double normalized = 0;
		int counter = 0;
		initCategoryProbabilities();
		for(RepositoryRecord record : currentDataset.values()){
			RepositoryRecord nextRecord = nextDataset.get(record.repository); 
			double currentActivity = record.eventCount;
			double nextActivity = (nextRecord != null) ? nextRecord.eventCount : 0;
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

	private void updateCategoryProbability(RepositoryRecord current, double nextActivity) {
		
		int category = ActivityRating.estimateCategory(current.eventCount, nextActivity);
		int activity = (int) Math.log(current.eventCount);
		activity2category[activity][category] += 1;
		
		int oldCategory = getOldActivityRating(current);
		
		category2category[oldCategory][category] += 1;
	}

	private int getOldActivityRating(RepositoryRecord current) {
		int oldCategory;
		RepositoryRecord prevRecord = prevDataset.get(current.repository);
		if(prevRecord == null){
			oldCategory = ActivityRating.UNKNOWN;
		}
		else{
			oldCategory = ActivityRating.estimateCategory(
					prevRecord.eventCount, current.eventCount);
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
		RepositoryLifetimeInsight app = new RepositoryLifetimeInsight(
				Settings.DATASET_PATH+"2012/1/", 
				Settings.DATASET_PATH+"2012/2/",
				Settings.DATASET_PATH+"2012/3/");
		app.printStats(Settings.MIN_ACTIVITY);

		time = (System.currentTimeMillis()-time)/1000;
		System.out.println("Time: " + time + "sec.");
		
	}
	
}
