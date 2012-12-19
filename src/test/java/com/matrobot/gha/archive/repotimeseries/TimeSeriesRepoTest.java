package com.matrobot.gha.archive.repotimeseries;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.matrobot.gha.archive.repotimeseries.RepoTimeSeries;

public class TimeSeriesRepoTest {

	@Test
	public void testRepoName() {
		
		RepoTimeSeries record = new RepoTimeSeries("ala");
		assertEquals("ala", record.getRepoName());
	}

	
	@Test
	public void testIncrementSeries() {
		
		RepoTimeSeries series = new RepoTimeSeries("ala");
		
		series.addDataPoint("1");
		series.addDataPoint("2");
		series.incrementLastPointValue();
		int[] values = series.getDataPoints();
		
		assertNotNull(values);
		assertEquals(2, values.length);
		assertEquals(1, values[1]);
	}
}
