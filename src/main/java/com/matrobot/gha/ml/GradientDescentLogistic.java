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
	protected double calculateCost(Dataset dataset) {

		double sum = 0;
		for(Sample sample : dataset.getData()){
			double h = predict(sample.features);
			double term1 = sample.output*Math.log(h);
			double term2 = (1-sample.output)*Math.log(1-h);
			sum += term1+term2;
		}
		
		return -sum/dataset.size();
	}

}
