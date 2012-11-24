package com.matrobot.gha.classifier;

public class StaticClassifier implements IClassifier {

	private int staticValue;
	
	public StaticClassifier(int value){
		staticValue = value;
	}
	
	
	@Override
	public int classify(int[] featureVector) {
		return staticValue;
	}

}
