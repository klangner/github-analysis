package com.matrobot.gha.ml;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class Dataset {

	private List<Sample> samples = new ArrayList<Sample>();
	private int featureCount = 0;
	private double[] maxValues;
	private List<String> featureNames = new ArrayList<String>();

	
	public Dataset(int featureCount){
		
		this.featureCount = featureCount;
		for(int i = 0; i < featureCount; i++){
			featureNames.add("Feature " + (i+1));
		}
	}
	
	
	/**
	 * Add feature names.
	 * @param featureNames
	 */
	public void addFeatureNames(List<String> names){
		
		featureNames.clear();
		for(String name : names){
			featureNames.add(name);
		}
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
	
	
	public double[] getOutputs(){
		
		double[] outputs = new double[samples.size()];
		for(int i = 0; i < samples.size(); i++){
			outputs[i] = samples.get(i).output;
		}
		
		return outputs;
	}
	
	
	public double[] getFeatureFromSamples(int index){
		
		double[] outputs = new double[samples.size()];
		for(int i = 0; i < samples.size(); i++){
			outputs[i] = samples.get(i).features[index];
		}
		
		return outputs;
	}

	public double[][] getFeatures() {

		double[][] outputs = new double[samples.size()][featureCount];
		for(int i = 0; i < samples.size(); i++){
			for(int j = 0; j < featureCount; j++){
				outputs[i][j] = samples.get(i).features[j];
			}
		}
		
		return outputs;
	}
	
	
	public void saveAsCSV(String filepath) throws IOException{
		
		FileOutputStream fos = new FileOutputStream(filepath, false);
		Writer writer = new OutputStreamWriter(fos, "UTF-8");
		
		for(String name : featureNames){
			writer.write(name + ",");
		}
		writer.write("output\n");
		
		for(Sample sample : samples){
			String line = "";
			for(int i = 0; i < featureCount; i++){
				line += sample.features[i] + ",";
			}
			line += sample.output + "\n";
			writer.write(line);
		}
		writer.close();
	}
	
}
