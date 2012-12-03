package com.matrobot.gha.app.repo;

import java.io.IOException;
import java.util.Vector;

import com.matrobot.gha.app.Settings;
import com.matrobot.gha.dataset.repo.RepositoryDatasetList;
import com.matrobot.gha.dataset.repo.RepositoryRecord;
import com.matrobot.gha.regression.CustomLinearRegression;
import com.matrobot.gha.regression.IRegression;
import com.matrobot.gha.regression.MultivariableRegression;
import com.matrobot.gha.regression.StaticRegression;

public class RegressionEvaluatorApp {

	private static final int MIN_ACTIVITY = 10;
	class TestSet{
		double[][] inputs;
		double[] outputs;
	}
	
	private RepositoryDatasetList datasets = new RepositoryDatasetList();
	private int counter;
	protected TestSet testSet;

	
	
	protected RegressionEvaluatorApp(String firstPath, String secondPath, String thirdPath) throws IOException{
		
		datasets.addFromFile(firstPath);
		datasets.addFromFile(secondPath);
		datasets.addFromFile(thirdPath);
		
		testSet = getMultvariableTrainingData();
	}
	
	private void evaluate(IRegression model) {

		counter = 0;
		double sumOfErrors = 0;
		double sum = 0;
		
		for(int i = 0; i < testSet.outputs.length; i++){
			
			double forecast = model.predict(testSet.inputs[i]);
			
			double error = Math.pow(testSet.outputs[i]-forecast, 2); 
			sumOfErrors += error;
			sum += forecast;
			counter += 1;
		}

		System.out.println(" Forecast mean: " + (sum/counter));
		System.out.println(" SE: " + Math.sqrt(sumOfErrors/counter));
	}

	
	/**
	 * Training data for single feature vector
	 * @return
	 */
	private TestSet getMultvariableTrainingData() {
	
		Vector<Double> x1 = new Vector<Double>();
		Vector<Double> x2 = new Vector<Double>();
		Vector<Double> y = new Vector<Double>();
		
		for(RepositoryRecord record : datasets.getDataset(1).values()){
			RepositoryRecord nextRecord = datasets.findRepository(2, record.repository); 
			RepositoryRecord prevRecord = datasets.findRepository(0, record.repository); 
			if(nextRecord.pushEventCount > MIN_ACTIVITY){
				x1.add((double) record.pushEventCount);
				double prevCount = (prevRecord!=null)?prevRecord.pushEventCount:0; 
				x2.add(prevCount);
				y.add((double) nextRecord.pushEventCount);
			}
		}
		
		TestSet testSet = new TestSet();
		testSet.inputs = new double[y.size()][2];
		testSet.outputs = new double[y.size()];
		for(int i = 0; i < y.size(); i++){
			testSet.inputs[i][0] = x1.get(i);
			testSet.inputs[i][1] = x2.get(i);
			testSet.outputs[i] = y.get(i);
		}

		return testSet;
	}

	
	public static void main(String[] args) throws IOException {

		RegressionEvaluatorApp app = new RegressionEvaluatorApp(
				Settings.DATASET_PATH+"2012-8/", 
				Settings.DATASET_PATH+"2012-9/", 
				Settings.DATASET_PATH+"2012-10/");

		// Static classifier
		System.out.println("Static model: ");
		app.evaluate(new StaticRegression(0.60));
		System.out.println();
		
		IRegression regression = CustomLinearRegression.train(app.testSet.inputs, app.testSet.outputs);
		System.out.println("Gradiant descend: ");
		app.evaluate(regression);
		System.out.println();
		
		regression = MultivariableRegression.train(app.testSet.inputs, app.testSet.outputs);
		System.out.println("Multivariable: ");
		app.evaluate(regression);
		System.out.println();
		
	}
	
}
