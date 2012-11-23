package com.matrobot.gha.app;

import java.io.IOException;
import java.util.HashMap;

import com.matrobot.gha.dataset.ActivityDataset;
import com.matrobot.gha.dataset.ActivityRecord;
import com.matrobot.gha.model.IModel;
import com.matrobot.gha.model.StaticModel;

public class ModelActivityEvaluatorApp {

	private static final int MIN_ACTIVITY = 10;
	private static final double PRECISION = .05;
	private static final String DATASET_PATH = "/home/klangner/datasets/github/";
	private HashMap<String, ActivityRecord> firstDataset;
	private HashMap<String, ActivityRecord> secondDataset;
	
	
	public static void main(String[] args) throws IOException {

		ModelActivityEvaluatorApp app = new ModelActivityEvaluatorApp(DATASET_PATH+"2012/2/", DATASET_PATH+"2012/3/");
		double score = Math.floor(app.evaluate()*1000)/10;

		System.out.println("Score: " + score + "%");
		
	}
	
	protected ModelActivityEvaluatorApp(String firstPath, String secondPath) throws IOException{
		
		ActivityDataset datasetReader = new ActivityDataset();
		firstDataset = datasetReader.loadData(firstPath);
		secondDataset = datasetReader.loadData(secondPath);
	}
	
	private double evaluate() {

		IModel model = new StaticModel();
		float score = 0;
		float maxScore = 0;
		for(ActivityRecord record : firstDataset.values()){
			ActivityRecord nextRecord = secondDataset.get(record.repository); 
			if(record.activity > MIN_ACTIVITY && nextRecord != null){
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
