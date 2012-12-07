package com.matrobot.gha.ml;

import java.util.ArrayList;
import java.util.List;

public class Dataset {

	private List<Sample> samples = new ArrayList<Sample>();
	private int featureCount = 0;
	private double[] maxValues;

	
	public Dataset(int featureCount){
		this.featureCount = featureCount;
	}
	
	public void addSample(double[] features, double output){
		
		assert(features.length == featureCount);
		
		Sample sample = new Sample();
		sample.features = features;
		sample.output = output;
		addSample(sample);
	}
	
	
	public int size(){
		return samples.size();
	}

	public int getFeatureCount() {
		return featureCount;
	}
	
	public List<Sample> getData(){
		return samples;
	}

	
	public void normalize() {

		maxValues = new double[featureCount];
		for(int i = 0; i < featureCount; i++){
			maxValues[i] = 0;
		}

		// Calculate max value
		for(Sample sample : samples){
			
			for(int i = 0; i < featureCount; i++){
				double value = Math.abs(sample.features[i]); 
				if(value > maxValues[i]){
					maxValues[i] = value;
				}
			}
		}
		
		// scale
		for(Sample sample : samples){
			
			sample.features = normalize(sample.features);
		}
	}
	
	
	public double[] normalize(double[] features) {

		for(int i = 0; i < featureCount; i++){
			features[i] = features[i]/maxValues[i];
		}
		
		return features;
	}

	public void addSample(Sample sample) {
		samples.add(sample);
	}
	
}
