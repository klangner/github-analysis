package com.matrobot.gha.archive.event;

import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.junit.Test;

public class EventFilterReaderTest {

	@Test
	public void getEventCount() {
		
		URL url = getClass().getResource("testdata");
		EventReader reader = new EventReader(url.getPath());
		EventFilterReader filterReader = new EventFilterReader(reader);
		filterReader.setRepoName("rails/rails");

		int count = 0;
		while(filterReader.next() != null){
			count ++;
		}
		
		assertEquals(213, count);
	}
}
