package com.matrobot.gha.features;

import com.matrobot.gha.dataset.repo.RepositoryRecord;

public class RepositoryActivityFeatures {

	private double[] features;
	
	public RepositoryActivityFeatures(RepositoryRecord currentRecord) {
		
		features = new double[1];
		features[0] = currentRecord.eventCount;
	}

	
	public double[] getValues(){
		return features;
	}

}
