package com.matrobot.gha.archive.repo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;

import org.junit.Test;

import com.matrobot.gha.archive.event.EventReader;

public class RepositoryReaderTest {

	@Test
	public void testEventCount() {
		
		URL url = getClass().getResource("../testdata");
		EventReader reader = new EventReader(url.getPath());
		RepositoryReader repoReader = new RepositoryReader(reader);

		RepositoryRecord record;
		while((record = repoReader.next()) != null){
			if(record.repoName.equals("rails/rails")){
				break;
			}
		}
		
		assertNotNull(record);
		assertEquals(213, record.eventCount);
	}

	
	@Test
	public void testPullOpened() {
		
		URL url = getClass().getResource("../testdata");
		EventReader reader = new EventReader(url.getPath());
		RepositoryReader repoReader = new RepositoryReader(reader);

		RepositoryRecord record;
		while((record = repoReader.next()) != null){
			if(record.repoName.equals("libgdx/libgdx")){
				break;
			}
		}
		
		assertNotNull(record);
		assertEquals(1, record.openedPullCount);
	}

	
	@Test
	public void testPullClosed() {
		
		URL url = getClass().getResource("../testdata");
		EventReader reader = new EventReader(url.getPath());
		RepositoryReader repoReader = new RepositoryReader(reader);

		RepositoryRecord record;
		while((record = repoReader.next()) != null){
			if(record.repoName.equals("vodik/powersave")){
				break;
			}
		}
		
		assertNotNull(record);
		assertEquals(2, record.closedPullCount);
	}

	@Test
	public void testRepoMetadata() {
		
		URL url = getClass().getResource("../testdata");
		EventReader reader = new EventReader(url.getPath());
		RepositoryReader repoReader = new RepositoryReader(reader);

		RepositoryRecord record;
		while((record = repoReader.next()) != null){
			if(record.repoName.equals("rails/rails")){
				break;
			}
		}
		
		assertNotNull(record);
		assertEquals("Ruby", record.language);
		assertEquals("http://rubyonrails.org", record.homepage);
		assertEquals("2008-04-10", record.createdAt);
	}

	
}
