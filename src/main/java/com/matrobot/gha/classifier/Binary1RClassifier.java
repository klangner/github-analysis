package com.matrobot.gha.classifier;

/**
 * Just assume that all are negative examples
 */
public class Binary1RClassifier implements IBinaryClassifier {

	@Override
	public double classify(double[] featureVector) {

		if(featureVector[0] > 0){
			return 1;
		}
		else{
			return 0;
		}
	}

}
