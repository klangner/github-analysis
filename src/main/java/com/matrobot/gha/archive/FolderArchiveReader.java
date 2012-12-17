package com.matrobot.gha.archive;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.matrobot.gha.archive.event.EventRecord;



public class FolderArchiveReader{

	private List<String> filePaths = new ArrayList<String>();
	private Iterator<EventRecord> datasetReader;
			
			
	/**
	 * Init reader from given folder
	 * @param filePath
	 * @throws IOException 
	 */
	public FolderArchiveReader(String folder) throws IOException{
		
		initFileNames(folder);
		nextDatasetReader();
	}
	
	
	private void initFileNames(String folder) {

		File rootFolder = new File(folder);
		FilenameFilter filter = new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return (name.endsWith(".gz"));
			}
		};

		if(rootFolder.isDirectory()){
			List<File> files = Arrays.asList(rootFolder.listFiles(filter));
			Collections.sort(files);
			
			for(File file : files){
				if(file.isFile()){
					filePaths.add(file.getPath());
				}
			}
		}
	}


	private void nextDatasetReader() {

		if(filePaths.size() > 0){
			String path = filePaths.remove(0);
			try {
				datasetReader = new FileArchiveReader(path);
			} catch (IOException e) {
				e.printStackTrace();
				datasetReader = null;
			}
		}
	}


	public EventRecord readNextRecord(){
		
		EventRecord data = null;
		
		if(datasetReader != null){
		
			if(datasetReader.hasNext()){
				data = datasetReader.next();
			}
			else{
				nextDatasetReader();
				if(datasetReader != null){
					data = datasetReader.next();		
				}
			}
		}
		
		return data;
	}
}
