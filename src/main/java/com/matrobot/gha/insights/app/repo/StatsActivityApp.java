package com.matrobot.gha.insights.app.repo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.matrobot.gha.archive.repo.RepositoryRecord;

public class StatsActivityApp {

	Properties prop = new Properties();
	private HashMap<String, RepositoryRecord> dataset1;
	private HashMap<String, RepositoryRecord> dataset2;
	
	
	public StatsActivityApp(String firstPath) throws IOException{
		
		prop.load(new FileInputStream("config.properties"));
		dataset1 = RepositoryRecord.loadData(prop.getProperty("data_path") + firstPath);
	}

	public void setNextDataset(String path) throws IOException {
		
		if(dataset2 != null){
			dataset1 = dataset2;
		}
		
		dataset2 = RepositoryRecord.loadData(prop.getProperty("data_path") + path);
	}

	private void printStats(int minActivity) {
		
		DescriptiveStatistics stats = new DescriptiveStatistics();
		int activity = 0;
		for(RepositoryRecord record : dataset1.values()){
			RepositoryRecord nextRecord = dataset2.get(record.repoName); 
			double currentActivity = record.eventCount;
			double nextActivity = (nextRecord!=null)? nextRecord.eventCount: 0;
			if(currentActivity > minActivity){
				double diff = (nextActivity-currentActivity)/currentActivity;
				stats.addValue(diff);
				activity += currentActivity;
			}
		}
	
		// Compute some statistics
		int count = (int) stats.getN();
		double mean = Math.floor(stats.getMean()*1000)/10;
		double std = Math.floor(stats.getStandardDeviation()*100)/10;
		
		System.out.println("Activity: " + activity + " repositories: " + count);
		System.out.println("Mean: " + mean + "% SD: " + std + "%");
		System.out.println();
	}

	public static void main(String[] args) throws IOException {

//		StatsActivityApp app = new StatsActivityApp("2012-9/");
//		app.setNextDataset("2012-10/");
//		app.printStats(5);
		
		statsForAll();
		
	}

	public static void statsForAll() throws IOException {
		long time = System.currentTimeMillis();
		StatsActivityApp app = new StatsActivityApp("2011-9/");
		
		System.out.println("2011-10");
		app.setNextDataset("2011-10/");
		app.printStats(5);

		System.out.println("2011-11");
		app.setNextDataset("2011-11/");
		app.printStats(5);

		System.out.println("2011-12");
		app.setNextDataset("2011-12/");
		app.printStats(5);

		System.out.println("2012-1");
		app.setNextDataset("2012-1/");
		app.printStats(5);

		System.out.println("2012-2");
		app.setNextDataset("2012-2/");
		app.printStats(5);

		System.out.println("2012-3");
		app.setNextDataset("2012-3/");
		app.printStats(5);

		System.out.println("2012-4");
		app.setNextDataset("2012-4/");
		app.printStats(5);

		System.out.println("2012-5");
		app.setNextDataset("2012-5/");
		app.printStats(5);

		System.out.println("2012-6");
		app.setNextDataset("2012-6/");
		app.printStats(5);

		System.out.println("2012-7");
		app.setNextDataset("2012-7/");
		app.printStats(5);

		System.out.println("2012-8");
		app.setNextDataset("2012-8/");
		app.printStats(5);

		System.out.println("2012-9");
		app.setNextDataset("2012-9/");
		app.printStats(5);

		System.out.println("2012-10");
		app.setNextDataset("2012-10/");
		app.printStats(5);

		time = (System.currentTimeMillis()-time)/1000;
		System.out.println("Time: " + time + "sec.");
	}
	
}
