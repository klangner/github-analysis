package com.matrobot.gha.app.repo;

import java.io.IOException;

import com.matrobot.gha.app.Settings;
import com.matrobot.gha.classifier.BayesClassifier;
import com.matrobot.gha.classifier.Binary1RClassifier;
import com.matrobot.gha.classifier.IBinaryClassifier;
import com.matrobot.gha.classifier.LogisticRegressionClassifier;
import com.matrobot.gha.filter.ClassifyRepositoryFilter;
import com.matrobot.gha.ml.Dataset;
import com.matrobot.gha.ml.EvaluationMetrics;
import com.matrobot.gha.ml.Sample;

public class ClassifierEvaluatorApp {

	private Dataset dataset;
	private int counter;
	private EvaluationMetrics metrics;
	
	
	protected ClassifierEvaluatorApp(String firstPath, String secondPath, String thirdPath) throws IOException{
		
		ClassifyRepositoryFilter filter = new ClassifyRepositoryFilter(firstPath, secondPath, thirdPath);
		dataset = filter.getDataset();
		dataset.normalize();
	}
	
	private double evaluate(IBinaryClassifier classifier, Dataset dataset) {

		metrics = new EvaluationMetrics();
		counter = 0;
		double sum = 0;
		for(Sample sample : dataset.getData()){
				
			double confidence = classifier.classify(dataset.normalize(sample.features));
			double error = Math.pow(sample.output-confidence, 2); 
			sum += error;
			if(error > 0.25){
				if(sample.output == 1){
					metrics.addFalseNegative();
				}
				else{
					metrics.addFalsePositive();
				}
			}
			else{
				if(sample.output == 1){
					metrics.addTruePositive();
				}
				else{
					metrics.addTrueNegative();
				}
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
		Dataset dataset = app.dataset;
		dataset.saveAsCSV(Settings.DATASET_PATH+"weka.csv");
		
		// 1R classifier
		System.out.println("1R: ");
		app.evaluate(new Binary1RClassifier(), dataset);
		app.metrics.print();
		System.out.println();

		// Bayes classifier
		System.out.println("Bayes: ");
		BayesClassifier bayes = new BayesClassifier(2);
		System.out.println("Train");
		bayes.train(dataset);
		app.evaluate(bayes, dataset);
		app.metrics.print();
		bayes.printModel();
		System.out.println();

		// Logistic regression
		LogisticRegressionClassifier classifier = new LogisticRegressionClassifier();
		System.out.println("Train");
		classifier.train(dataset);
		System.out.println("Evaluate");
		app.evaluate(classifier, dataset);
		System.out.println("Logistic regression: ");
		app.metrics.print();
		System.out.println();
	}
	
}
