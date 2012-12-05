package com.matrobot.gha.app.repo;

import java.io.IOException;

import com.matrobot.gha.app.Settings;
import com.matrobot.gha.classifier.BinaryStaticClassifier;
import com.matrobot.gha.classifier.IBinaryClassifier;
import com.matrobot.gha.dataset.repo.RepositoryDatasetList;
import com.matrobot.gha.dataset.repo.RepositoryRecord;
import com.matrobot.gha.features.RepositoryFeatures;

public class ClassifierEvaluatorApp {

	private static final int MIN_ACTIVITY = 0;
	private RepositoryDatasetList datasets = new RepositoryDatasetList();
	private int errorCount;
	private int counter;
	
	
	protected ClassifierEvaluatorApp(String firstPath, String secondPath, String thirdPath) throws IOException{
		
		datasets.addFromFile(firstPath);
		datasets.addFromFile(secondPath);
		datasets.addFromFile(thirdPath);
	}
	
	private double evaluate(IBinaryClassifier classifier, int minActivity) {

		counter = 0;
		errorCount = 0;
		double sum = 0;
		for(RepositoryRecord record : datasets.getDataset(1).values()){
			RepositoryRecord nextRecord = datasets.findRepository(2, record.repository); 
			if(record.eventCount > MIN_ACTIVITY){
				
				double expected = RepositoryFeatures.getExpectedValue(record, nextRecord);
				double[] features = RepositoryFeatures.getFeatures(record);
				double confidence = classifier.classify(features);
				
				double error = Math.pow(expected-confidence, 2); 
				sum += error;
				if(error > 0.25){
					errorCount++;
				}
				counter += 1;
			}
		}

		return Math.sqrt(sum/counter);
	}

	
	/**
	 * Feature vector:
	 *  feature[0] = currentActivity in log10 scale
	 *  feature[1] = current activity rating (from previous month)
	 */
	public static void main(String[] args) throws IOException {

		ClassifierEvaluatorApp app = new ClassifierEvaluatorApp(
				Settings.DATASET_PATH+"2012-1/", 
				Settings.DATASET_PATH+"2012-10/",
				Settings.DATASET_PATH+"2012-11/");
		double score;
		int correctPercentage;

		// Static classifier
		score = app.evaluate(new BinaryStaticClassifier(), Settings.MIN_ACTIVITY);
		correctPercentage = 100-(int)((app.errorCount*100.0)/app.counter);
		System.out.println("Static: ");
		System.out.println("  Error: " + score);
		System.out.println("  Correct: " + correctPercentage + "%");
		System.out.println();
		
	}
	
}
