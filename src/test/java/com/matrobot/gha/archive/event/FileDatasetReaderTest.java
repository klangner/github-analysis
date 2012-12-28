package com.matrobot.gha.archive.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import org.junit.Test;

import com.matrobot.gha.archive.event.EventRecord;
import com.matrobot.gha.archive.event.FileArchiveReader;

public class FileDatasetReaderTest {

	@Test
	public void testFirstRecord() throws IOException {
		InputStream inputStream = getClass().getResourceAsStream("../testdata/2012-04-01-0.json.gz");
		InputStream gzipStream = new GZIPInputStream(inputStream);
		FileArchiveReader reader = new FileArchiveReader(gzipStream);
		
		EventRecord data = reader.next();
		assertEquals("2012-04-01T00:00:00Z", data.created_at);
	}


	@Test
	public void testRepositoryId() throws IOException {
		InputStream inputStream = getClass().getResourceAsStream("../testdata/2012-04-01-0.json.gz");
		InputStream gzipStream = new GZIPInputStream(inputStream);
		FileArchiveReader reader = new FileArchiveReader(gzipStream);
		
		EventRecord data = reader.next();
		assertEquals("azonwan/rable", data.getRepositoryId());
	}


	@Test
	public void testEventType() throws IOException {
		InputStream inputStream = getClass().getResourceAsStream("../testdata/2012-04-01-0.json.gz");
		InputStream gzipStream = new GZIPInputStream(inputStream);
		FileArchiveReader reader = new FileArchiveReader(gzipStream);
		
		EventRecord data = reader.next();
		assertEquals("CreateEvent", data.type);
	}


	@Test
	public void testRecordCount() throws IOException {
		InputStream inputStream = getClass().getResourceAsStream("../testdata/2012-04-01-0.json.gz");
		InputStream gzipStream = new GZIPInputStream(inputStream);
		FileArchiveReader reader = new FileArchiveReader(gzipStream);
		
		int count = 0;
		while(reader.next() != null){
			count ++;
		}
		
		assertEquals(7981, count);
	}


	@Test
	public void testRecordCount2() throws IOException {
		InputStream inputStream = getClass().getResourceAsStream("../testdata/2012-10-13-0.json.gz");
		InputStream gzipStream = new GZIPInputStream(inputStream);
		FileArchiveReader reader = new FileArchiveReader(gzipStream);
		
		int count = 0;
		while(reader.next() != null){
			count ++;
		}
		
		assertEquals(3086, count);
	}


	@Test
	public void testCommitters1() throws IOException {
		InputStream inputStream = getClass().getResourceAsStream("../testdata/push_event-1.json");
		FileArchiveReader reader = new FileArchiveReader(inputStream);
		
		EventRecord record = reader.next();
		
		assertNotNull(record);
		
		Set<String> committers = record.getCommitters();
		assertEquals(2, committers.size());
		assertTrue(committers.contains("Max Medvedev"));
		assertTrue(committers.contains("ala"));
	}


	@Test
	public void testCommitters2() throws IOException {
		InputStream inputStream = getClass().getResourceAsStream("../testdata/push_event-2.json");
		FileArchiveReader reader = new FileArchiveReader(inputStream);
		
		EventRecord record = reader.next();
		
		assertNotNull(record);
		
		Set<String> committers = record.getCommitters();
		assertEquals(2, committers.size());
		assertTrue(committers.contains("Bill Krueger"));
		assertTrue(committers.contains("Rob Sayre"));
	}


	@Test
	public void testRepositoryForks() throws IOException {
		InputStream inputStream = getClass().getResourceAsStream("../testdata/push_event-1.json");
		FileArchiveReader reader = new FileArchiveReader(inputStream);
		
		EventRecord event = reader.next();
		
		assertNotNull(event);
		
		assertEquals(5, event.repository.forks);
	}


	@Test
	public void testPullAction() throws IOException {
		InputStream inputStream = getClass().getResourceAsStream("../testdata/pull_event-1.json");
		FileArchiveReader reader = new FileArchiveReader(inputStream);
		
		EventRecord event = reader.next();
		
		assertNotNull(event);
		
		assertEquals("opened", event.payload.action);
	}


	@Test
	public void testLanguage() throws IOException {
		InputStream inputStream = getClass().getResourceAsStream("../testdata/pull_event-1.json");
		FileArchiveReader reader = new FileArchiveReader(inputStream);
		
		EventRecord event = reader.next();
		
		assertNotNull(event);
		
		assertEquals("C", event.repository.language);
	}


	@Test
	public void testHomepage() throws IOException {
		InputStream inputStream = getClass().getResourceAsStream("../testdata/pull_event-1.json");
		FileArchiveReader reader = new FileArchiveReader(inputStream);
		
		EventRecord event = reader.next();
		
		assertNotNull(event);
		
		assertEquals("http://luke.dashjr.org/programs/bitcoin/files/bfgminer/", event.repository.homepage);
	}


	@Test
	public void testCreatedAt() throws IOException {
		InputStream inputStream = getClass().getResourceAsStream("../testdata/pull_event-1.json");
		FileArchiveReader reader = new FileArchiveReader(inputStream);
		
		EventRecord event = reader.next();
		
		assertNotNull(event);
		
		assertEquals("2012-04-26T04:29:32-07:00", event.repository.created_at);
	}

}
