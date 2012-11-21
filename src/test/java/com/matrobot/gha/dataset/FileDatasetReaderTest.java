package com.matrobot.gha.dataset;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class FileDatasetReaderTest {

	@Test
	public void testFirstRecord() throws IOException {
		InputStream inputStream = getClass().getResourceAsStream("testdata/2012-04-01-0.json.gz");
		FileDatasetReader reader = new FileDatasetReader(inputStream);
		
		DataRecord data = reader.readNextRecord();
		assertEquals("2012-04-01T00:00:00Z", data.created_at);
	}


	@Test
	public void testRecordCount() throws IOException {
		InputStream inputStream = getClass().getResourceAsStream("testdata/2012-04-01-0.json.gz");
		FileDatasetReader reader = new FileDatasetReader(inputStream);
		
		int count = 0;
		while(reader.readNextRecord() != null){
			count ++;
		}
		
		assertEquals(7981, count);
	}


	@Test
	public void testRecordCount2() throws IOException {
		InputStream inputStream = getClass().getResourceAsStream("testdata/2012-10-13-0.json.gz");
		FileDatasetReader reader = new FileDatasetReader(inputStream);
		
		int count = 0;
		while(reader.readNextRecord() != null){
			count ++;
		}
		
		assertEquals(3086, count);
	}

}
