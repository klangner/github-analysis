package com.matrobot.gha.archive.user;

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


public class UserRecord {

	public String name;
	public int eventCount = 0;
	public int pushEventCount = 0;
	
	/**
	 * Load user.json
	 */
	public static HashMap<String, UserRecord> loadData(String filePath) throws IOException{
		
		List<UserRecord> rows = new ArrayList<UserRecord>();
		Gson gson = new Gson();
		Type datasetType = new TypeToken<Collection<UserRecord>>(){}.getType();

		FileInputStream fis = new FileInputStream(filePath+"users.json");
		Reader reader = new InputStreamReader(fis, "UTF-8");
		rows = gson.fromJson(reader, datasetType);
		
		reader.close();

		HashMap<String, UserRecord> dataset = new HashMap<String, UserRecord>();
		for(UserRecord row : rows){
			dataset.put(row.name, row);
		}
		
		return dataset;
	}
	
	
}
