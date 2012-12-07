package com.matrobot.gha.classifier;

import com.matrobot.gha.ml.Dataset;
import com.matrobot.gha.ml.GradientDescentLogistic;

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
}
