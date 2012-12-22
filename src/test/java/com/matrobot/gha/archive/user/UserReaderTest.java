package com.matrobot.gha.archive.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;

import org.junit.Test;

import com.matrobot.gha.archive.event.EventReader;

public class UserReaderTest {

	private static final String USER_NAME = "tenderlove";

	
	@Test
	public void getEventCount() {
		
		URL url = getClass().getResource("../testdata");
		EventReader eventReader = new EventReader(url.getPath());
		UserReader reader = new UserReader(eventReader);

		UserRecord record;
		while((record = reader.next()) != null){
			if(USER_NAME.equals(record.name)){
				break;
			}
		}
		
		assertNotNull(record);
		assertEquals(211, record.eventCount);
	}
}
