package com.matrobot.gha.insights.classifier;

import com.matrobot.gha.insights.ml.Dataset;
import com.matrobot.gha.insights.ml.NaiveBayes;
import com.matrobot.gha.insights.ml.Sample;

/**
 * Just assume that all are negative examples
 */
public class BayesClassifier implements IBinaryClassifier {

	private NaiveBayes model;
	
	public BayesClassifier(int featureCount) {
		model = new NaiveBayes(featureCount);
	}
	
	
	@Override
	public double classify(double[] featureVector) {
		return model.classify(featureVector);
	}

	
	public void train(Dataset testSet) {
		
		for(Sample sample : testSet.getData()){
			model.train(sample);
		}
	}
	

	public void printModel(){
		model.printModel();
	}
	
}
