package com.matrobot.gha.app.repo;

import java.io.IOException;
import java.util.Vector;

import com.matrobot.gha.app.Settings;
import com.matrobot.gha.dataset.repo.RepositoryDatasetList;
import com.matrobot.gha.dataset.repo.RepositoryRecord;
import com.matrobot.gha.regression.IRegression;
import com.matrobot.gha.regression.MultivariableRegression;
import com.matrobot.gha.regression.StaticRegression;

public class RegressionEvaluatorApp {

	private static final int MIN_ACTIVITY = 10;
	private static final int PREDICTION_RANGE = 140;
	
	class TestSet{
		double[][] inputs;
		double[] outputs;
		Vector<String> names;
	}
	
	private RepositoryDatasetList datasets = new RepositoryDatasetList();
	private int counter;
	protected TestSet testSet;
	
	
	protected RegressionEvaluatorApp(String firstPath, String secondPath, String thirdPath) throws IOException{
		
		datasets.addFromFile(firstPath);
		datasets.addFromFile(secondPath);
		datasets.addFromFile(thirdPath);
		
		testSet = prepareTrainingData();
	}
	
	/**
	 * Training data for single feature vector
	 * @return
	 */
	private TestSet prepareTrainingData() {
	
		Vector<Double> x1 = new Vector<Double>();
		Vector<Double> x2 = new Vector<Double>();
		Vector<Double> y = new Vector<Double>();
		Vector<String> names = new Vector<String>();
		
		for(RepositoryRecord record : datasets.getDataset(1).values()){
			RepositoryRecord nextRecord = datasets.findRepository(2, record.repository); 
			RepositoryRecord prevRecord = datasets.findRepository(0, record.repository); 
			if(nextRecord.pushEventCount > MIN_ACTIVITY){
				x1.add((double) record.pushEventCount);
				double prevCount = (prevRecord!=null)?prevRecord.pushEventCount:0; 
				x2.add(prevCount);
				y.add((double) nextRecord.pushEventCount);
				names.add(record.repository);
			}
		}
		
		TestSet testSet = new TestSet();
		testSet.inputs = new double[y.size()][2];
		testSet.outputs = new double[y.size()];
		testSet.names = names;
		for(int i = 0; i < y.size(); i++){
			testSet.inputs[i][0] = x1.get(i);
			testSet.inputs[i][1] = x2.get(i);
			testSet.outputs[i] = y.get(i);
		}
	
//		scaleFeatures(testSet.inputs);
		
		return testSet;
	}

	private void evaluate(IRegression model) {
		
		counter = 0;
		int correctCount = 0;
		double sumOfErrors = 0;
		double sum = 0;
		
		for(int i = 0; i < testSet.outputs.length; i++){
			
			double forecast = model.predict(testSet.inputs[i]);
			double error = Math.pow(testSet.outputs[i]-forecast, 2);
			if(isInRange(testSet.outputs[i], forecast, PREDICTION_RANGE)){
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

//	private void scaleFeatures(double[][] inputs) {
//
//		for(int i = 0; i < inputs[0].length; i++){
//			
//			DescriptiveStatistics stats = new DescriptiveStatistics();
//			for(int j = 0; j < inputs.length; j++){
//				stats.addValue(inputs[j][i]);
//			}
//			
//			for(int j = 0; j < inputs.length; j++){
//				inputs[j][i] = (inputs[j][i]-stats.getMean())/stats.getStandardDeviation(); 
//			}
//		}
//	}

	protected void evalulateRandomRepos(IRegression regression, int count) {

		for(int i = 0; i < count; i++){
			int index = (int) (Math.random()*testSet.names.size());
			String name = testSet.names.get(index);
			double forecast = regression.predict(testSet.inputs[index]);
			System.out.println(name + ": " + (int)testSet.outputs[index] + " -> " + (int)forecast);
		}
	}

	public static void main(String[] args) throws IOException {

		IRegression regression;
		
		System.out.println("Load datasets");
		RegressionEvaluatorApp app = new RegressionEvaluatorApp(
				Settings.DATASET_PATH+"2012-9/", 
				Settings.DATASET_PATH+"2012-10/", 
				Settings.DATASET_PATH+"2012-11/");

		
		// Static classifier
		System.out.println("Static model: ");
		regression = new StaticRegression(0.8);
		app.evaluate(regression);
//		app.evalulateRandomRepos(regression, 20);
		System.out.println();
		
//		regression = CustomLinearRegression.train(app.testSet.inputs, app.testSet.outputs);
//		System.out.println("Gradiant descend: ");
//		app.evaluate(regression);
//		System.out.println();
		
		MultivariableRegression model = MultivariableRegression.trainByNormalEquation(app.testSet.inputs, app.testSet.outputs);
		model.save("models/mr1.model");
		regression = MultivariableRegression.createFromFile("models/mr1.model");
		System.out.println("Multivariable: ");
//		app.evalulateRandomRepos(regression, 20);
		app.evaluate(regression);
		System.out.println();
	}
	
}
