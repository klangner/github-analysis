package com.matrobot.gha.app.parser;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.matrobot.gha.app.Settings;
import com.matrobot.gha.dataset.EventRecord;
import com.matrobot.gha.dataset.FolderDatasetReader;
import com.matrobot.gha.dataset.SummaryRecord;
import com.matrobot.gha.dataset.repo.RepositoryRecord;
import com.matrobot.gha.dataset.user.UserRecord;

public class RepositoryParserApp {

	private static final int MIN_ACTIVITY = 10;
	private String datasetPath;
	HashMap<String, RepositoryRecord> repos = new HashMap<String, RepositoryRecord>();
	HashMap<String, UserRecord> users = new HashMap<String, UserRecord>();
	private SummaryRecord info = new SummaryRecord();
	private int year;
	private int month;
	
	
	public RepositoryParserApp(int year, int month) throws IOException{
		
		this.year = year;
		this.month = month;
		datasetPath = Settings.DATASET_PATH + year + "-" + month; 
		parseFolder();
	}

	public RepositoryParserApp(String folder) throws IOException{
		
		datasetPath = Settings.DATASET_PATH + folder; 
		parseFolder();
	}

	private void parseFolder() throws IOException{
		
		FolderDatasetReader datasetReader = new FolderDatasetReader(datasetPath);
		EventRecord	recordData;
		
		info.eventCount = 0;
		info.newRepositoryCount = 0;
		while((recordData = datasetReader.readNextRecord()) != null){
			
			updateRepositoryData(recordData);
			updateUserData(recordData);
			info.eventCount ++;
		}
		
		info.repositoryCount = repos.size();
	}

	
	private void updateRepositoryData(EventRecord event) {
	
		String url = event.getRepositoryId();
		if(url != null){
			
			RepositoryRecord data = repos.get(url);
			if(data == null){
				data = new RepositoryRecord();
				data.repository = url;
			}

			if(event.isCreateRepository()){
				data.isNew = true;
				info.newRepositoryCount += 1;
			}
			else if(event.type.equals("PushEvent")){
				data.pushEventCount += 1;
			}
			else if(event.type.equals("IssuesEvent")){
				data.issueOpenEventCount += 1;
			}

			data.eventCount += 1;
			repos.put(url, data);
			
		}
	}

	
	private void updateUserData(EventRecord event) {
	
//		UserRecord user = users.get(event.actor.login);
//		if( user == null){
//			user = new UserRecord();
//			user.name = event.actor.login;
//		}
//		
//		if(event.type.equals("PushEvent")){
//			user.pushEventCount += 1;
//		}
//		
//		user.eventCount += 1;
//		users.put(user.name, user);
	}

	
	protected int getRepoCommiters() {

		int count = 0;
		for(RepositoryRecord record : repos.values()){
			if(record.commiters.size() > 1){
				count += 1;
				System.out.println(record.repository);
			}
		}
		
		return count;
	}

	public void saveAsJson() {
	
		FileWriter writer;
		String json;
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		try{
			writer = new FileWriter(datasetPath+"/repositories.json", false);
			json = gson.toJson(repos.values());
			writer.write(json);
			writer.close();
			
//			writer = new FileWriter(datasetPath+"/users.json");
//			json = gson.toJson(users.values());
//			writer.write(json);
//			writer.close();
//			
			writer = new FileWriter(datasetPath+"/summary.json");
			json = gson.toJson(info);
			writer.write(json);
			writer.close();
			
		}catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}
	}

	
	public void saveAsCSV() {
		
		try{
			String filename = "repositories-" + year + "-" + month + ".csv";
			FileOutputStream fos = new FileOutputStream(Settings.DATASET_PATH + filename, false);
			Writer writer = new OutputStreamWriter(fos, "UTF-8");
			writer.write("name,year,month,push_count\n");
			for(RepositoryRecord record : repos.values()){
				if(record.pushEventCount >= MIN_ACTIVITY){
					String line = record.repository + "," +
									year + "," + month  + "," +
									record.pushEventCount + "\n"; 
					writer.write(line);
				}
			}
			writer.close();
			
		}catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}
	}
	

	
	public static void main(String[] args) throws IOException {

		parseMonth(2012, 11);
		
		// Parse 2011
		for(int i = 9; i <= 12; i++){
//			parseMonth(2011, i);
		}

		// Parse 2012
		for(int i = 1; i <= 11; i++){
//			parseMonth(2012, i);
		}
	}

	
	private static void parseMonth(int year, int month) throws IOException {
		
		long time = System.currentTimeMillis();

		System.out.println("Dataset: " + year + "-" + month);
		RepositoryParserApp app = new RepositoryParserApp(year, month);
		app.saveAsJson();
		app.saveAsCSV();
		time = (System.currentTimeMillis()-time)/1000;
		System.out.println(	"Repos: " + app.repos.size() + 
							" Users: " + app.users.size() + 
							" Events: " + app.info.eventCount);
//		System.out.println("Repos with more then 1 commiter:" + app.getRepoCommiters());
		
		System.out.println("Parse time: " + time + "sec.");
		System.out.println();
	}
}
