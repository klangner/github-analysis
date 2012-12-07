package com.matrobot.gha.app.repo;

import java.io.IOException;

import com.matrobot.gha.app.Settings;
import com.matrobot.gha.classifier.BinaryStaticClassifier;
import com.matrobot.gha.classifier.IBinaryClassifier;
import com.matrobot.gha.classifier.LogisticRegressionClassifier;
import com.matrobot.gha.dataset.repo.RepositoryDatasetList;
import com.matrobot.gha.dataset.repo.RepositoryRecord;
import com.matrobot.gha.ml.Dataset;
import com.matrobot.gha.ml.Sample;

public class ClassifierEvaluatorApp {

	private static final int MIN_ACTIVITY = 0;
	private RepositoryDatasetList datasets = new RepositoryDatasetList();
	private int errorCount;
	private int counter;
	
	
	protected ClassifierEvaluatorApp(String firstPath, String secondPath, String thirdPath) throws IOException{
		
		datasets.addFromFile(firstPath);
		datasets.addFromFile(secondPath);
		datasets.addFromFile(thirdPath);
	}
	
	private double evaluate(IBinaryClassifier classifier, Dataset dataset) {

		counter = 0;
		errorCount = 0;
		double sum = 0;
		for(RepositoryRecord record : datasets.getDataset(1).values()){
			RepositoryRecord nextRecord = datasets.findRepository(2, record.repository); 
			RepositoryRecord prevRecord = datasets.findRepository(0, record.repository); 
			if(nextRecord.pushEventCount > MIN_ACTIVITY){
				
				Sample sample = createSample(record, nextRecord, prevRecord);
				
				double confidence = classifier.classify(dataset.normalize(sample.features));
				
				double error = Math.pow(sample.output-confidence, 2); 
				sum += error;
				if(error > 0.25){
					errorCount++;
				}
				counter += 1;
			}
		}

		return Math.sqrt(sum/counter);
	}

	
	/**
	 * Training data for single feature vector
	 * @return
	 */
	public Dataset prepareTrainingData() {
	
		Dataset dataset = new Dataset(2);
		
		for(RepositoryRecord record : datasets.getDataset(1).values()){
			RepositoryRecord nextRecord = datasets.findRepository(2, record.repository); 
			RepositoryRecord prevRecord = datasets.findRepository(0, record.repository); 
			if(nextRecord.pushEventCount > MIN_ACTIVITY){
				Sample sample = createSample(record, nextRecord, prevRecord);
				dataset.addSample(sample);
			}
		}
		
		return dataset;
	}

	private Sample createSample(RepositoryRecord record,
			RepositoryRecord nextRecord, RepositoryRecord prevRecord) {
		Sample sample = new Sample();
		sample.features = new double[2];
		sample.features[0] = record.pushEventCount-prevRecord.pushEventCount;
		sample.features[1] =  record.pushEventCount;
		sample.output = (nextRecord.pushEventCount > record.pushEventCount)? 1 : 0;
		return sample;
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
		Dataset testSet = app.prepareTrainingData();
		testSet.normalize();

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
