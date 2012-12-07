package com.matrobot.gha.app.repo;

import java.io.IOException;

import com.matrobot.gha.app.Settings;
import com.matrobot.gha.classifier.BinaryStaticClassifier;
import com.matrobot.gha.classifier.IBinaryClassifier;
import com.matrobot.gha.classifier.LogisticRegressionClassifier;
import com.matrobot.gha.filter.RepositoryActivityFilter;
import com.matrobot.gha.ml.Dataset;
import com.matrobot.gha.ml.Sample;

public class ClassifierEvaluatorApp {

	private Dataset dataset;
	private int errorCount;
	private int counter;
	
	
	protected ClassifierEvaluatorApp(String firstPath, String secondPath, String thirdPath) throws IOException{
		
		RepositoryActivityFilter filter = new RepositoryActivityFilter(firstPath, secondPath, thirdPath);
		dataset = filter.getDataset();
		dataset.normalize();
	}
	
	private double evaluate(IBinaryClassifier classifier, Dataset dataset) {

		counter = 0;
		errorCount = 0;
		double sum = 0;
		for(Sample sample : dataset.getData()){
				
			double confidence = classifier.classify(dataset.normalize(sample.features));
			double error = Math.pow(sample.output-confidence, 2); 
			sum += error;
			if(error > 0.25){
				errorCount++;
			}
			counter += 1;
		}

		return Math.sqrt(sum/counter);
	}

	
	/**
	 * Feature vector:
	 *  feature[0] = currentActivity in log10 scale
	 *  feature[1] = current activity rating (from previous month)
	 */
	public static void main(String[] args) throws IOException {

		ClassifierEvaluatorApp app = new ClassifierEvaluatorApp(
				Settings.DATASET_PATH+"2012-1/", 
				Settings.DATASET_PATH+"2012-10/",
				Settings.DATASET_PATH+"2012-11/");
		double score;
		int correctPercentage;
		Dataset testSet = app.dataset;

		// Static classifier
		score = app.evaluate(new BinaryStaticClassifier(), testSet);
		correctPercentage = 100-(int)((app.errorCount*100.0)/app.counter);
		System.out.println("Static: ");
		System.out.println("  Error: " + score);
		System.out.println("  Correct: " + correctPercentage + "%");
		System.out.println();

		// Logistic regression
		LogisticRegressionClassifier classifier = new LogisticRegressionClassifier();
		System.out.println("Train");
		classifier.train(testSet);
		System.out.println("Evaluate");
		score = app.evaluate(classifier, testSet);
		correctPercentage = 100-(int)((app.errorCount*100.0)/app.counter);
		System.out.println("Logistic regression: ");
		System.out.println("  Error: " + score);
		System.out.println("  Correct: " + correctPercentage + "%");
		System.out.println();
		
	}
	
}
