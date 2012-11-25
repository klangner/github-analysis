package com.matrobot.gha.category;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.matrobot.gha.category.ActivityRating;


public class ActivityRatingTest {

	@Test
	public void testZero() {
		int category = ActivityRating.estimateCategory(0, 1);
		
		assertEquals(ActivityRating.UNKNOWN, category);
	}

	@Test
	public void testA() {
		int category = ActivityRating.estimateCategory(10, 30);
		
		assertEquals(ActivityRating.EXPLODING, category);
	}


	@Test
	public void testB() {
		int category = ActivityRating.estimateCategory(10, 18);
		
		assertEquals(ActivityRating.GROWING, category);
	}


	@Test
	public void testC() {
		int category = ActivityRating.estimateCategory(10, 12);
		
		assertEquals(ActivityRating.STABLE, category);
	}


	@Test
	public void testC2() {
		int category = ActivityRating.estimateCategory(10, 8);
		
		assertEquals(ActivityRating.STABLE, category);
	}


	@Test
	public void testD() {
		int category = ActivityRating.estimateCategory(10, 7);
		
		assertEquals(ActivityRating.DECAYING, category);
	}


	@Test
	public void testE() {
		int category = ActivityRating.estimateCategory(11, 1);
		
		assertEquals(ActivityRating.DYING, category);
	}


	@Test
	public void testF() {
		int category = ActivityRating.estimateCategory(10, 0);
		
		assertEquals(ActivityRating.UNKNOWN, category);
	}
}
