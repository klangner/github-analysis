package com.matrobot.gha.archive.cmd;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.matrobot.gha.Configuration;
import com.matrobot.gha.ICommand;
import com.matrobot.gha.archive.event.EventReader;
import com.matrobot.gha.archive.event.EventRecord;
import com.matrobot.gha.archive.event.IEventReader;
import com.matrobot.gha.archive.repo.RepositoryRecord;

/**
 * Find repository state by going back in history events.
 * E.g.
 * Find number of forks in 2011-2 by counting back from 
 * 
 * @author Krzysztof Langner
 */
public class RepoHistoryCmd implements ICommand{

	private HashMap<String, RepositoryRecord> repos = new HashMap<String, RepositoryRecord>();
	private List<RepositoryRecord> estimatedRepos = new ArrayList<RepositoryRecord>();
	
	
	@Override
	public void run(Configuration params) throws IOException {

		IEventReader reader;
		
		System.out.println("Looking for active repositories in the first month");
		List<String> months = params.getMonthFolders();
		reader = new EventReader(months.get(0));
		months.remove(0);
		prepareRepos(reader);
		System.out.println("Found " + repos.size() + " repositories");
		
		reader = new EventReader(months);
		System.out.println("Estimating number of forks");
		estimateForks(reader);
		
		sortByForks();
		
		System.out.println("Saving data");
		saveAsCSV(params.getOutputStream());
		System.out.println("Done");
	}


	/**
	 * Find all active repos.
	 */
	private void prepareRepos(IEventReader reader) {

		EventRecord	event;
		while((event = reader.next()) != null){
			String repoName = event.getRepositoryId(); 
			if(repoName != null){
				RepositoryRecord repoRecord = repos.get(repoName);
				if(repoRecord == null){
					repoRecord = new RepositoryRecord(repoName);
					repos.put(repoName, repoRecord);
				}
				
				if(event.type.equals("ForkEvent")){
					repoRecord.forkEventCount ++;
				}
			}
		}
	}


	private void estimateForks(IEventReader reader) {
		
		EventRecord	event;
		while((event = reader.next()) != null){
			String repoName = event.getRepositoryId(); 
			RepositoryRecord repoRecord = repos.get(repoName);
			if(repoRecord != null){
				if(event.repository != null){
					estimateRepoFork(event, repoRecord);
				}
				else if(event.type.equals("ForkEvent")){
					repoRecord.forkEventCount ++;
				}
			}
		}
	}


	private void estimateRepoFork(EventRecord event, RepositoryRecord repoRecord) {

		repoRecord.forkEventCount = event.repository.forks - repoRecord.forkEventCount;
		repos.remove(repoRecord.repoName);
		estimatedRepos.add(repoRecord);
	}


	private void sortByForks() {
		
		Comparator<RepositoryRecord> cmp = new Comparator<RepositoryRecord>() {
			public int compare(RepositoryRecord o1, RepositoryRecord o2) {
				return o2.forkEventCount-o1.forkEventCount;
			}
		};
		
		Collections.sort(estimatedRepos, cmp);
	}


	private void saveAsCSV(PrintStream printStream) throws IOException{
		
		printStream.println("repository,forks");
		for(RepositoryRecord record : estimatedRepos){
			printStream.println(record.repoName + "," + record.forkEventCount);
		}
		
	}

	
	/**
	 * for local testing
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		Configuration params = new Configuration("configs/forks.yaml");
		
		RepoHistoryCmd app = new RepoHistoryCmd();
		app.run(params);
	}
	
}
