package com.matrobot.gha.ml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DatasetTest {

	@Test
	public void testSize() {
		double[] input = {1, 2};
		
		Dataset dataset = new Dataset(2);
		dataset.addSample(input, 1);
		
		assertEquals(1, dataset.size());
	}


	@Test
	public void normalize() {
		
		Sample sample;
		double[] input1 = {1, 2};
		double[] input2 = {-30, -50};
		double[] input3 = {10, 200};
		
		Dataset dataset = new Dataset(2);
		dataset.addSample(input1, 1);
		dataset.addSample(input2, 1);
		dataset.addSample(input3, 1);
		dataset.normalize();

		sample = dataset.getData().get(0);
		assertEquals(1.0/30, sample.features[0], 0.01);
		assertEquals(2.0/200, sample.features[1], 0.01);
	}
}
