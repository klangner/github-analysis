package com.matrobot.gha.insights.app.repo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import com.matrobot.gha.archive.repo.RepositoryRecord;

/**
 * Check repository activity for 12 months.
 * 1. Select repositories created on month 1
 * 2. For each month add project to the count if there is any activity
 * 3. Show number of active repositories for each month 
 * 4. Show how many repositories stay active after full year
 */
public class LongTermRepositoryApp {

	Properties prop = new Properties();
	private List<String> createdRepositories = new ArrayList<String>();
	private List<Integer> activeProjectCounts = new ArrayList<Integer>();

	
	public LongTermRepositoryApp(String firstMonthPath) throws IOException{
		
		prop.load(new FileInputStream("config.properties"));
		initRepositories(firstMonthPath);
	}
	
	
	private void initRepositories(String filePath) throws IOException {

		HashMap<String, RepositoryRecord> dataset = RepositoryRecord.loadData(filePath);
		
		for(RepositoryRecord repository : dataset.values()){
			createdRepositories.add(repository.repository);
		}
		
		activeProjectCounts.add(createdRepositories.size());
	}


	public void addMonth(String filePath) throws IOException {

		HashMap<String, RepositoryRecord> dataset = RepositoryRecord.loadData(prop.getProperty("data_path") + filePath);
		List<String> nextRepositories = new ArrayList<String>();
		
		int activeRepositoryCount = 0;
		for(String name : createdRepositories){
			RepositoryRecord record = dataset.get(name);
			if(record != null && record.pushEventCount > 0){
				activeRepositoryCount += 1;
				nextRepositories.add(name);
			}
		}
		
		activeProjectCounts.add(activeRepositoryCount);
		createdRepositories = nextRepositories;
	}


	public void printMonthlyActivity() {
		
		for(int month = 0; month < activeProjectCounts.size(); month ++){
			
			System.out.println("Month: " + month + " active: " + activeProjectCounts.get(month));
		}
	}


	public void saveAsCSV() {
		
		try{
			String filename = "top_projects.csv";
			FileOutputStream fos = new FileOutputStream(prop.getProperty("data_path") + filename, false);
			Writer writer = new OutputStreamWriter(fos, "UTF-8");
			writer.write("name\n");
			for(int i = 0; i < 100; i ++){
			
				int index = (int) (Math.random()*createdRepositories.size());
				String name = createdRepositories.remove(index);
				writer.write(name + "\n");
			}
			writer.close();
			
		}catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}
	}

	
	public static void main(String[] args) throws IOException {

		long time = System.currentTimeMillis();
		LongTermRepositoryApp app = new LongTermRepositoryApp("2011-10/");
		
		app.addMonth("2011-11/"); 
		app.addMonth("2011-12/");
		app.addMonth("2012-1/");
		app.addMonth("2012-2/");
		app.addMonth("2012-3/");
		app.addMonth("2012-4/");
		app.addMonth("2012-5/");
		app.addMonth("2012-6/");
		app.addMonth("2012-7/");
		app.addMonth("2012-8/");
		app.addMonth("2012-9/");
		app.addMonth("2012-10/");
		app.addMonth("2012-10/");
		
		System.out.println("Create report");
		app.printMonthlyActivity();
		app.saveAsCSV();
		
		time = (System.currentTimeMillis()-time)/1000;
		System.out.println("Time: " + time + "sec.");
		
	}
	
}
