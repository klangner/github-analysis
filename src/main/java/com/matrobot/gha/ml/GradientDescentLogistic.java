package com.matrobot.gha.ml;


/**
 * Gradient descent for logistic regression
 * 
 * @author Krzysztof Langner
 */
public class GradientDescentLogistic extends GradientDescentLinear{

	@Override
	public double predict(double[] input) {
		
		double a = getLinearRegression(input);
		
		return 1/(1 + Math.pow(Math.E, -a));
	}
	
	@Override
	protected double calculateCost(double[][] inputs, double[] outputs) {

		double sum = 0;
		for(int j = 0; j < inputs.length; j++){
			double h = predict(inputs[j]);
			double term1 = outputs[j]*Math.log(h);
			double term2 = (1-outputs[j])*Math.log(1-h);
			sum += term1+term2;
		}
		
		return -sum/inputs.length;
	}
}
