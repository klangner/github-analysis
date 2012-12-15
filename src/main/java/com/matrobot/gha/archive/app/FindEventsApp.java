package com.matrobot.gha.archive.app;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Properties;

import com.matrobot.gha.ICommand;
import com.matrobot.gha.ParamParser;
import com.matrobot.gha.archive.EventRecord;
import com.matrobot.gha.archive.FolderArchiveReader;
import com.matrobot.gha.archive.repo.RepositoryRecord;

public class FindEventsApp implements ICommand{

	private String datasetPath;
	HashMap<String, RepositoryRecord> repos = new HashMap<String, RepositoryRecord>();
	private Writer writer;
	
	
	public FindEventsApp(){
	
	}
		
	public FindEventsApp(int year, int month) throws IOException{
		
		Properties prop = new Properties();
		prop.load(new FileInputStream("config.properties"));
		String outputFilename = prop.getProperty("data_path") + "events.csv";
		initWriter(outputFilename);
		datasetPath = prop.getProperty("data_path") + year + "-" + month; 
	}


	@Override
	public void run(ParamParser params) throws IOException {

		initWriter("events.csv");
		
		for(String path : params.getMonthFolders()){
			datasetPath = path;
			System.out.println("Parse: " + datasetPath);
			findEventByRepositoryName(params.getRepositoryName());
		}

		close();
	}


	private void close() throws IOException {
		writer.close();
	}


	private void initWriter(String outputFilename) throws UnsupportedEncodingException, IOException {
		
		FileOutputStream fos = new FileOutputStream(outputFilename, false);
		writer = new OutputStreamWriter(fos, "UTF-8");
		writer.write(EventRecord.getCSVHeaders());
	}

	
	protected void findEventByRepositoryName(String name) throws IOException{
		
		FolderArchiveReader datasetReader = new FolderArchiveReader(datasetPath);
		EventRecord	record;
		
		while((record = datasetReader.readNextRecord()) != null){

			String repoId = record.getRepositoryId(); 
			if(repoId != null && repoId.equals(name)){
				writer.write(record.toCSV());
			}
		}
	}

	
	protected void findEventByUser(String name) throws IOException{
		
		FolderArchiveReader datasetReader = new FolderArchiveReader(datasetPath);
		EventRecord	record;
		
		while((record = datasetReader.readNextRecord()) != null){
			
			if(record.getCommitters().contains(name)){
				writer.write(record.toCSV());
			}
		}
	}

	
	public static void main(String[] args) throws IOException {

		FindEventsApp app = new FindEventsApp(2011, 6);
		app.findEventByRepositoryName("rubinius/rubinius");
		app.close();
	}
}
