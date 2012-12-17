package com.matrobot.gha.archive.cmd;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;

import com.matrobot.gha.Configuration;
import com.matrobot.gha.ICommand;
import com.matrobot.gha.archive.event.EventRecord;
import com.matrobot.gha.archive.event.FolderArchiveReader;
import com.matrobot.gha.archive.repo.RepositoryMonthlyActivity;

/**
 * Parse archive and create reports with repository info by months
 * 
 * @author Krzysztof Langner
 */
public class RepoActivityTimeSeriesCmd implements ICommand{

	private HashMap<String, RepositoryMonthlyActivity> repos = new HashMap<String, RepositoryMonthlyActivity>();
	private boolean allowNewRepository;
	private int counter;
	
	
	@Override
	public void run(Configuration params) throws IOException {

		allowNewRepository = true;
		counter = 0;
		for(String path : params.getMonthFolders()){
			String datasetPath = path;
			System.out.println("Parse: " + datasetPath);
			parseFolder(datasetPath);
			allowNewRepository = false;
			counter ++;
		}
		
		saveAsCSV(repos.values(), params.getOutputStream());
	}


	private void parseFolder(String datasetPath) throws IOException{
		
		FolderArchiveReader datasetReader = new FolderArchiveReader(datasetPath);
		EventRecord	recordData;
		
		while((recordData = datasetReader.readNextRecord()) != null){
			updateRepositoryData(recordData);
		}
		
		for(RepositoryMonthlyActivity record : repos.values()){
			record.closeMonth();
		}
	}

	
	private void updateRepositoryData(EventRecord event) {
	
		String url = event.getRepositoryId();
		if(url != null){
			
			RepositoryMonthlyActivity record = repos.get(url);
			if(record == null){
				if(!allowNewRepository){
					return;
				}
				
				record = new RepositoryMonthlyActivity(url);
			}

			if(event.type.equals("PushEvent")){
				record.incLastMonth();
			}

			repos.put(url, record);
		}
	}

	
	private void saveAsCSV(Collection<RepositoryMonthlyActivity> records, PrintStream printStream) {

		printStream.print("repository");
		for(int i = 0; i < counter; i++){
			printStream.print(", month" + (i+1));
		}
		printStream.print("\n");
		
		for(RepositoryMonthlyActivity record : records){
			printStream.print(record.toCSV());
		}
		
	}


	/**
	 * for local testing
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		Configuration params = new Configuration("configs/repos2.yaml");
		
		RepoActivityTimeSeriesCmd app = new RepoActivityTimeSeriesCmd();
		app.run(params);
	}
	
}
