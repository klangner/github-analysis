package com.matrobot.gha.regression;

import org.apache.commons.math3.stat.regression.SimpleRegression;


public class ApacheLinearRegression implements IRegression{

	private SimpleRegression regression = new SimpleRegression();

	
	public ApacheLinearRegression(SimpleRegression regression){
	
		this.regression = regression;
	}
	
	
	@Override
	public double predict(double[] input) {
		return regression.predict(input[0]);
	}


	@Override
	public double getExpectedError() {
		return 0;
	}
	
	public static ApacheLinearRegression train(double[] inputs, double[] outputs){
		
		SimpleRegression regression = new SimpleRegression();
		
		for(int i = 0; i < inputs.length; i++){
			regression.addData(inputs[i], outputs[i]);
		}
		
		System.out.println("a=" + regression.getSlope() + " b=" + regression.getIntercept());
		return new ApacheLinearRegression(regression);
	}
	
}
