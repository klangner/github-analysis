package com.matrobot.gha.archive.repouser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;

import org.junit.Test;

import com.matrobot.gha.archive.event.EventReader;

public class RepoUserReaderTest {

	private static final String REPO_NAME = "rails/rails";
	private static final String USER_NAME = "tenderlove";

	
	@Test
	public void getEventCount() {
		
		URL url = getClass().getResource("../testdata");
		EventReader reader = new EventReader(url.getPath());
		RepoUserReader repoReader = new RepoUserReader(reader);

		RepoUserRecord record;
		String key = REPO_NAME + "#" + USER_NAME;
		while((record = repoReader.next()) != null){
			if(record.key.equals(key)){
				break;
			}
		}
		
		assertNotNull(record);
		assertEquals(211, record.eventCount);
	}
}
