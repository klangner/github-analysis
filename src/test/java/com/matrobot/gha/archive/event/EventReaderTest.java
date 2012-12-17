package com.matrobot.gha.archive.event;

import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.junit.Test;

public class EventReaderTest {

	@Test
	public void getRepositoryId1() {
		
		URL url = getClass().getResource("testdata");
		EventReader reader = new EventReader(url.getPath());

		EventRecord data = reader.next();
		assertEquals("2012-04-01T00:00:00Z", data.created_at);
	}
}
