package com.matrobot.gha.archive.event;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.matrobot.gha.archive.event.EventRecord;

public class EventRecordTest {

	@Test
	public void getRepositoryId1() {
		
		EventRecord record = new EventRecord();
		record.repo = record.new Repo();
		record.repo.url = "https://api.github.com/repos/chiphogg/bestinbg";
		
		assertEquals("chiphogg/bestinbg", record.getRepositoryId());
	}


	@Test
	public void getRepositoryId2() {
		
		EventRecord record = new EventRecord();
		record.repository = record.new Repository();
		record.repository.url = "https://api.github.com/chiphogg/bestinbg";
		
		assertEquals("chiphogg/bestinbg", record.getRepositoryId());
	}
}
