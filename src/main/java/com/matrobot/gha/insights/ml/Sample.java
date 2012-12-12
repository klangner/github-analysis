package com.matrobot.gha.insights.ml;

public class Sample {

	public String name;
	public double[] features;
	public double output;
	
	
	public Sample(int size){
		features = new double[size];
	}
	
	
	public Sample(double[] features, double output){
		this.features = features;
		this.output = output;
	}
}
