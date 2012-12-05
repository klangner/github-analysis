package com.matrobot.gha.app.repo;

import java.io.IOException;
import java.util.Vector;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import com.matrobot.gha.app.Settings;
import com.matrobot.gha.dataset.repo.RepositoryDatasetList;
import com.matrobot.gha.dataset.repo.RepositoryRecord;
import com.matrobot.gha.features.RepositoryFeatures;

public class CorrelationApp{

	private RepositoryDatasetList datasets = new RepositoryDatasetList();
	
	public CorrelationApp(String firstPath, String secondPath, String thirdPath) throws IOException {
		
		datasets.addFromFile(firstPath);
		datasets.addFromFile(secondPath);
		datasets.addFromFile(thirdPath);

    }

	
	public double calculateCorrelationCoff(){
		
		Vector<Double> x = new Vector<Double>();
		Vector<Double> y = new Vector<Double>();
		
		for(RepositoryRecord record : datasets.getDataset(1).values()){
			RepositoryRecord nextRecord = datasets.findRepository(2, record.repository);
			RepositoryRecord prevRecord = datasets.findRepository(0, record.repository);
			
			if(prevRecord != null && nextRecord != null){
			
//				double expectedOld = RepositoryFeatures.getExpectedValue(prevRecord, record);
				double expectedNew = RepositoryFeatures.getExpectedValue(record, nextRecord);
				x.add((double) record.pushEventCount);
				y.add(expectedNew);
			}
		}
		
		double[] a = new double[x.size()];
		for(int i = 0; i < x.size(); i++){
			a[i] = x.get(i);
		}
		double[] b = new double[y.size()];
		for(int i = 0; i < y.size(); i++){
			b[i] = y.get(i);
		}
		
		double covariance = new PearsonsCorrelation().correlation(a, b);
		return covariance;
	}

	
   public static void main(String args[]) throws IOException {
	   
        CorrelationApp app = new CorrelationApp(
        		Settings.DATASET_PATH+"2012-8/", 
        		Settings.DATASET_PATH+"2012-9/",
        		Settings.DATASET_PATH+"2012-10/");
        
        System.out.println("Correlation=" + app.calculateCorrelationCoff());
    }
}