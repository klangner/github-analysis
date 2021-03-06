package com.matrobot.gha.insights.ml;

public class NaiveBayes {

	private int featureCount;
	private double positiveCount = 1;
	private double negativeCount = 1;
	private double[] positiveEvidenceCount;
	private double[] negativeEvidenceCount;
	
	
	public NaiveBayes(int featureCount){
		this.featureCount = featureCount;
		positiveEvidenceCount = new double[featureCount];
		negativeEvidenceCount = new double[featureCount];
		for(int i = 0; i < featureCount; i++){
			positiveEvidenceCount[i] = 1;
			negativeEvidenceCount[i] = 1;
		}
		
	}
	
	
	public void train(Sample sample){
		
		if(sample.output == 1){
			positiveCount++;
			for(int i = 0; i < featureCount; i++){
				if(sample.features[i] > 0){
					positiveEvidenceCount[i] += 1;
				}
			}
		}
		else{
			negativeCount++;
			for(int i = 0; i < featureCount; i++){
				if(sample.features[i] > 0){
					negativeEvidenceCount[i] += 1;
				}
			}
		}
	}
	
	
	protected double getPositiveProb(){
		return (positiveCount)/(positiveCount+negativeCount);
	}
	
	
	protected double getNegativeProb(){
		return (negativeCount)/(positiveCount+negativeCount);
	}


	public double classify(double[] features) {
		
		double positiveScore = calculatePositiveScore(features);
		double negativeScore = calculateNegativeScore(features);
		
		if(positiveScore > negativeScore){
			return 1;
		}
		else{
			return 0;
		}
	}


	private double calculateNegativeScore(double[] features) {
		
		double hipothesisProb = getNegativeProb();
		double evidenceProb = 1;
		
		for(int i = 0; i < features.length; i++){
			if(features[i] > 0){
				evidenceProb *= negativeEvidenceCount[i]/negativeCount;
			}
			else{
				evidenceProb *= (negativeCount-negativeEvidenceCount[i])/negativeCount;
			}
		}
		
		double score = hipothesisProb * evidenceProb;
		return score;
	}


	private double calculatePositiveScore(double[] features) {
		
		double hipothesisProb = getPositiveProb();
		double evidenceProb = 1;
		
		for(int i = 0; i < features.length; i++){
			if(features[i] > 0){
				evidenceProb *= positiveEvidenceCount[i]/positiveCount;
			}
			else{
				evidenceProb *= (positiveCount-positiveEvidenceCount[i])/positiveCount;
			}
		}
		
		double score = hipothesisProb * evidenceProb;
		return score;
	}


	public void printModel() {

		System.out.println("Positive: " + positiveCount);
		for(int i = 0; i < positiveEvidenceCount.length; i++){
			System.out.println("PF" + (i+1) + ": " + positiveEvidenceCount[i]);
		}
		
		System.out.println("Negative: " + negativeCount);
		for(int i = 0; i < negativeEvidenceCount.length; i++){
			System.out.println("NF" + (i+1) + ": " + negativeEvidenceCount[i]);
		}
		
	}
}
