package com.matrobot.gha.model;

public interface IModel {

	/**
	 * Make forecast about future 
	 */
	public double makeForecast(double[] params);
	
	/**
	 * Get expected error.
	 * We assume that forecast outcome is taken from normal distribution (mean)
	 * And error is normalized SD.
	 */
	public double getExpectedError();
}
