package com.matrobot.gha.app.user;

import java.io.IOException;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.matrobot.gha.app.Settings;
import com.matrobot.gha.archive.user.UserArchiveList;
import com.matrobot.gha.archive.user.UserRecord;

public class StatsApp {

	private UserArchiveList datasets = new UserArchiveList();
	
	
	protected StatsApp(String firstPath, String secondPath, String thirdPath) throws IOException{
		
		datasets.addFromFile(firstPath);
		datasets.addFromFile(secondPath);
		datasets.addFromFile(thirdPath);
	}
	
	private void printStats() {
		
		DescriptiveStatistics stats = new DescriptiveStatistics();
		int currentMonthCount = 0;
		int nextMonthCount = 0;
		int counter = 0;
		double sum = 0;
		for(UserRecord record : datasets.getDataset(1).values()){
			UserRecord nextRecord = datasets.findRepository(2, record.name); 
			double currentActivity = record.pushEventCount;
			double nextActivity = nextRecord.pushEventCount;
			if(currentActivity > 0){
				double diff = (nextActivity-currentActivity)/currentActivity;
				stats.addValue(diff);
				currentMonthCount += currentActivity;
				nextMonthCount += nextActivity;
				counter += 1;
				
				sum += nextActivity-currentActivity;
			}
		}
	
		// Compute some statistics
		int count = (int) stats.getN();
		double mean = Math.floor(stats.getMean()*1000)/10;
		double std = Math.floor(stats.getStandardDeviation()*100)/10;
		
		System.out.println("Current month: " + (currentMonthCount/counter) + 
				" Next: " + (nextMonthCount/counter) + " sum: " + sum);
		System.out.println("Mean: " + mean + "% SD: " + std + "% users: " + count);
	}

	public static void main(String[] args) throws IOException {

		long time = System.currentTimeMillis();
		StatsApp app = new StatsApp(
				Settings.DATASET_PATH+"2011-10/", 
				Settings.DATASET_PATH+"2011-11/",
				Settings.DATASET_PATH+"2011-12/");
		app.printStats();

		time = (System.currentTimeMillis()-time)/1000;
		System.out.println("Time: " + time + "sec.");
		
	}
	
}
