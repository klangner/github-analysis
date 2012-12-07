package com.matrobot.gha.ml;


/**
 * Gradient descent for multiple variables
 * 
 * @author Krzysztof Langner
 */
public class GradientDescentLinear{

	private double alpha = 1;
	private double[] coefficients;

	
	/**
	 * Override this function for different regression model (e.g logistic regression)
	 * @param input
	 * @return
	 */
	public double predict(double[] input) {
		
		return getLinearRegression(input);
	}
	
	protected double getLinearRegression(double[] input) {
		
		double sum = 0;
		for(int i = 0; i < coefficients.length && i < input.length+1; i++){
			double x = (i==0)? 1: input[i-1];
			sum += coefficients[i]*x;
		}
		
		return sum;
	}
	
	public void setAlpha(double alpha){
		this.alpha = alpha;
	}


	public void train(double[][] inputs, double[] outputs){
		
//		long time = System.currentTimeMillis();
		double[] tempCoeffs = new double[inputs[0].length+1];
		double maxGradient = 10;

		coefficients = new double[inputs[0].length+1];
		for(int i = 0; i < coefficients.length; i++){
			coefficients[i] = 0;
		}

		double oldCost = calculateCost(inputs, outputs);
		while(maxGradient > .0001){

			maxGradient = 0;
			for(int i = 0; i < coefficients.length; i++){

				double sum = 0;
				for(int j = 0; j < inputs.length; j++){
					double h = predict(inputs[j]);
					double x = (i==0)? 1 : inputs[j][i-1];
					sum += (h-outputs[j])*x;
				}
				double gradient = sum/inputs.length;
				tempCoeffs[i] = coefficients[i] - alpha*gradient;
				maxGradient = Math.max(maxGradient, Math.abs(gradient));
			}

			for(int i = 0; i < coefficients.length; i++){
				coefficients[i] = tempCoeffs[i];
			}
			
			double newCost = calculateCost(inputs, outputs);
			if(newCost > oldCost){
				System.out.println("Cost function incresing. Probably alpha too big");
				break;
			}
			else{
				oldCost = newCost;
			}
		}
		
//		System.out.println("Learning time: " + (System.currentTimeMillis()-time)/1000);
	}

	
	/**
	 * Calculate cost function on given inputs.
	 * Override this function for different regression model (e.g logistic regression)
	 */
	protected double calculateCost(double[][] inputs, double[] outputs) {

		double sum = 0;
		for(int j = 0; j < inputs.length; j++){
			double h = predict(inputs[j]);
			sum += Math.pow((h-outputs[j]), 2);
		}
		
		return sum;
	}


	public void printModel() {

		for(int i = 0; i < coefficients.length; i++){
			System.out.println(i + ": " + coefficients[i]);
		}
	}
	
}
