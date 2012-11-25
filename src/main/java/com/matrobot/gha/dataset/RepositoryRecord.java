package com.matrobot.gha.dataset;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RepositoryRecord {

	public String repository;
	public int activity = 0;
	
	
	/**
	 * Load activity.json
	 * 
	 * @param filename
	 * @throws IOException
	 */
	public static HashMap<String, RepositoryRecord> loadData(String filePath) throws IOException{
		
		List<RepositoryRecord> rows = new ArrayList<RepositoryRecord>();
		Gson gson = new Gson();
		Type datasetType = new TypeToken<Collection<RepositoryRecord>>(){}.getType();

		FileInputStream fis = new FileInputStream(filePath+"repositories.json");
		Reader reader = new InputStreamReader(fis, "UTF-8");
		rows = gson.fromJson(reader, datasetType);
		
		reader.close();

		HashMap<String, RepositoryRecord> dataset = new HashMap<String, RepositoryRecord>();
		for(RepositoryRecord row : rows){
			dataset.put(row.repository, row);
		}
		
		return dataset;
	}
	
}
