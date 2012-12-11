package com.matrobot.gha.ml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NaiveBayesTest {

	@Test
	public void testDataset1() {
		
		NaiveBayes bayes = new NaiveBayes(1);
		Sample sample;
		
		sample = new Sample(2);
		sample.features[0] = 1;
		sample.features[1] = 1;
		sample.output = 1;
		bayes.train(sample);
		
		sample = new Sample(2);
		sample.features[0] = 1;
		sample.features[1] = 1;
		sample.output = 1;
		bayes.train(sample);
		
		sample = new Sample(2);
		sample.features[0] = 0;
		sample.features[1] = 0;
		sample.output = 1;
		bayes.train(sample);
		
		sample = new Sample(2);
		sample.features[0] = 0;
		sample.features[1] = 1;
		sample.output = 0;
		bayes.train(sample);
		
		sample = new Sample(2);
		sample.features[0] = 1;
		sample.features[1] = 0;
		sample.output = 0;
		bayes.train(sample);
		
		assertEquals(4.0/7.0, bayes.getPositiveProb(), 0.01);
		assertEquals(3.0/7.0, bayes.getNegativeProb(), 0.01);
		
		sample = new Sample(2);
		sample.features[0] = 1;
		sample.features[1] = 0;
		assertEquals(1, bayes.classify(sample.features), 0.01);
	}

}
