package com.matrobot.gha.archive.repo;

import static org.junit.Assert.assertNull;

import java.net.URL;

import org.junit.Test;

import com.matrobot.gha.archive.event.EventReader;

public class FilterRepoReaderTest {

	@Test
	public void getEventCount() {
		
		final int minActivity = 5;
		URL url = getClass().getResource("../testdata");
		EventReader reader = new EventReader(url.getPath());
		RepositoryReader repoReader = new RepositoryReader(reader);
		FilteredRepoReader filteredReader = new FilteredRepoReader(repoReader);
		filteredReader.setMinActivity(minActivity);

		RepositoryRecord record;
		while((record = filteredReader.next()) != null){
			if(record.eventCount < minActivity){
				break;
			}
		}
		
		assertNull(record);
	}
}
