package com.matrobot.gha.archive.user;

import static org.junit.Assert.assertNull;

import java.net.URL;

import org.junit.Test;

import com.matrobot.gha.archive.event.EventReader;

public class FilterUserReaderTest {

	@Test
	public void getEventCount() {
		
		final int minActivity = 5;
		URL url = getClass().getResource("../testdata");
		EventReader reader = new EventReader(url.getPath());
		IUserReader repoReader = new UserReader(reader);
		FilteredUserReader filteredReader = new FilteredUserReader(repoReader);
		filteredReader.setMinActivity(minActivity);

		UserRecord record;
		while((record = filteredReader.next()) != null){
			if(record.eventCount < minActivity){
				break;
			}
		}
		
		assertNull(record);
	}
}
