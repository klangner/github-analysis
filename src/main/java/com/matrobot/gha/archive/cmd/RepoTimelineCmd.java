package com.matrobot.gha.archive.cmd;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import com.matrobot.gha.Configuration;
import com.matrobot.gha.ICommand;
import com.matrobot.gha.archive.event.EventReader;
import com.matrobot.gha.archive.event.FilteredEventReader;
import com.matrobot.gha.archive.event.IEventReader;
import com.matrobot.gha.archive.repotimeline.ITimelineRepoReader;
import com.matrobot.gha.archive.repotimeline.RepoTimeline;
import com.matrobot.gha.archive.repotimeline.TimelineRepoReader;

/**
 * Parse archive and create reports with repository info by months
 * 
 * @author Krzysztof Langner
 */
public class RepoTimelineCmd implements ICommand{

	@Override
	public void run(Configuration params) throws IOException {

		IEventReader eventReader = createEventReader(params);
		ITimelineRepoReader reader = createRepoReader(params, eventReader);
		saveAsCSV(reader, params.getOutputStream());
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
	private ITimelineRepoReader createRepoReader(Configuration params, IEventReader eventReader) {
		
		ITimelineRepoReader repoReader = new TimelineRepoReader(eventReader);
		
		return repoReader;
	}
	
	
	private void saveAsCSV(ITimelineRepoReader reader, PrintStream printStream) throws UnsupportedEncodingException, IOException {

		boolean printHeaders = true;
		RepoTimeline record;
		while((record = reader.next()) != null){
			if(printHeaders){
				printStream.println(record.getCSVHeaders());
				printHeaders = false;
			}
			printStream.println(record.toCSV());
		}
	}


	/**
	 * for local testing
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		Configuration params = new Configuration("configs/repo_timeline.yaml");
		
		RepoTimelineCmd app = new RepoTimelineCmd();
		app.run(params);
	}
	
}
