package com.matrobot.gha.app.repo;

import java.io.IOException;
import java.util.Vector;

import com.matrobot.gha.app.Settings;
import com.matrobot.gha.dataset.repo.RepositoryDatasetList;
import com.matrobot.gha.dataset.repo.RepositoryRecord;
import com.matrobot.gha.regression.CustomLinearRegression;
import com.matrobot.gha.regression.IRegression;
import com.matrobot.gha.regression.ApacheLinearRegression;
import com.matrobot.gha.regression.StaticRegression;

public class RegressionEvaluatorApp {

	private RepositoryDatasetList datasets = new RepositoryDatasetList();
	private int counter;
	
	
	protected RegressionEvaluatorApp(String firstPath, String secondPath, String thirdPath) throws IOException{
		
		datasets.addFromFile(firstPath);
		datasets.addFromFile(secondPath);
//		datasets.addFromFile(thirdPath);
	}
	
	private void evaluate(IRegression model, int minActivity) {

		counter = 0;
		double sumOfErrors = 0;
		double sum = 0;
		double[][] trainingData = getTrainingData();
		for(int i = 0; i < trainingData[0].length; i++){
			
			double[] params = new double[1];
			params[0] = trainingData[0][i];
			double forecast = model.predict(params);
			
			double error = Math.pow(trainingData[1][i]-forecast, 2); 
			sumOfErrors += error;
			sum += forecast;
			counter += 1;
		}

		System.out.println(" Forecast mean: " + (sum/counter));
		System.out.println(" SE: " + Math.sqrt(sumOfErrors/counter));
	}

	
	private double[][] getTrainingData() {
	
		Vector<Double> x = new Vector<Double>();
		Vector<Double> y = new Vector<Double>();
		
		for(RepositoryRecord record : datasets.getDataset(0).values()){
			RepositoryRecord nextRecord = datasets.findRepository(1, record.repository); 
			if(nextRecord.pushEventCount > 10){
				x.add((double) record.pushEventCount);
				y.add((double) nextRecord.pushEventCount);
			}
		}
		
		double[][] data = new double[2][x.size()];
		for(int i = 0; i < x.size(); i++){
			data[0][i] = x.get(i);
			data[1][i] = y.get(i);
		}

		return data;
	}

	public static void main(String[] args) throws IOException {

		RegressionEvaluatorApp app = new RegressionEvaluatorApp(
				Settings.DATASET_PATH+"2012-9/", 
				Settings.DATASET_PATH+"2012-10/",
				Settings.DATASET_PATH+"2011-12/");

		// Static classifier
		System.out.println("Static model: ");
		app.evaluate(new StaticRegression(0.60), Settings.MIN_ACTIVITY);
		System.out.println();
		
		double[][] trainingData = app.getTrainingData();
		IRegression regression = CustomLinearRegression.train(trainingData[0], trainingData[1]);
		System.out.println("Gradiant descend: ");
		app.evaluate(regression, Settings.MIN_ACTIVITY);
		System.out.println();
		
		regression = ApacheLinearRegression.train(trainingData[0], trainingData[1]);
		System.out.println("Simple descend: ");
		app.evaluate(regression, Settings.MIN_ACTIVITY);
		System.out.println();
		
	}
	
}
