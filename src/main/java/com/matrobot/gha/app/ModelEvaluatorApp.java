package com.matrobot.gha.app;

import java.io.IOException;
import java.util.HashMap;

import com.matrobot.gha.dataset.ActivityDataset;
import com.matrobot.gha.dataset.ActivityRecord;
import com.matrobot.gha.model.IModel;
import com.matrobot.gha.model.LinearModel;
import com.matrobot.gha.model.StaticModel;

public class ModelEvaluatorApp {

	private HashMap<String, ActivityRecord> firstDataset;
	private HashMap<String, ActivityRecord> secondDataset;
	
	
	protected ModelEvaluatorApp(String firstPath, String secondPath) throws IOException{
		
		ActivityDataset datasetReader = new ActivityDataset();
		firstDataset = datasetReader.loadData(firstPath);
		secondDataset = datasetReader.loadData(secondPath);
	}
	
	private double evaluate(IModel model, int minActivity) {

		int counter = 0;
		double sum = 0;
		for(ActivityRecord record : firstDataset.values()){
			ActivityRecord nextRecord = secondDataset.get(record.repository); 
			if(record.activity > minActivity && nextRecord != null){
				double expected = model.makePrediction(record.activity);
				sum += relativeDistancePow(expected, nextRecord.activity);
				counter += 1;
			}
		}

		return Math.sqrt(sum/counter);
	}

	private double relativeDistancePow(double guessValue, int correctValue) {

		double range = (guessValue-correctValue)/correctValue;
		return Math.pow(range, 2);
	}

	public static void main(String[] args) throws IOException {

		ModelEvaluatorApp app = new ModelEvaluatorApp(
				Settings.DATASET_PATH+"2012/2/", Settings.DATASET_PATH+"2012/3/");
		double score;

		// Static model
		score = app.evaluate(new StaticModel(), Settings.MIN_ACTIVITY);
		System.out.println("Static model score: " + score);
		
		// Constant model
		score = app.evaluate(new LinearModel(0.42, 0), Settings.MIN_ACTIVITY);
		System.out.println("Linear model score: " + score);
		
	}
	
}
