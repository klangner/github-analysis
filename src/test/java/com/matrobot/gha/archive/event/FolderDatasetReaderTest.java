package com.matrobot.gha.archive.event;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URL;

import org.junit.Test;

import com.matrobot.gha.archive.event.EventRecord;
import com.matrobot.gha.archive.event.FolderArchiveReader;

public class FolderDatasetReaderTest {

	@Test
	public void testFirstRecord() throws IOException {
		URL url = getClass().getResource("../testdata");
		FolderArchiveReader reader = new FolderArchiveReader(url.getPath());
		
		EventRecord data = reader.readNextRecord();
		assertEquals("2012-04-01T00:00:00Z", data.getCreatedAt());
	}


	@Test
	public void testRecordCount() throws IOException {
		URL url = getClass().getResource("../testdata");
		FolderArchiveReader reader = new FolderArchiveReader(url.getPath());
		
		int count = 0;
		while(reader.readNextRecord() != null){
			count ++;
		}
		
		assertEquals(11067, count);
	}

}
