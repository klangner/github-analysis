package com.matrobot.gha.archive.app;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.matrobot.gha.app.Settings;
import com.matrobot.gha.archive.EventRecord;
import com.matrobot.gha.archive.FolderArchiveReader;

public class RepoEventsApp {

	private String repoName;
	List<EventRecord> events = new ArrayList<EventRecord>();
	
	
	public RepoEventsApp(String repoName) throws IOException{
		
		this.repoName = repoName;
	}

	public void parseMonth(int year, int month) throws IOException{

		System.out.println("Parse: " + year + "-" + month);
		String datasetPath = Settings.DATASET_PATH + year + "-" + month; 
		FolderArchiveReader datasetReader = new FolderArchiveReader(datasetPath);
		EventRecord	event;
		
		while((event = datasetReader.readNextRecord()) != null){
			
			String name = event.getRepositoryId();
			if(repoName.equals(name)){
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
		FileOutputStream fos = new FileOutputStream(Settings.DATASET_PATH + filename, false);
		Writer writer = new OutputStreamWriter(fos, "UTF-8");
		writer.write("created_at, type, actor_email\n");
		for(EventRecord event : events){
			String line = event.created_at + "," + event.type + "," + 
					event.getActorEmail() + "\n"; 
			writer.write(line);
		}
		writer.close();
	}
	

	public static void main(String[] args) throws IOException {

		long time = System.currentTimeMillis();
		RepoEventsApp app = new RepoEventsApp("rubinius/rubinius");
		
//		app.parseMonth(2011, 12);
//		app.parseMonth(2012, 1);
		
		// Parse 2011
		for(int i = 3; i <= 12; i++){
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