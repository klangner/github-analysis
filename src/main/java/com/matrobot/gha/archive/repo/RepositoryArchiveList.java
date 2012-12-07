package com.matrobot.gha.archive.repo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RepositoryArchiveList {

	private List<HashMap<String, RepositoryRecord>> datasets = 
			new ArrayList<HashMap<String,RepositoryRecord>>();
	
	
	public void addFromFile(String filePath) throws IOException{
		
		HashMap<String, RepositoryRecord> dataset = RepositoryRecord.loadData(filePath);
		datasets.add(dataset);
	}


	public HashMap<String, RepositoryRecord> getDataset(int index) {
		return datasets.get(index);
	}


	/**
	 * Find repository by name in the given dataset
	 * @param datasetIndex
	 * @param repositoryName
	 * @return found object or new one if there is not repository record for given dataset.
	 */
	public RepositoryRecord findRepository(int datasetIndex, String repositoryName) {

		RepositoryRecord repository = datasets.get(datasetIndex).get(repositoryName);
		if(repository == null){
			repository = new RepositoryRecord();
		}
		
		return repository;
	}


	public int size() {
		return datasets.size();
	}
}
