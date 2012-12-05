package com.matrobot.gha.ml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GradientDescentTest {

	@Test
	public void testRegression1() {
		double[][] input = {{1, 2}, {1, 3}};
		double[] output = {4, 5};
		double[] test = {5, 6};
		
		GradientDescent gradientDescent = new GradientDescent();
		gradientDescent.setAlpha(0.001);
		gradientDescent.train(input, output);
		
		assertEquals(12, gradientDescent.predict(test), 0.01);
	}

}
