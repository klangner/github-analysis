package com.matrobot.gha.app.classifier;

import java.io.IOException;
import java.util.HashMap;

import com.matrobot.gha.app.Settings;
import com.matrobot.gha.category.ActivityChangeCategory;
import com.matrobot.gha.classifier.IClassifier;
import com.matrobot.gha.classifier.StaticClassifier;
import com.matrobot.gha.dataset.ActivityDataset;
import com.matrobot.gha.dataset.ActivityRecord;

public class ClassifierEvaluatorApp {

	private HashMap<String, ActivityRecord> firstDataset;
	private HashMap<String, ActivityRecord> goldDataset;
	private int errorCount;
	private int counter;
	
	
	protected ClassifierEvaluatorApp(String firstPath, String secondPath) throws IOException{
		
		ActivityDataset datasetReader = new ActivityDataset();
		firstDataset = datasetReader.loadData(firstPath);
		goldDataset = datasetReader.loadData(secondPath);
	}
	
	private double evaluate(IClassifier classifier, int minActivity) {

		counter = 0;
		errorCount = 0;
		double sum = 0;
		for(ActivityRecord record : firstDataset.values()){
			ActivityRecord goldRecord = goldDataset.get(record.repository); 
			if(record.activity > minActivity && goldRecord != null){
				int expected = ActivityChangeCategory.estimateCategory(record.activity, goldRecord.activity);
				int[] featureVector = new int[1];
				featureVector[0] = record.activity;
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

	public static void main(String[] args) throws IOException {

		ClassifierEvaluatorApp app = new ClassifierEvaluatorApp(
				Settings.DATASET_PATH+"2012/2/", Settings.DATASET_PATH+"2012/3/");
		double score;
		int correctPercentage;

		// Static classifier
		score = app.evaluate(new StaticClassifier(3), Settings.MIN_ACTIVITY);
		correctPercentage = 100-(int)((app.errorCount*100.0)/app.counter);
		System.out.println("Static3:");
		System.out.println("  Error: " + score);
		System.out.println("  Correct: " + correctPercentage + "%");
		System.out.println();
		
		score = app.evaluate(new StaticClassifier(2), Settings.MIN_ACTIVITY);
		correctPercentage = 100-(int)((app.errorCount*100.0)/app.counter);
		System.out.println("Static2: ");
		System.out.println("  Error: " + score);
		System.out.println("  Correct: " + correctPercentage + "%");
		System.out.println();
		
	}
	
}
