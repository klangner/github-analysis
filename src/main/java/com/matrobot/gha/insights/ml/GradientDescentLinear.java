package com.matrobot.gha.insights.ml;


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


	public void train(Dataset dataset) {
		
//		long time = System.currentTimeMillis();
		double[] tempCoeffs = new double[dataset.getFeatureCount()+1];
		double maxGradient = 10;

		coefficients = new double[dataset.getFeatureCount()+1];
		for(int i = 0; i < coefficients.length; i++){
			coefficients[i] = 0;
		}

		double oldCost = calculateCost(dataset);
		while(maxGradient > .001){

			maxGradient = 0;
			for(int i = 0; i < coefficients.length; i++){

				double sum = 0;
				for(Sample sample : dataset.getData()){
					double h = predict(sample.features);
					double x = (i==0)? 1 : sample.features[i-1];
					sum += (h-sample.output)*x;
				}
				double gradient = sum/dataset.size();
				tempCoeffs[i] = coefficients[i] - alpha*gradient;
				maxGradient = Math.max(maxGradient, Math.abs(gradient));
			}

			for(int i = 0; i < coefficients.length; i++){
				coefficients[i] = tempCoeffs[i];
			}
			
			double newCost = calculateCost(dataset);
			if(newCost > oldCost){
				System.out.println("Cost function incresing. Probably alpha too big");
//				break;
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
	protected double calculateCost(Dataset dataset) {

		double sum = 0;
		for(Sample sample : dataset.getData()){
			double h = predict(sample.features);
			sum += Math.pow((h-sample.output), 2);
		}
		
		return sum;
	}


	public void printModel() {

		for(int i = 0; i < coefficients.length; i++){
			System.out.println(i + ": " + coefficients[i]);
		}
	}
	
}
