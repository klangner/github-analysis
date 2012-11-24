package com.matrobot.gha.app;

import java.io.IOException;
import java.util.HashMap;

import com.matrobot.gha.category.ActivityChangeCategory;
import com.matrobot.gha.classifier.IClassifier;
import com.matrobot.gha.classifier.StaticClassifier;
import com.matrobot.gha.dataset.ActivityDataset;
import com.matrobot.gha.dataset.ActivityRecord;

public class ClassifierEvaluatorApp {

	private HashMap<String, ActivityRecord> firstDataset;
	private HashMap<String, ActivityRecord> secondDataset;
	
	
	protected ClassifierEvaluatorApp(String firstPath, String secondPath) throws IOException{
		
		ActivityDataset datasetReader = new ActivityDataset();
		firstDataset = datasetReader.loadData(firstPath);
		secondDataset = datasetReader.loadData(secondPath);
	}
	
	private double evaluate(IClassifier classifier, int minActivity) {

		int counter = 0;
		double sum = 0;
		for(ActivityRecord record : firstDataset.values()){
			ActivityRecord nextRecord = secondDataset.get(record.repository); 
			if(record.activity > minActivity && nextRecord != null){
				int expected = ActivityChangeCategory.estimateCategory(record.activity, nextRecord.activity);
				int[] featureVector = new int[1];
				featureVector[0] = record.activity;
				int classifiedCategory = classifier.classify(featureVector);
				sum += Math.pow(expected-classifiedCategory, 2);
				counter += 1;
			}
		}

		return Math.sqrt(sum/counter);
	}

	public static void main(String[] args) throws IOException {

		ClassifierEvaluatorApp app = new ClassifierEvaluatorApp(
				Settings.DATASET_PATH+"2012/2/", Settings.DATASET_PATH+"2012/3/");
		double score;

		// Static classifier
		score = app.evaluate(new StaticClassifier(3), Settings.MIN_ACTIVITY);
		System.out.println("Static3 classifier error: " + score);
		score = app.evaluate(new StaticClassifier(2), Settings.MIN_ACTIVITY);
		System.out.println("Static2 classifier error: " + score);
		
	}
	
}
