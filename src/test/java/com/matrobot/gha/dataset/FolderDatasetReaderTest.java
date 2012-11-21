package com.matrobot.gha.dataset;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URL;

import org.junit.Test;

public class FolderDatasetReaderTest {

	@Test
	public void testFirstRecord() throws IOException {
		URL url = getClass().getResource("testdata");
		IDatasetReader reader = new FolderDatasetReader(url.getPath());
		
		DataRecord data = reader.readNextRecord();
		assertEquals("2012-04-01T00:00:00Z", data.created_at);
	}


	@Test
	public void testRecordCount() throws IOException {
		URL url = getClass().getResource("testdata");
		IDatasetReader reader = new FolderDatasetReader(url.getPath());
		
		int count = 0;
		while(reader.readNextRecord() != null){
			count ++;
		}
		
		assertEquals(11067, count);
	}

}
