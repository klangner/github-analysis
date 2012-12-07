package com.matrobot.gha.ml;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GradientDescentLogisticTest {

	@Test
	public void testRegression1() {
		Dataset dataset = new Dataset(2);
		double[][] input = {
					{0.1, 0.2}, {0.1, 0.3}, {0.1, 0.3},
					{2, 2}, {1, 3}, {4, 1},
				};
		double[] testPositive = {5, 6};
		double[] testNegative = {0.5, 0.6};
		
		dataset.addSample(input[0], 0);
		dataset.addSample(input[1], 0);
		dataset.addSample(input[2], 0);
		dataset.addSample(input[3], 1);
		dataset.addSample(input[4], 1);
		dataset.addSample(input[5], 1);
		GradientDescentLogistic gradientDescent = new GradientDescentLogistic();
		gradientDescent.setAlpha(1);
		gradientDescent.train(dataset);
		
		assertTrue(gradientDescent.predict(testPositive) > 0.5);
		assertTrue(gradientDescent.predict(testNegative) < 0.5);
	}

}
