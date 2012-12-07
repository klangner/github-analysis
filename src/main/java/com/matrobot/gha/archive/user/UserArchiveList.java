package com.matrobot.gha.archive.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserArchiveList {

	private List<HashMap<String, UserRecord>> datasets = 
			new ArrayList<HashMap<String,UserRecord>>();
	
	
	public void addFromFile(String filePath) throws IOException{
		
		HashMap<String, UserRecord> dataset = UserRecord.loadData(filePath);
		datasets.add(dataset);
	}


	public HashMap<String, UserRecord> getDataset(int index) {
		return datasets.get(index);
	}


	/**
	 * Find repository by name in the given dataset
	 * @param datasetIndex
	 * @param userName
	 * @return found object or new one if there is not repository record for given dataset.
	 */
	public UserRecord findRepository(int datasetIndex, String userName) {

		UserRecord user = datasets.get(datasetIndex).get(userName);
		if(user == null){
			user = new UserRecord();
		}
		
		return user;
	}


	public int size() {
		return datasets.size();
	}
}
