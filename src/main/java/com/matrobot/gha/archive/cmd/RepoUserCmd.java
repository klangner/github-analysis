package com.matrobot.gha.archive.cmd;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import com.matrobot.gha.Configuration;
import com.matrobot.gha.ICommand;
import com.matrobot.gha.archive.event.EventReader;
import com.matrobot.gha.archive.event.FilteredEventReader;
import com.matrobot.gha.archive.event.IEventReader;
import com.matrobot.gha.archive.repouser.IRepoUserReader;
import com.matrobot.gha.archive.repouser.RepoUserReader;
import com.matrobot.gha.archive.repouser.RepoUserRecord;

/**
 * Parse archive and create reports with repository-user info
 * 
 * @author Krzysztof Langner
 */
public class RepoUserCmd implements ICommand{

	
	
	@Override
	public void run(Configuration params) throws IOException {

		IEventReader eventReader = createEventReader(params);
		IRepoUserReader reader = createRepoReader(params, eventReader);
		saveAsCSV(reader, params);
	}

	/**
	 * Filter by repository if repository param provided
	 */
	private IEventReader createEventReader(Configuration params) {
		IEventReader eventReader = new EventReader(params.getMonthFolders());
		if(params.getRepositoryName() != null){
			FilteredEventReader filteredEventReader = new FilteredEventReader(eventReader);
			filteredEventReader.setRepoName(params.getRepositoryName());
			eventReader = filteredEventReader;
		}
		return eventReader;
	}

	
	/**
	 * Create repository reader. 
	 * Add:
	 * - min activity filter
	 * - ordered reader
	 */
	private IRepoUserReader createRepoReader(Configuration params, IEventReader eventReader) {
		IRepoUserReader reader = new RepoUserReader(eventReader);
		return reader;
	}

	
	private void saveAsCSV(IRepoUserReader reader, Configuration params) throws UnsupportedEncodingException, IOException {

		PrintStream output = params.getOutputStream();
		int minActivity = params.getMinActivity();
		output.println(RepoUserRecord.getCSVHeaders());
		RepoUserRecord record;
		while((record = reader.next()) != null){
			if(record.eventCount > minActivity){
				output.println(record.toCSV());
			}
		}
	}

	
	/**
	 * Export data for matrobot.com website
	 */
	public static void main(String[] args) throws IOException {

		RepoUserCmd app = new RepoUserCmd();
		Configuration params = new Configuration("configs/export_repouser.yaml");
		
		app.run(params);
	}
	
}
