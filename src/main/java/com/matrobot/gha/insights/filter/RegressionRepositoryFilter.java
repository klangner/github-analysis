package com.matrobot.gha.insights.filter;

import java.io.IOException;

import com.matrobot.gha.archive.repo.RepositoryRecord;
import com.matrobot.gha.insights.ml.Dataset;
import com.matrobot.gha.insights.ml.Sample;

public class RegressionRepositoryFilter {

	private static final int MIN_ACTIVITY = 5;
	private RepositoryArchiveList datasets = new RepositoryArchiveList();
	
	
	public RegressionRepositoryFilter(String firstPath, String secondPath, String thirdPath) throws IOException{
		
		datasets.addFromFile(firstPath);
		datasets.addFromFile(secondPath);
		datasets.addFromFile(thirdPath);
	}
	
	/**
	 * Training data for single feature vector
	 * @return
	 */
	public Dataset getDataset() {
	
		Dataset dataset = new Dataset(2);
		
		for(RepositoryRecord record : datasets.getDataset(1).values()){
			RepositoryRecord nextRecord = datasets.findRepository(2, record.repoName); 
			RepositoryRecord prevRecord = datasets.findRepository(0, record.repoName); 
			if(record.pushEventCount > MIN_ACTIVITY && nextRecord.pushEventCount > MIN_ACTIVITY){
				Sample sample = createSample(record, nextRecord, prevRecord);
				dataset.addSample(sample);
			}
		}
		
		return dataset;
	}

	private Sample createSample(RepositoryRecord record,
			RepositoryRecord nextRecord, RepositoryRecord prevRecord) {
		Sample sample = new Sample(2);
		sample.name = record.repoName;
		sample.features[0] = record.pushEventCount;
		sample.features[1] =  prevRecord.pushEventCount;
		sample.output = nextRecord.pushEventCount;
		return sample;
	}
	
}
