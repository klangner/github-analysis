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

	private static final String DATASET_PATH = "/home/klangner/datasets/github/2012/activity.json";
	private List<ActivityRecord> dataset = new ArrayList<ActivityRecord>();
	
	
	public static void main(String[] args) throws IOException {

		long time = System.currentTimeMillis();
		AnalizeActivityApp app = new AnalizeActivityApp();
		app.loadData(DATASET_PATH);
		app.printStats();

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

		FileInputStream fis = new FileInputStream(filePath);
		Reader reader = new InputStreamReader(fis, "UTF-8");
		dataset = gson.fromJson(reader, datasetType);
		
		reader.close();

	}

	
	private void printStats() {
		
		DescriptiveStatistics stats = new DescriptiveStatistics();
		for(ActivityRecord record : dataset){
			if(record.currentMonth > 0){
				double diff = (record.nextMonth-record.currentMonth)/record.currentMonth;
				stats.addValue(diff);
			}
		}

		// Compute some statistics
		double mean = stats.getMean();
		double std = stats.getStandardDeviation();
		
		System.out.println("Mean: " + mean + " SD: " + std);
	}

}
