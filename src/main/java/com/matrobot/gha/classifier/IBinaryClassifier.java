package com.matrobot.gha.classifier;

public interface IBinaryClassifier {

	/**
	 * @return confidence probability that this is positive class
	 */
	public double classify(double[] featureVector);
}
