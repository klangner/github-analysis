package com.matrobot.gha.cmd;

import java.io.IOException;
import java.util.HashMap;

import com.matrobot.gha.dataset.DataRecord;
import com.matrobot.gha.dataset.FolderDatasetReader;
import com.matrobot.gha.dataset.IDatasetReader;

public class ActivityApp {

	private static final String DATASET_PATH = "/home/klangner/datasets/github/2012/03";
	
	
	public static void main(String[] args) throws IOException {

		HashMap<String, Integer> repos = new HashMap<String, Integer>();
		IDatasetReader datasetReader = new FolderDatasetReader(DATASET_PATH);
		DataRecord	recordData;
		long time = System.currentTimeMillis();
		
		while((recordData = datasetReader.readNextRecord()) != null){
			
			if(recordData.repo != null){
				Integer value = repos.get(recordData.repo.url);
				if(value != null){
					value += 1;
				}
				else{
					value = 1;
				}
				repos.put(recordData.repo.url, value);
			}
		}
		
		time = (System.currentTimeMillis()-time)/1000;
		System.out.println("Found Count: " + repos.size() + " in: " + time + "sec.");
	}

}
