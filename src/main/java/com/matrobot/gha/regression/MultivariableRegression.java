package com.matrobot.gha.regression;




public class MultivariableRegression implements IRegression{

	private double[] coefficients;

	
	public MultivariableRegression(double[] coefficients){
		
		this.coefficients = coefficients;
	}
	
	
	@Override
	public double predict(double[] input) {
		
		double sum = 0;
		for(int i = 0; i < coefficients.length && i < input.length+1; i++){
			double x = (i==0)? 1: input[i-1];
			sum += coefficients[i]*x;
		}
		
		return sum;
	}


	public static MultivariableRegression train(double[][] inputs, double[] outputs){
		
		long time = System.currentTimeMillis();
		double[] coefficients = new double[inputs[0].length+1];
		double[] tempCoeffs = new double[inputs[0].length+1];
		double maxGradient = 10;
		double alpha = 0.0001;

		for(int i = 0; i < coefficients.length; i++){
			coefficients[i] = 0;
		}
		
		while(maxGradient > .0001){

			maxGradient = 0;
			for(int i = 0; i < coefficients.length; i++){

				double sum = 0;
				for(int j = 0; j < inputs.length; j++){
					double h = functionValue(inputs[j], coefficients);
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
		}
		
		
		System.out.println("Learning time: " + (System.currentTimeMillis()-time)/1000);
		return new MultivariableRegression(coefficients);
	}

	private static double functionValue(double[] params, double[] coefficients) {
		
		double sum = 0;
		for(int i = 0; i < coefficients.length && i < params.length+1; i++){
			double x = (i==0)? 1: params[i-1];
			sum += coefficients[i]*x;
		}
		
		return sum;
	}
}
