package com.matrobot.gha.regression;

public class StaticRegression implements IRegression {

	private double staticValue;
	
	public StaticRegression(double value){
		staticValue = value;
	}
	
	
	@Override
	public double makeForecast(double[] params){
		return staticValue*params[0];
	}


	@Override
	public double getExpectedError() {
		return 0;
	}

}
