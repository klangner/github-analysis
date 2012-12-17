package com.matrobot.gha.archive.app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.matrobot.gha.archive.FolderArchiveReader;
import com.matrobot.gha.archive.event.EventRecord;

public class RepoEventsApp {

	Properties prop = new Properties();
	private String repoName;
	List<EventRecord> events = new ArrayList<EventRecord>();
	private Set<String> excludeEventTypes = new HashSet<String>();
	
	
	public RepoEventsApp(String repoName) throws IOException{
		
		prop.load(new FileInputStream("config.properties"));
		this.repoName = repoName;
	}
	
	
	public void addExcludeType(String eventType){
		excludeEventTypes.add(eventType);
	}

	public void parseMonth(int year, int month) throws IOException{

		System.out.println("Parse: " + year + "-" + month);
		String datasetPath = prop.getProperty("data_path") + year + "-" + month; 
		FolderArchiveReader datasetReader = new FolderArchiveReader(datasetPath);
		EventRecord	event;
		
		while((event = datasetReader.readNextRecord()) != null){
			
			String name = event.getRepositoryId();
			if(repoName.equals(name) && !excludeEventTypes.contains(event.type)){
				events.add(event);
			}
		}
	}

	
	public void saveAsCSV() throws FileNotFoundException, UnsupportedEncodingException, IOException {
		
		Collections.sort(events, new Comparator<EventRecord>() {
			public int compare(EventRecord r1, EventRecord r2) {
				return r1.created_at.compareTo(r2.created_at);
			}
		} );
		
		String filename = repoName.replace('/', '-') + ".csv";
		FileOutputStream fos = new FileOutputStream(prop.getProperty("data_path") + filename, false);
		Writer writer = new OutputStreamWriter(fos, "UTF-8");
		writer.write("created_at, type, actor_login\n");
		for(EventRecord event : events){
			String line = event.created_at + "," + event.type + "," + 
					event.getActorLogin() + "\n"; 
			writer.write(line);
		}
		writer.close();
	}
	

	public static void main(String[] args) throws IOException {

		long time = System.currentTimeMillis();
		RepoEventsApp app = new RepoEventsApp("rubinius/rubinius");
		app.addExcludeType("WatchEvent");
		
//		app.parseMonth(2011, 12);
//		app.parseMonth(2012, 8);
		
		// Parse 2011
		for(int i = 2; i <= 12; i++){
			app.parseMonth(2011, i);
		}

		// Parse 2012
		for(int i = 1; i <= 11; i++){
			app.parseMonth(2012, i);
		}
		
		app.saveAsCSV();
		time = (System.currentTimeMillis()-time)/1000;
		System.out.println("Parse time: " + time + "sec.");
		System.out.println();
	}

}
