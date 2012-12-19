package com.matrobot.gha.archive.cmd;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import com.matrobot.gha.Configuration;
import com.matrobot.gha.ICommand;
import com.matrobot.gha.archive.event.EventReader;
import com.matrobot.gha.archive.repo.IRepositoryReader;
import com.matrobot.gha.archive.repo.FilteredRepoReader;
import com.matrobot.gha.archive.repo.RepositoryReader;
import com.matrobot.gha.archive.repo.RepositoryRecord;

/**
 * Parse archive and create reports with repository info by months
 * 
 * @author Krzysztof Langner
 */
public class RepoActivityCmd implements ICommand{

	private IRepositoryReader reader;
	
	
	@Override
	public void run(Configuration params) throws IOException {

		EventReader eventReader = new EventReader(params.getMonthFolders());
		RepositoryReader repoReader = new RepositoryReader(eventReader);
		reader = repoReader;
		if(params.getMinActivity() > 0){
			FilteredRepoReader filteredReader = new FilteredRepoReader(reader);
			filteredReader.setMinActivity(params.getMinActivity());
			reader = filteredReader;
		}
		
		saveAsCSV(params.getOutputStream());
	}

/*
	private Comparator<RepositoryRecord> getComparator(String orderBy) {

		Comparator<RepositoryRecord> cmp;
		
		if(orderBy.equals("community_size")){
			cmp = new Comparator<RepositoryRecord>() {
				public int compare(RepositoryRecord o1, RepositoryRecord o2) {
					if(o1.community.size() == o2.community.size()){
						return 0;
					}
					else if(o1.community.size() > o2.community.size()){
						return 1;
					}
					else{
						return -1;
					}
				}
			};
		}
		else{
			cmp = new Comparator<RepositoryRecord>() {
				public int compare(RepositoryRecord o1, RepositoryRecord o2) {
					return o1.repository.compareTo(o2.repository);
				}
			};
		}
		
		return cmp;
	}
*/

	private void saveAsCSV(PrintStream printStream) throws UnsupportedEncodingException, IOException {
		
		printStream.print(RepositoryRecord.getCSVHeaders());
		RepositoryRecord record;
		while((record = reader.next()) != null){
			printStream.print(record.toCSV());
		}
	}

	
	/**
	 * for local testing
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		Configuration params = new Configuration("configs/export_repos.yaml");
		
		RepoActivityCmd app = new RepoActivityCmd();
		app.run(params);
	}
	
}
