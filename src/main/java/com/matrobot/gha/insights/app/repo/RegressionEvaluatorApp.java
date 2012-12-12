package com.matrobot.gha.insights.app.repo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.matrobot.gha.insights.filter.RegressionRepositoryFilter;
import com.matrobot.gha.insights.ml.Dataset;
import com.matrobot.gha.insights.ml.Sample;
import com.matrobot.gha.insights.regression.IRegression;
import com.matrobot.gha.insights.regression.MultivariableRegression;
import com.matrobot.gha.insights.regression.StaticRegression;

public class RegressionEvaluatorApp {

	private static final int PREDICTION_RANGE = 140;
	
	Properties prop = new Properties();
	private Dataset dataset;
	private int counter;
	
	
	protected RegressionEvaluatorApp(String firstPath, String secondPath, String thirdPath) throws IOException{
		
		prop.load(new FileInputStream("config.properties"));
		RegressionRepositoryFilter filter = new RegressionRepositoryFilter(
				prop.getProperty("data_path") + firstPath, 
				prop.getProperty("data_path") + secondPath, 
				prop.getProperty("data_path") + thirdPath);
		dataset = filter.getDataset();
		dataset.normalize();
	}
	
	private void evaluate(IRegression model) {
		
		counter = 0;
		int correctCount = 0;
		double sumOfErrors = 0;
		double sum = 0;
		
		for(Sample sample : dataset.getData()){
			
			double forecast = model.predict(sample.features);
			double error = Math.pow(sample.output-forecast, 2);
			if(isInRange(sample.output, forecast, PREDICTION_RANGE)){
				correctCount ++;
			}
			sumOfErrors += error;
			sum += forecast;
			counter += 1;
		}

		System.out.println(" Forecast mean: " + (sum/counter));
		System.out.println(" SE: " + Math.sqrt(sumOfErrors/counter));
		System.out.println(" Correct: " + (correctCount*100.0)/counter);
	}

	
	private boolean isInRange(double expected, double forecast, int rangePercentage) {

		double size = expected/100*rangePercentage;
		return (forecast<expected+size && forecast>expected-size);
	}


	public static void main(String[] args) throws IOException {

		IRegression regression;
		
		System.out.println("Load datasets");
		RegressionEvaluatorApp app = new RegressionEvaluatorApp("2012-9/", "2012-10/", "2012-11/");

		
		// Static classifier
		System.out.println("Static model: ");
		regression = new StaticRegression(0.8);
		app.evaluate(regression);
//		app.evalulateRandomRepos(regression, 20);
		System.out.println();
		
		MultivariableRegression model = MultivariableRegression.trainByNormalEquation(app.dataset);
		model.save("models/mr1.model");
		regression = MultivariableRegression.createFromFile("models/mr1.model");
		System.out.println("Multivariable: ");
//		app.evalulateRandomRepos(regression, 20);
		app.evaluate(regression);
		System.out.println();
	}
	
}
