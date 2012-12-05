package com.matrobot.gha.features;

import com.matrobot.gha.dataset.repo.RepositoryRecord;

public class RepositoryFeatures {

	
	public static double[] getFeatures(RepositoryRecord currentRecord) {
		
		double[] features = new double[1];
		features[0] = currentRecord.pushEventCount;
		
		return features;
	}

	
	/**
	 * Positive value is when activity increases
	 * 
	 * @param currentActivity
	 * @param nextActivity
	 * @return
	 */
	public static double getExpectedValue(RepositoryRecord currentRecord, RepositoryRecord nextRecord){
		
		if(nextRecord.pushEventCount >= currentRecord.pushEventCount){
			return 1;
		}
		else{
			return 0;
		}
	}

}
