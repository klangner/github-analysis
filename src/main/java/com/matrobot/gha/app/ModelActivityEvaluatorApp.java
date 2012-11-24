package com.matrobot.gha.app;

import java.io.IOException;
import java.util.HashMap;

import com.matrobot.gha.dataset.ActivityDataset;
import com.matrobot.gha.dataset.ActivityRecord;
import com.matrobot.gha.model.IModel;
import com.matrobot.gha.model.LinearModel;
import com.matrobot.gha.model.StaticModel;

public class ModelActivityEvaluatorApp {

	private static final double PRECISION = .20;
	private HashMap<String, ActivityRecord> firstDataset;
	private HashMap<String, ActivityRecord> secondDataset;
	
	
	public static void main(String[] args) throws IOException {

		ModelActivityEvaluatorApp app = new ModelActivityEvaluatorApp(
				Settings.DATASET_PATH+"2012/9/", Settings.DATASET_PATH+"2012/10/");
		double score;

		// Static model
		score = app.evaluate(new StaticModel(), Settings.MIN_ACTIVITY);
		score = Math.floor(score*1000)/10;
		System.out.println("Static model score: " + score + "%");
		
		// Constant model
		score = app.evaluate(new LinearModel(1.032, 0), Settings.MIN_ACTIVITY);
		score = Math.floor(score*1000)/10;
		System.out.println("Linear model score: " + score + "%");
		
	}
	
	protected ModelActivityEvaluatorApp(String firstPath, String secondPath) throws IOException{
		
		ActivityDataset datasetReader = new ActivityDataset();
		firstDataset = datasetReader.loadData(firstPath);
		secondDataset = datasetReader.loadData(secondPath);
	}
	
	private double evaluate(IModel model, int minActivity) {

		float score = 0;
		float maxScore = 0;
		for(ActivityRecord record : firstDataset.values()){
			ActivityRecord nextRecord = secondDataset.get(record.repository); 
			if(record.activity > minActivity && nextRecord != null){
				double expected = model.makePrediction(record.activity);
				if(isInRange(expected, nextRecord.activity)){
					score += 1;
				}
				
				maxScore += 1;
			}
		}

		float modelScore;
		if(maxScore > 0){
			modelScore = score/maxScore; 
		}
		else{
			modelScore = 0;
		}
		
		return modelScore;
	}

	private boolean isInRange(double guessValue, int correctValue) {

		double range = guessValue*PRECISION;
		
		return (correctValue < guessValue+range && correctValue > guessValue-range);
	}

}
