package com.matrobot.gha.dataset;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;


/**
    Sample data:
        {
        "created_at": "2012-04-01T00:00:00Z",
        "payload": {
            "ref_type": "repository",
            "ref": null,
            "description": "a project with ROR",
            "master_branch": "master"
        },
        "repo": {
            "id": 3889255,
            "url": "https://api.github.dev/repos/azonwan/rable",
            "name": "azonwan/rable"
        },
        "type": "CreateEvent",
        "public": true,
        "actor": {
            "avatar_url": "https://secure.gravatar.com/avatar/ca087d20dcf8cb24426c3e816adcdda7?d=http://github.dev%2Fimages%2Fgravatars%2Fgravatar-user-420.png",
            "gravatar_id": "ca087d20dcf8cb24426c3e816adcdda7",
            "id": 830226,
            "url": "https://api.github.dev/users/azonwan",
            "login": "azonwan"
        },
        "id": "1536460828"
    }
 */
public class FileDatasetReader implements Iterator<DataRecord>{

	private List<DataRecord> records;
	private Iterator<DataRecord> iterator;
//	private String filename;
	
	
	/**
	 * Init reader from file path
	 * @param filePath
	 * @throws IOException 
	 */
	public FileDatasetReader(String filePath) throws IOException{
	
//		filename = filePath;
		initContent(new FileInputStream(filePath));
	}
	
	
	/**
	 * Init reader from stream
	 * @param inputStream
	 * @throws IOException 
	 */
	public FileDatasetReader(InputStream inputStream) throws IOException{

		initContent(inputStream);
	}


	private void initContent(InputStream inputStream) throws IOException{
		
		Gson gson = new Gson();
		InputStream gzipStream = new GZIPInputStream(inputStream);
		JsonReader reader = new JsonReader(new InputStreamReader(gzipStream, "UTF-8"));
		reader.setLenient(true);
	    records = new ArrayList<DataRecord>();
	    while (reader.hasNext() && reader.peek() != JsonToken.END_DOCUMENT) {
	    	DataRecord record = gson.fromJson(reader, DataRecord.class);
	        records.add(record);
	    }
	    
	    reader.close();
	    iterator = records.iterator();
	}


	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}


	@Override
	public DataRecord next() {
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


