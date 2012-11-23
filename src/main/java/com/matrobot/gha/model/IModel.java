package com.matrobot.gha.model;

public interface IModel {

	/**
	 * 
	 * @param currentValue - value used to make prediction.
	 * @return predicted value
	 */
	public int makePrediction(int currentValue);
}
