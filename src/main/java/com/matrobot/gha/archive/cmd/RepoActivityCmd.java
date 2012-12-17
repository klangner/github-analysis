package com.matrobot.gha.archive.cmd;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.matrobot.gha.Configuration;
import com.matrobot.gha.ICommand;
import com.matrobot.gha.archive.event.EventRecord;
import com.matrobot.gha.archive.event.FolderArchiveReader;
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
			parseFolder(datasetPath);
		}
		
		List<RepositoryRecord> records = new ArrayList<RepositoryRecord>(repos.values());
		if(params.getOrderBy() != null){
			Collections.sort(records, getComparator(params.getOrderBy()));
		}

		saveAsCSV(records, params.getOutputStream());
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


	private void saveAsCSV(Collection<RepositoryRecord> records, PrintStream printStream) throws UnsupportedEncodingException, IOException {
		
		printStream.print(RepositoryRecord.getCSVHeaders());
		for(RepositoryRecord record : records){
			printStream.print(record.toCSV());
		}
	}

	
	/**
	 * for local testing
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		Configuration params = new Configuration(args[0]);
		
		RepoActivityCmd app = new RepoActivityCmd();
		app.run(params);
	}
	
}
