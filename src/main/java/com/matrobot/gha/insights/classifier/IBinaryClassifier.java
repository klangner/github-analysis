package com.matrobot.gha.insights.classifier;

public interface IBinaryClassifier {

	/**
	 * @return confidence probability that this is positive class
	 */
	public double classify(double[] featureVector);
}
