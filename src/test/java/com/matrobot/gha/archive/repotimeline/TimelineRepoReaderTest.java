package com.matrobot.gha.archive.repotimeline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;

import org.junit.Test;

import com.matrobot.gha.archive.event.EventReader;
import com.matrobot.gha.archive.repotimeline.RepoTimeline;
import com.matrobot.gha.archive.repotimeline.TimelineRepoReader;

public class TimelineRepoReaderTest {

	private static final String REPO_NAME = "rails/rails";

	
	@Test
	public void testSerieSize() {
		
		URL url = getClass().getResource("../testdata");
		EventReader reader = new EventReader(url.getPath());
		TimelineRepoReader repoReader = new TimelineRepoReader(reader);

		RepoTimeline record;
		while((record = repoReader.next()) != null){
			if(record.getRepoName().equals(REPO_NAME)){
				break;
			}
		}
		
		assertNotNull(record);
		int[] values = record.getDataPoints();
		assertEquals(2, values.length);
		assertEquals(1, values[0]);
		assertEquals(0, values[1]);
	}
}
