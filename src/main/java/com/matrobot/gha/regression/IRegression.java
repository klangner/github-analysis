package com.matrobot.gha.regression;

public interface IRegression {

	/**
	 * Make forecast about future 
	 * Param vector:
	 *  params[0] = number of pushes
	 */
	public double makeForecast(double[] params);
	
	/**
	 * Get expected error.
	 * We assume that forecast outcome is taken from normal distribution (mean)
	 * And error is normalized SD.
	 */
	public double getExpectedError();
}
