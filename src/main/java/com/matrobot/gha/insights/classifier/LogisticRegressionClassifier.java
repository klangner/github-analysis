package com.matrobot.gha.insights.classifier;

import com.matrobot.gha.insights.ml.Dataset;
import com.matrobot.gha.insights.ml.GradientDescentLogistic;

/**
 * Just assume that all are negative examples
 */
public class LogisticRegressionClassifier implements IBinaryClassifier {

	private GradientDescentLogistic model = new GradientDescentLogistic();
	
	public LogisticRegressionClassifier() {
		model.setAlpha(8);
	}
	
	
	@Override
	public double classify(double[] featureVector) {
		return model.predict(featureVector);
	}

	
	public void train(Dataset testSet) {
		model.train(testSet);
	}
	

	public void printModel(){
		model.printModel();
	}
	
}
