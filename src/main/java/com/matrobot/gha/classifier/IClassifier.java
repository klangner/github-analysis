package com.matrobot.gha.classifier;

public interface IClassifier {

	/**
	 * 
	 * @param currentValue - value used to make prediction.
	 * @return predicted value
	 */
	public int classify(int[] featureVector);
}
