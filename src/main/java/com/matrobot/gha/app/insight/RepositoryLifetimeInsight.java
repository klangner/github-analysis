package com.matrobot.gha.app.insight;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.matrobot.gha.app.Settings;
import com.matrobot.gha.dataset.repo.RepositoryDatasetList;
import com.matrobot.gha.dataset.repo.RepositoryRecord;

/**
 * Check repository activity for 12 months.
 * 1. Select repositories created on month 1
 * 2. For each month add project to the count if there is any activity
 * 3. Show number of active repositories for each month 
 * 4. Show how many repositories stay active after full year
 */
public class RepositoryLifetimeInsight {

	private RepositoryDatasetList datasets = new RepositoryDatasetList();
	
	
	protected void printMonthlyActivity() {
		
		List<String> createdRepositories = new ArrayList<String>();
		for(RepositoryRecord repository : datasets.getDataset(0).values()){
			if(repository.isNew){
				createdRepositories.add(repository.repository);
			}
		}
		
		System.out.println("Found: " + createdRepositories.size() + " new repositories.");
		
		for(int i = 1; i < datasets.size(); i++){
			
			int activeRepositoryCount = 0;
			for(String name : createdRepositories){
				RepositoryRecord record = datasets.findRepository(i, name);
				if(record.pushEventCount > 0){
					activeRepositoryCount += 1;
				}
			}
			
			System.out.println("Month: " + i + " active: " + activeRepositoryCount);
		}
	}


	protected void randomCheck( int projectCount) {
		
		List<RepositoryRecord> createdRepositories = new ArrayList<RepositoryRecord>();
		for(RepositoryRecord repository : datasets.getDataset(0).values()){
			if(repository.isNew){
				createdRepositories.add(repository);
			}
		}

		Random random = new Random();
		
		for(int i = 0; i < projectCount; i++){
			
			String name = createdRepositories.get(random.nextInt(createdRepositories.size())).repository;
			int lastActivity = 0;
			for(int j = datasets.size()-1; j > 0; j--){
				RepositoryRecord record = datasets.findRepository(j, name);
				if(record.pushEventCount > 0){
					lastActivity = j;
					break;
				}
			}
			
			System.out.println("Repository: " + name + " last activity month: " + lastActivity);
		}

		
		for(RepositoryRecord record : createdRepositories){
			
			RepositoryRecord lastRecord = datasets.findRepository(datasets.size()-1, record.repository);
			if(lastRecord.pushEventCount > 0){
				System.out.println("Still active: " + record.repository);
			}
		}
		
	}

	
	public static void main(String[] args) throws IOException {

		long time = System.currentTimeMillis();
		RepositoryLifetimeInsight app = new RepositoryLifetimeInsight();
		app.datasets.addFromFile(Settings.DATASET_PATH+"2011-10/"); 
		app.datasets.addFromFile(Settings.DATASET_PATH+"2011-11/"); 
		app.datasets.addFromFile(Settings.DATASET_PATH+"2011-12/");
		app.datasets.addFromFile(Settings.DATASET_PATH+"2012-1/");
		app.datasets.addFromFile(Settings.DATASET_PATH+"2012-2/");
		app.datasets.addFromFile(Settings.DATASET_PATH+"2012-3/");
		app.datasets.addFromFile(Settings.DATASET_PATH+"2012-4/");
		app.datasets.addFromFile(Settings.DATASET_PATH+"2012-5/");
		app.datasets.addFromFile(Settings.DATASET_PATH+"2012-6/");
		app.datasets.addFromFile(Settings.DATASET_PATH+"2012-7/");
		app.datasets.addFromFile(Settings.DATASET_PATH+"2012-8/");
		app.datasets.addFromFile(Settings.DATASET_PATH+"2012-9/");
		app.datasets.addFromFile(Settings.DATASET_PATH+"2012-10/");
		
//		app.printMonthlyActivity();
		app.randomCheck(5);
		
		time = (System.currentTimeMillis()-time)/1000;
		System.out.println("Time: " + time + "sec.");
		
	}
	
}
