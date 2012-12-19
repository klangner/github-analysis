package com.matrobot.gha.archive.repo;

import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.junit.Test;

import com.matrobot.gha.archive.event.EventReader;

public class OrderedRepoReaderTest {

	@Test
	public void sortInc() {
		
		URL url = getClass().getResource("../testdata");
		EventReader reader = new EventReader(url.getPath());
		RepositoryReader repoReader = new RepositoryReader(reader);
		OrderedRepoReader sortedReader = new OrderedRepoReader(repoReader);
		sortedReader.addField(OrderedRepoReader.SORT_BY_FORKS);

		RepositoryRecord record = sortedReader.next();

		assertEquals("jailuthra/page-about-me", record.repository);
	}

	@Test
	public void sortDec() {
		
		URL url = getClass().getResource("../testdata");
		EventReader reader = new EventReader(url.getPath());
		RepositoryReader repoReader = new RepositoryReader(reader);
		OrderedRepoReader sortedReader = new OrderedRepoReader(repoReader);
		sortedReader.addField(OrderedRepoReader.SORT_BY_FORKS_DESC);

		RepositoryRecord record = sortedReader.next();

		assertEquals("daniel-vsln/PPS1901", record.repository);
	}

}
