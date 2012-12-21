package com.matrobot.gha.archive.repotimeline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.matrobot.gha.archive.repotimeline.RepoTimeline;

public class TimelineRepoTest {

	@Test
	public void testRepoName() {
		
		RepoTimeline record = new RepoTimeline("ala");
		assertEquals("ala", record.getRepoName());
	}

	
	@Test
	public void testIncrementSeries() {
		
		RepoTimeline series = new RepoTimeline("ala");
		
		series.addDataPoint("1");
		series.addDataPoint("2");
		series.incrementLastPointValue();
		int[] values = series.getDataPoints();
		
		assertNotNull(values);
		assertEquals(2, values.length);
		assertEquals(1, values[1]);
	}
}
