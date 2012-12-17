package com.matrobot.gha.archive.cmd;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import com.matrobot.gha.ICommand;
import com.matrobot.gha.Configuration;
import com.matrobot.gha.archive.EventRecord;
import com.matrobot.gha.archive.FolderArchiveReader;
import com.matrobot.gha.archive.repo.RepositoryRecord;

/**
 * Parse archive and create reports with repository info by months
 * 
 * @author Krzysztof Langner
 */
public class RepoActivityCmd implements ICommand{

	HashMap<String, RepositoryRecord> repos = new HashMap<String, RepositoryRecord>();
	
	
	@Override
	public void run(Configuration params) throws IOException {

		for(String path : params.getMonthFolders()){
			String datasetPath = path;
			System.out.println("Parse: " + datasetPath);
			parseFolder(datasetPath);
		}
		
		saveAsCSV(params.getOutputStream());
	}


	private void parseFolder(String datasetPath) throws IOException{
		
		FolderArchiveReader datasetReader = new FolderArchiveReader(datasetPath);
		EventRecord	recordData;
		
		while((recordData = datasetReader.readNextRecord()) != null){
			updateRepositoryData(recordData);
		}
	}

	
	private void updateRepositoryData(EventRecord event) {
	
		String url = event.getRepositoryId();
		if(url != null){
			
			RepositoryRecord record = repos.get(url);
			if(record == null){
				record = new RepositoryRecord();
				record.repository = url;
			}

			if(event.isCreateRepository()){
				record.isNew = true;
			}
			else if(event.type.equals("PushEvent")){
				addPushEventToRepository(event, record);
			}
			else if(event.type.equals("IssuesEvent")){
				record.issueOpenEventCount += 1;
			}
			else if(event.type.equals("ForkEvent")){
				record.forkEventCount += 1;
			}
			

			record.eventCount += 1;
			repos.put(url, record);
			
		}
	}

	private void addPushEventToRepository(EventRecord event, RepositoryRecord record) {
		
		if(event.payload.size > 0){
			record.pushEventCount += 1;
			for(String committer : event.getCommitters()){
				record.committers.add(committer);
				record.community.add(committer);
			}
		}
	}

	
	private void saveAsCSV(PrintStream printStream) throws UnsupportedEncodingException, IOException {
		
		printStream.print(RepositoryRecord.getCSVHeaders());
		for(RepositoryRecord record : repos.values()){
			printStream.print(record.toCSV());
		}
	}
}
