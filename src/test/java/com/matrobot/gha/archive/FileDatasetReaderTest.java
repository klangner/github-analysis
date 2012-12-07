package com.matrobot.gha.archive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.junit.Test;

import com.matrobot.gha.archive.EventRecord;
import com.matrobot.gha.archive.FileArchiveReader;

public class FileDatasetReaderTest {

	@Test
	public void testFirstRecord() throws IOException {
		InputStream inputStream = getClass().getResourceAsStream("testdata/2012-04-01-0.json.gz");
		FileArchiveReader reader = new FileArchiveReader(inputStream);
		
		EventRecord data = reader.next();
		assertEquals("2012-04-01T00:00:00Z", data.created_at);
	}


	@Test
	public void testRepositoryId() throws IOException {
		InputStream inputStream = getClass().getResourceAsStream("testdata/2012-04-01-0.json.gz");
		FileArchiveReader reader = new FileArchiveReader(inputStream);
		
		EventRecord data = reader.next();
		assertEquals("azonwan/rable", data.getRepositoryId());
	}


	@Test
	public void testEventType() throws IOException {
		InputStream inputStream = getClass().getResourceAsStream("testdata/2012-04-01-0.json.gz");
		FileArchiveReader reader = new FileArchiveReader(inputStream);
		
		EventRecord data = reader.next();
		assertEquals("CreateEvent", data.type);
	}


	@Test
	public void testRecordCount() throws IOException {
		InputStream inputStream = getClass().getResourceAsStream("testdata/2012-04-01-0.json.gz");
		FileArchiveReader reader = new FileArchiveReader(inputStream);
		
		int count = 0;
		while(reader.next() != null){
			count ++;
		}
		
		assertEquals(7981, count);
	}


	@Test
	public void testRecordCount2() throws IOException {
		InputStream inputStream = getClass().getResourceAsStream("testdata/2012-10-13-0.json.gz");
		FileArchiveReader reader = new FileArchiveReader(inputStream);
		
		int count = 0;
		while(reader.next() != null){
			count ++;
		}
		
		assertEquals(3086, count);
	}


	@Test
	public void testCommitters() throws IOException {
		InputStream inputStream = getClass().getResourceAsStream("testdata/push_event-1.json.gz");
		FileArchiveReader reader = new FileArchiveReader(inputStream);
		
		EventRecord record = reader.next();
		
		assertNotNull(record);
		
		Set<String> committers = record.getCommitters();
		assertEquals(2, committers.size());
		assertTrue(committers.contains("Max Medvedev"));
		assertTrue(committers.contains("ala"));
	}

}
