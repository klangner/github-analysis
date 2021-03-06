package com.matrobot.gha.archive.event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EventReader implements IEventReader{

	private List<String> folders;
	private FolderArchiveReader archiveReader;
	private int folderIndex = 0;
	
	
	public EventReader(List<String> folders){
		this.folders = folders;
		prepareNextDataset();
	}
	
	
	public EventReader(String folder) {
		folders = new ArrayList<String>();
		folders.add(folder);
		prepareNextDataset();
	}


	@Override
	public EventRecord next(){

		EventRecord record = null;
		
		if(archiveReader != null){
			record = archiveReader.readNextRecord();
			if(record == null){
				prepareNextDataset();
				record = next();
			}
		}
		
		return record;
	}


	private void prepareNextDataset() {

		if(folderIndex < folders.size()){
			try {
				archiveReader = new FolderArchiveReader(folders.get(folderIndex));
				folderIndex++;
			} catch (IOException e) {
				e.printStackTrace();
				archiveReader = null;
			}
		}
		else{
			archiveReader = null;
		}
	}
}
