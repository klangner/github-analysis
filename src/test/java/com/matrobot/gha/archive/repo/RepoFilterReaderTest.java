package com.matrobot.gha.archive.repo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;

import org.junit.Test;

import com.matrobot.gha.archive.event.EventReader;

public class RepoFilterReaderTest {

	private static final String REPO_NAME = "rails/rails";

	
	@Test
	public void getEventCount() {
		
		URL url = getClass().getResource("../testdata");
		EventReader reader = new EventReader(url.getPath());
		RepositoryReader repoReader = new RepositoryReader(reader);
		RepoFilterReader filteredReader = new RepoFilterReader(repoReader);

		RepositoryRecord record;
		while((record = filteredReader.next()) != null){
			if(record.repository.equals(REPO_NAME)){
				break;
			}
		}
		
		assertNotNull(record);
		assertEquals(213, record.eventCount);
	}
}
