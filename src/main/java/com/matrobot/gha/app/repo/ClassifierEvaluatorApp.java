package com.matrobot.gha.app.repo;

import java.io.IOException;
import java.util.HashMap;

import com.matrobot.gha.app.Settings;
import com.matrobot.gha.category.ActivityRating;
import com.matrobot.gha.classifier.IClassifier;
import com.matrobot.gha.classifier.ManualRepoClassifier;
import com.matrobot.gha.classifier.StaticClassifier;
import com.matrobot.gha.dataset.RepositoryRecord;

public class ClassifierEvaluatorApp {

	private HashMap<String, RepositoryRecord> prevDataset;
	private HashMap<String, RepositoryRecord> currentDataset;
	private HashMap<String, RepositoryRecord> nextDataset;
	private int errorCount;
	private int counter;
	
	
	protected ClassifierEvaluatorApp(String firstPath, String secondPath, String thirdPath) throws IOException{
		
		prevDataset = RepositoryRecord.loadData(firstPath);
		currentDataset = RepositoryRecord.loadData(secondPath);
		nextDataset = RepositoryRecord.loadData(thirdPath);
	}
	
	private double evaluate(IClassifier classifier, int minActivity) {

		counter = 0;
		errorCount = 0;
		double sum = 0;
		for(RepositoryRecord record : currentDataset.values()){
			RepositoryRecord nextRecord = nextDataset.get(record.repository); 
			double currentActivity = record.activity;
			double nextActivity = (nextRecord != null) ? nextRecord.activity : 0;
			if(record.activity > minActivity){
				
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
		featureVector[0] = (int) Math.log10(currentRecord.activity);
		RepositoryRecord prevRecord = prevDataset.get(currentRecord.repository);
		if(prevRecord == null){
			featureVector[1] = ActivityRating.UNKNOWN;
		}
		else{
			featureVector[1] = ActivityRating.estimateCategory(
					prevRecord.activity, currentRecord.activity);
		}
		return featureVector;
	}

	public static void main(String[] args) throws IOException {

		ClassifierEvaluatorApp app = new ClassifierEvaluatorApp(
				Settings.DATASET_PATH+"2012/1/", 
				Settings.DATASET_PATH+"2012/2/",
				Settings.DATASET_PATH+"2012/3/");
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
