package com.matrobot.gha.regression;


public class CustomLinearRegression implements IRegression{

	private double slope;
	private double intercept;
	
	public CustomLinearRegression(double slope, double intercept){
	
		this.slope = slope;
		this.intercept = intercept;
	}
	
	
	@Override
	public double predict(double[] input) {
		return slope*input[0]+intercept;
	}


	@Override
	public double getExpectedError() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public static CustomLinearRegression train(double[] inputs, double[] outputs){
		
		double slope = 0;
		double intercept = 0;
		double tempSlope;
		double tempIntercept;
		double gradientSlope = 10;
		double gradientIntercept = 10;
		int m = inputs.length;
		long time = System.currentTimeMillis();
		double alpha = 0.0001;
		double sum;
		
		while(Math.abs(gradientSlope) > .001 || Math.abs(gradientIntercept) > .001){

			sum = 0;
			for(int i = 0; i < m; i++){
				sum += inputs[i]*slope+intercept-outputs[i];
			}
			gradientIntercept = sum/m;
			tempIntercept = intercept - alpha*gradientIntercept; 

			sum = 0;
			for(int i = 0; i < m; i++){
				sum += (inputs[i]*slope+intercept-outputs[i])*inputs[i];
			}
			gradientSlope = sum/m;
			tempSlope = slope - alpha*gradientSlope;
			
			intercept = tempIntercept;
			slope = tempSlope;
			
		}
		
		System.out.println("a=" + slope + " b=" + intercept);
		System.out.println("Learning time: " + (System.currentTimeMillis()-time)/1000);
		return new CustomLinearRegression(slope, intercept);
	}
	
}
