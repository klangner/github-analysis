package com.matrobot.gha.ml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GradientDescentLinearTest {

	@Test
	public void testRegression1() {
		Dataset dataset = new Dataset(2);
		double[][] input = {{1, 2}, {1, 3}};
		double[] test = {5, 6};
		
		dataset.addSample(input[0], 4);
		dataset.addSample(input[1], 5);
		GradientDescentLinear gradientDescent = new GradientDescentLinear();
		gradientDescent.setAlpha(0.001);
		gradientDescent.train(dataset);
		
		assertEquals(12, gradientDescent.predict(test), 0.01);
	}

}
