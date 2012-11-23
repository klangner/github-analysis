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

public class ActivityDataset {

	/**
	 * Load activity.json
	 * 
	 * @param filename
	 * @throws IOException
	 */
	public HashMap<String, ActivityRecord> loadData(String filePath) throws IOException{
		
		List<ActivityRecord> rows = new ArrayList<ActivityRecord>();
		Gson gson = new Gson();
		Type datasetType = new TypeToken<Collection<ActivityRecord>>(){}.getType();

		FileInputStream fis = new FileInputStream(filePath+"activity.json");
		Reader reader = new InputStreamReader(fis, "UTF-8");
		rows = gson.fromJson(reader, datasetType);
		
		reader.close();

		HashMap<String, ActivityRecord> dataset = new HashMap<String, ActivityRecord>();
		for(ActivityRecord row : rows){
			dataset.put(row.repository, row);
		}
		
		return dataset;
	}
}
