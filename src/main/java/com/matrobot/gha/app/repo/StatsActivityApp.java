package com.matrobot.gha.app.repo;

import java.io.IOException;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.matrobot.gha.app.Settings;
import com.matrobot.gha.category.ActivityRating;
import com.matrobot.gha.dataset.repo.RepositoryDatasetList;
import com.matrobot.gha.dataset.repo.RepositoryRecord;

public class StatsActivityApp {

	private RepositoryDatasetList datasets = new RepositoryDatasetList();
	private int[][] activity2category = new int[20][6];
	private int[][] category2category = new int[10][6];
	
	
	protected StatsActivityApp(String firstPath, String secondPath, String thirdPath) throws IOException{
		
		datasets.addFromFile(firstPath);
		datasets.addFromFile(secondPath);
		datasets.addFromFile(thirdPath);
	}
	
	private void printStats(int minActivity) {
		
		DescriptiveStatistics stats = new DescriptiveStatistics();
		int before = 0;
		int after = 0;
		double normalized = 0;
		int counter = 0;
		initCategoryProbabilities();
		for(RepositoryRecord record : datasets.getDataset(1).values()){
			RepositoryRecord nextRecord = datasets.findRepository(2, record.repository); 
			double currentActivity = record.eventCount;
			double nextActivity = nextRecord.eventCount;
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
		
		RepositoryRecord prevRecord = datasets.findRepository(0, current.repository);
		return ActivityRating.estimateCategory(prevRecord.eventCount, current.eventCount);
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
				Settings.DATASET_PATH+"2011-10/", 
				Settings.DATASET_PATH+"2011-11/",
				Settings.DATASET_PATH+"2011-12/");
		app.printStats(Settings.MIN_ACTIVITY);

		time = (System.currentTimeMillis()-time)/1000;
		System.out.println("Time: " + time + "sec.");
		
	}
	
}
