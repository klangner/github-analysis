package com.matrobot.gha.archive.repo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RepositoryRecord {

	public String repoName;
	public int eventCount = 0;
	public int pushEventCount = 0;
	public int issueOpenEventCount = 0;
	public int forkEventCount = 0;
	public int openedPullCount = 0;
	public int closedPullCount = 0;
	public boolean isNew = false;
	public boolean isFork = false;
	
	public Set<String> committers = new HashSet<String>();
	public Set<String> community = new HashSet<String>();
	
	
	public RepositoryRecord(String name) {
		repoName = name;
	}

	
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
			dataset.put(row.repoName, row);
		}
		
		return dataset;
	}
	
	/**
	 * @return Header for CSV file
	 */
	public static String getCSVHeaders(){
		return "name,push_count,event_count,committer_count,fork_count,community_size,opened_pulls,closed_pulls";
	}
	
	/**
	 * @return CSV
	 */
	public String toCSV(){
		return repoName + "," + pushEventCount + "," + eventCount + "," + committers.size() + "," +
				forkEventCount + "," + community.size() + "," + openedPullCount + "," + closedPullCount; 
	}
}
