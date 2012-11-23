package com.matrobot.gha.app;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matrobot.gha.dataset.ActivityRecord;
import com.matrobot.gha.model.IModel;
import com.matrobot.gha.model.StaticModel;

public class ModelActivityEvaluatorApp {

	private static final int MIN_ACTIVITY = 10;
	private static final double PRECISION = .1;
	private static final String DATASET_PATH = "/home/klangner/datasets/github/";
	private HashMap<String, ActivityRecord> firstDataset;
	private HashMap<String, ActivityRecord> secondDataset;
	
	
	public static void main(String[] args) throws IOException {

		ModelActivityEvaluatorApp app = new ModelActivityEvaluatorApp(DATASET_PATH+"2012/2/", DATASET_PATH+"2012/3/");
		double score = Math.floor(app.evaluate()*1000)/10;

		System.out.println("Score: " + score + "%");
		
	}
	
	protected ModelActivityEvaluatorApp(String firstPath, String secondPath) throws IOException{
		
		firstDataset = loadData(firstPath);
		secondDataset = loadData(secondPath);
	}
	
	/**
	 * Load json dataset created by ParseActivityApp
	 * 
	 * @param filename
	 * @throws IOException
	 */
	private HashMap<String, ActivityRecord> loadData(String filePath) throws IOException{
		
		List<ActivityRecord> rows = new ArrayList<ActivityRecord>();
		Gson gson = new Gson();
		Type datasetType = new TypeToken<Collection<ActivityRecord>>(){}.getType();

		FileInputStream fis = new FileInputStream(filePath+"activity.json");
		Reader reader = new InputStreamReader(fis, "UTF-8");
		rows = gson.fromJson(reader, datasetType);
		
		reader.close();

		HashMap<String, ActivityRecord> dataset = new HashMap<String, ActivityRecord>();
		for(ActivityRecord row : rows){
			dataset.put(row.repository, row);
		}
		
		return dataset;
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

		double range = correctValue*PRECISION;
		
		return (guessValue < correctValue+range && guessValue > correctValue-range);
	}

}
