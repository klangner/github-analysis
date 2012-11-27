package com.matrobot.gha.app.repo;

import java.io.IOException;

import com.matrobot.gha.app.Settings;
import com.matrobot.gha.dataset.repo.RepositoryDatasetList;
import com.matrobot.gha.dataset.repo.RepositoryRecord;
import com.matrobot.gha.regression.IRegression;
import com.matrobot.gha.regression.StaticRegression;

public class RegressionEvaluatorApp {

	private RepositoryDatasetList datasets = new RepositoryDatasetList();
	private int counter;
	
	
	protected RegressionEvaluatorApp(String firstPath, String secondPath, String thirdPath) throws IOException{
		
		datasets.addFromFile(firstPath);
		datasets.addFromFile(secondPath);
//		datasets.addFromFile(thirdPath);
	}
	
	private double evaluate(IRegression model, int minActivity) {

		counter = 0;
		double sum = 0;
		for(RepositoryRecord record : datasets.getDataset(0).values()){
			RepositoryRecord nextRecord = datasets.findRepository(1, record.repository); 
			double currentActivity = record.pushEventCount;
			double nextActivity = nextRecord.pushEventCount;
			if(currentActivity > minActivity){
				
				double[] params = createParamVector(record);
				double forecast = model.makeForecast(params);
				
				double error = Math.pow(nextActivity-forecast, 2); 
				sum += error;
				counter += 1;
			}
		}

		return Math.sqrt(sum/counter);
	}

	
	/**
	 * Feature vector:
	 *  feature[0] = number of pushes
	 */
	private double[] createParamVector(RepositoryRecord currentRecord) {
		
		double[] featureVector = new double[1];
		featureVector[0] = currentRecord.pushEventCount;
		return featureVector;
	}

	public static void main(String[] args) throws IOException {

		RegressionEvaluatorApp app = new RegressionEvaluatorApp(
				Settings.DATASET_PATH+"2012-1/", 
				Settings.DATASET_PATH+"2012-2/",
				Settings.DATASET_PATH+"2012-3/");
		double score;

		// Static classifier
		score = app.evaluate(new StaticRegression(0.32), Settings.MIN_ACTIVITY);
		System.out.println("Static model: ");
		System.out.println("  Error: " + score);
		System.out.println();
	}
	
}
