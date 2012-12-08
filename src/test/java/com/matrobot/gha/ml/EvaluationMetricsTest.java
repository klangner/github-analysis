package com.matrobot.gha.ml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EvaluationMetricsTest {

	@Test
	public void testMetrics() {
		
		EvaluationMetrics metrics = new EvaluationMetrics();
		
		metrics.addTruePositive();
		metrics.addTruePositive();
		
		metrics.addTrueNegative();
		metrics.addTrueNegative();
		
		metrics.addFalsePositive();
		metrics.addFalsePositive();
		metrics.addFalsePositive();

		metrics.addFalseNegative();
		metrics.addFalseNegative();
		metrics.addFalseNegative();
		metrics.addFalseNegative();
		
		assertEquals(2.0/5, metrics.getPrecision(), 0.01);
		assertEquals(2.0/6, metrics.getRecall(), 0.01);
		assertEquals(0.364, metrics.getFScore(), 0.01);
		assertEquals(4.0/11, metrics.getAccuracy(), 0.01);
	}
}
