package com.matrobot.gha.regression;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

import com.matrobot.gha.ml.Dataset;




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
		
		return Math.max(sum, 0);
	}


	public static MultivariableRegression train(double[][] inputs, double[] outputs){
		
		long time = System.currentTimeMillis();
		double[] coefficients = new double[inputs[0].length+1];
		double[] tempCoeffs = new double[inputs[0].length+1];
		double maxGradient = 10;
		double alpha = 1;

		for(int i = 0; i < coefficients.length; i++){
			coefficients[i] = 0;
		}

		double oldCost = calculateCost(inputs, coefficients, outputs);
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
			
			double newCost = calculateCost(inputs, coefficients, outputs);
			if(newCost > oldCost){
				System.out.println("Cost function incresing. Probably aplha too big");
				break;
			}
			else{
				oldCost = newCost;
			}
		}
		
		
		System.out.println("Learning time: " + (System.currentTimeMillis()-time)/1000);
		return new MultivariableRegression(coefficients);
	}

	
	private static double calculateCost(double[][] inputs, double[] coeffs, double[] outputs) {

		double sum = 0;
		for(int j = 0; j < inputs.length; j++){
			double h = functionValue(inputs[j], coeffs);
			sum += Math.pow((h-outputs[j]), 2);
		}
		
		return sum;
	}


	private static double functionValue(double[] params, double[] coefficients) {
		
		double sum = 0;
		for(int i = 0; i < coefficients.length && i < params.length+1; i++){
			double x = (i==0)? 1: params[i-1];
			sum += coefficients[i]*x;
		}
		
		return sum;
	}


	@Override
	public void printModel() {

		for(int i = 0; i < coefficients.length; i++){
			System.out.println(i + ": " + coefficients[i]);
		}
	}
	
	public static MultivariableRegression trainByNormalEquation(Dataset dataset){

		OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
		regression.newSampleData(dataset.getOutputs(), dataset.getFeatures());
		return new MultivariableRegression(regression.estimateRegressionParameters());
	}

	
	public void save(String filename){
		
		try{
			FileOutputStream fos = new FileOutputStream(filename, false);
			Writer writer = new OutputStreamWriter(fos, "UTF-8");
			for(int i = 0; i < coefficients.length; i++){
				writer.write(Double.toString(coefficients[i]));
				if(i+1 < coefficients.length){
					writer.write(",");
				}
			}
			writer.close();
			
		}catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}
	}

	
	public static MultivariableRegression createFromFile(String filename){
		
		double[] coeff = null;
		
		try{
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String content = reader.readLine();
			String[] tokens = content.split(",");
			coeff = new double[tokens.length];
			for(int i = 0; i < tokens.length; i++){
				coeff[i] = Double.parseDouble(tokens[i]);
			}
			
			reader.close();
			
		}catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}
		
		return new MultivariableRegression(coeff);
	}
}
