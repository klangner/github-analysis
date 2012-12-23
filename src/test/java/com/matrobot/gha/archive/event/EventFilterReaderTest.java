package com.matrobot.gha.archive.event;

import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.junit.Test;

public class EventFilterReaderTest {

	@Test
	public void testEventCount() {
		
		URL url = getClass().getResource("../testdata");
		EventReader reader = new EventReader(url.getPath());
		FilteredEventReader filterReader = new FilteredEventReader(reader);
		filterReader.setRepoName("rails/rails");

		int count = 0;
		while(filterReader.next() != null){
			count ++;
		}
		
		assertEquals(213, count);
	}

	@Test
	public void testActorFilter() {
		
		URL url = getClass().getResource("../testdata");
		EventReader reader = new EventReader(url.getPath());
		FilteredEventReader filterReader = new FilteredEventReader(reader);
		filterReader.setRepoName("rails/rails");
		filterReader.setActor("kylev");

		int count = 0;
		while(filterReader.next() != null){
			count ++;
		}
		
		assertEquals(1, count);
	}

	@Test
	public void testEventTypeFilter() {
		
		URL url = getClass().getResource("../testdata");
		EventReader reader = new EventReader(url.getPath());
		FilteredEventReader filterReader = new FilteredEventReader(reader);
		filterReader.setRepoName("rails/rails");
		filterReader.addEventType("PullRequestEvent");

		int count = 0;
		while(filterReader.next() != null){
			count ++;
		}
		
		assertEquals(210, count);
	}
}
