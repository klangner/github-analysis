package com.matrobot.gha.cmd;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matrobot.gha.dataset.ActivityRecord;

public class AnalizeActivityApp {

	private static final String DATASET_PATH = "/home/klangner/datasets/github/";
	private List<ActivityRecord> dataset = new ArrayList<ActivityRecord>();
	
	
	public static void main(String[] args) throws IOException {

		long time = System.currentTimeMillis();
		AnalizeActivityApp app = new AnalizeActivityApp();
		app.loadData(DATASET_PATH+"2012/3/");
		app.printStats(0);
		app.printStats(100);

		time = (System.currentTimeMillis()-time)/1000;
		System.out.println("Time: " + time + "sec.");
		
	}
	
	/**
	 * Load json dataset created by ParseActivityApp
	 * 
	 * @param filename
	 * @throws IOException
	 */
	protected void loadData(String filePath) throws IOException{
		
		Gson gson = new Gson();
		Type datasetType = new TypeToken<Collection<ActivityRecord>>(){}.getType();

		FileInputStream fis = new FileInputStream(filePath+"activity_changes.json");
		Reader reader = new InputStreamReader(fis, "UTF-8");
		dataset = gson.fromJson(reader, datasetType);
		
		reader.close();

	}

	
	private void printStats(int minActivity) {
		
		DescriptiveStatistics stats = new DescriptiveStatistics();
		for(ActivityRecord record : dataset){
			if(record.currentMonth > minActivity){
				double diff = (record.nextMonth-record.currentMonth)/record.currentMonth;
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
