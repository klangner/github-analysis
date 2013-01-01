package com.matrobot.gha.archive.event;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;


public class FileArchiveReader implements Iterator<EventRecord>{

	private List<EventRecord> records;
	private Iterator<EventRecord> iterator;
	private String filename;
	
	
	/**
	 * Init reader from file path
	 * @param filePath
	 * @throws IOException 
	 */
	public FileArchiveReader(String filePath) throws IOException{
	
		filename = filePath;

		InputStream gzipStream = new GZIPInputStream(new FileInputStream(filePath));
		initContent(gzipStream);
	}
	
	
	/**
	 * Init reader from stream
	 * @param inputStream
	 * @throws IOException 
	 */
	public FileArchiveReader(InputStream inputStream) throws IOException{

		initContent(inputStream);
	}


	private void initContent(InputStream inputStream) throws IOException{

		Gson gson = EventRecord.getGson();
		JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
		reader.setLenient(true);
	    records = new ArrayList<EventRecord>();
	    try{
		    while (reader.hasNext() && reader.peek() != JsonToken.END_DOCUMENT) {
		    	EventRecord record = gson.fromJson(reader, EventRecord.class);
		        records.add(record);
		    }
	    }
	    catch(JsonSyntaxException e){
	    	System.out.println("File: " + filename);
	    	int index = records.size()-1;
	    	if(index < 0){
	    		System.out.println("first record problem");
	    	}
	    	else{
	    		System.out.println(records.get(records.size()-1));
	    	}
	    	System.err.println(e);
	    }
	    catch(EOFException e){
	    	System.out.println("EOF in : " + filename);
	    	System.err.println(e);
	    }
	    
	    reader.close();
	    iterator = records.iterator();
	}


	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}


	@Override
	public EventRecord next() {
		if(iterator.hasNext()){
			return iterator.next();
		}
		else{
			return null;
		}
	}


	@Override
	public void remove() {
	}

}


