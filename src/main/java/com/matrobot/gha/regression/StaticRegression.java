package com.matrobot.gha.regression;

public class StaticRegression implements IRegression {

	private double staticValue;
	
	public StaticRegression(double value){
		staticValue = value;
	}
	
	
	@Override
	public double predict(double[] params){
		return staticValue*params[0];
	}


	@Override
	public void printModel() {

		System.out.println("Static: " + staticValue);
	}
}
