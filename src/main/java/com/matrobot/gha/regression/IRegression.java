package com.matrobot.gha.regression;

public interface IRegression {

	/**
	 * Make forecast about future 
	 * Param vector:
	 *  params[0] = number of pushes
	 */
	public double predict(double[] params);

	public void printModel();
}
