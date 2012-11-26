package com.matrobot.gha.app.repo;

import java.io.IOException;

import com.matrobot.gha.app.Settings;
import com.matrobot.gha.category.ActivityRating;
import com.matrobot.gha.classifier.IClassifier;
import com.matrobot.gha.classifier.ManualRepoClassifier;
import com.matrobot.gha.classifier.StaticClassifier;
import com.matrobot.gha.dataset.repo.RepositoryDatasetList;
import com.matrobot.gha.dataset.repo.RepositoryRecord;

public class ClassifierEvaluatorApp {

	private RepositoryDatasetList datasets = new RepositoryDatasetList();
	private int errorCount;
	private int counter;
	
	
	protected ClassifierEvaluatorApp(String firstPath, String secondPath, String thirdPath) throws IOException{
		
		datasets.addFromFile(firstPath);
		datasets.addFromFile(secondPath);
		datasets.addFromFile(thirdPath);
	}
	
	private double evaluate(IClassifier classifier, int minActivity) {

		counter = 0;
		errorCount = 0;
		double sum = 0;
		for(RepositoryRecord record : datasets.getDataset(1).values()){
			RepositoryRecord nextRecord = datasets.findRepository(2, record.repository); 
			double currentActivity = record.eventCount;
			double nextActivity = nextRecord.eventCount;
			if(record.eventCount > minActivity){
				
				int expected = getExpectedCategory(currentActivity, nextActivity);
				int[] featureVector = createFeatureVector(record);
				int classifiedCategory = classifier.classify(featureVector);
				
				double error = Math.pow(expected-classifiedCategory, 2); 
				sum += error;
				if(error > 0){
					errorCount++;
				}
				counter += 1;
			}
		}

		return Math.sqrt(sum/counter);
	}

	
	/**
	 * Find expected category
	 */
	private int getExpectedCategory(double currentActivity, double nextActivity) {
		
		return ActivityRating.estimateCategory(currentActivity, nextActivity);
	}

	
	/**
	 * Feature vector:
	 *  feature[0] = currentActivity in log10 scale
	 *  feature[1] = current activity rating (from previous month)
	 */
	private int[] createFeatureVector(RepositoryRecord currentRecord) {
		
		int[] featureVector = new int[2];
		featureVector[0] = (int) Math.log10(currentRecord.eventCount);
		RepositoryRecord prevRecord = datasets.findRepository(0, currentRecord.repository);
		featureVector[1] = ActivityRating.estimateCategory(
				prevRecord.eventCount, currentRecord.eventCount);
		return featureVector;
	}

	public static void main(String[] args) throws IOException {

		ClassifierEvaluatorApp app = new ClassifierEvaluatorApp(
				Settings.DATASET_PATH+"2012-1/", 
				Settings.DATASET_PATH+"2012-2/",
				Settings.DATASET_PATH+"2012-3/");
		double score;
		int correctPercentage;

		// Static classifier
		score = app.evaluate(new StaticClassifier(2), Settings.MIN_ACTIVITY);
		correctPercentage = 100-(int)((app.errorCount*100.0)/app.counter);
		System.out.println("Static2: ");
		System.out.println("  Error: " + score);
		System.out.println("  Correct: " + correctPercentage + "%");
		System.out.println();
		
		score = app.evaluate(new ManualRepoClassifier(), Settings.MIN_ACTIVITY);
		correctPercentage = 100-(int)((app.errorCount*100.0)/app.counter);
		System.out.println("Manual:");
		System.out.println("  Error: " + score);
		System.out.println("  Correct: " + correctPercentage + "%");
		System.out.println();
		
	}
	
}
