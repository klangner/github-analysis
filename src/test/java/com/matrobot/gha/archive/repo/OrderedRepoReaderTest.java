package com.matrobot.gha.archive.repo;

import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.junit.Test;

import com.matrobot.gha.archive.event.EventReader;

public class OrderedRepoReaderTest {

	@Test
	public void sortForksInc() {
		
		URL url = getClass().getResource("../testdata");
		EventReader reader = new EventReader(url.getPath());
		RepositoryReader repoReader = new RepositoryReader(reader);
		OrderedRepoReader sortedReader = new OrderedRepoReader(repoReader);
		sortedReader.setField(OrderedRepoReader.SORT_BY_FORKS);

		RepositoryRecord record = sortedReader.next();

		assertEquals("jailuthra/page-about-me", record.repoName);
	}

	@Test
	public void sortForksDec() {
		
		URL url = getClass().getResource("../testdata");
		EventReader reader = new EventReader(url.getPath());
		RepositoryReader repoReader = new RepositoryReader(reader);
		OrderedRepoReader sortedReader = new OrderedRepoReader(repoReader);
		sortedReader.setField(OrderedRepoReader.SORT_BY_FORKS_DESC);

		RepositoryRecord record = sortedReader.next();

		assertEquals("daniel-vsln/PPS1901", record.repoName);
	}

	@Test
	public void sortCommunityInc() {
		
		URL url = getClass().getResource("../testdata");
		EventReader reader = new EventReader(url.getPath());
		RepositoryReader repoReader = new RepositoryReader(reader);
		OrderedRepoReader sortedReader = new OrderedRepoReader(repoReader);
		sortedReader.setField(OrderedRepoReader.SORT_BY_FORKS);

		RepositoryRecord record = sortedReader.next();

		assertEquals("jailuthra/page-about-me", record.repoName);
	}

}
