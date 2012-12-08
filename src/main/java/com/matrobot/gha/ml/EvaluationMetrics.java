package com.matrobot.gha.ml;


public class EvaluationMetrics {

	private double truePositive = 0;
	private double falseNegative = 0;
	private double falsePositive = 0;
	private double trueNegatives = 0;

	
	public void addTruePositive(){
		truePositive ++;
	}
	
	public void addTrueNegative(){
		trueNegatives ++;
	}
	
	public void addFalseNegative(){
		falseNegative ++;
	}
	
	public void addFalsePositive(){
		falsePositive ++;
	}
	

	public double getPrecision(){
		
		if(truePositive > 0){
			return truePositive/(truePositive+falsePositive);
		}
		else{
			return 0;
		}
	}

	
	public double getRecall(){

		if(truePositive > 0){
			return truePositive/(truePositive+falseNegative);
		}
		else{
			return 0;
		}
	}

	
	public double getFScore(){
		
		double precision = getPrecision();
		double recall = getRecall();
		
		if(precision+recall > 0){
			return 2*(precision*recall)/(precision+recall);
		}
		else{
			return 0;
		}
	}


	public void print() {

		System.out.println("Accuracy: " + getAccuracy());
		System.out.println("Precision: " + getPrecision());
		System.out.println("Recall: " + getRecall());
		System.out.println("F score: " + getFScore());
	}

	public double getAccuracy() {

		return (truePositive+trueNegatives)/(truePositive+trueNegatives+falsePositive+falseNegative);
	}
}
