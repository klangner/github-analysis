package com.matrobot.gha.model;

public class StaticModel implements IModel {

	private double staticValue;
	
	public StaticModel(double value){
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
