package com.matrobot.gha.model;

/**
 * Linear model.
 * 
 * @author Krzysztof Langner
 *
 */
public class LinearModel implements IModel {

	private double slope;
	private double intercept;
	
	/**
	 * Linear model y = ax+b 
	 */
	public LinearModel(double a, double b){
		slope = a;
		intercept = b;
	}
	
	
	@Override
	public int makePrediction(int currentValue) {
		return (int) (currentValue*slope+intercept);
	}

}
