package com.matrobot.gha.category;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.matrobot.gha.category.ActivityChangeCategory;


public class ActivityRatingTest {

	@Test
	public void testZero() {
		int category = ActivityChangeCategory.estimateCategory(0, 1);
		
		assertEquals(ActivityChangeCategory.F, category);
	}

	@Test
	public void testA() {
		int category = ActivityChangeCategory.estimateCategory(10, 30);
		
		assertEquals(ActivityChangeCategory.A, category);
	}


	@Test
	public void testB() {
		int category = ActivityChangeCategory.estimateCategory(10, 18);
		
		assertEquals(ActivityChangeCategory.B, category);
	}


	@Test
	public void testC() {
		int category = ActivityChangeCategory.estimateCategory(10, 11);
		
		assertEquals(ActivityChangeCategory.C, category);
	}


	@Test
	public void testD() {
		int category = ActivityChangeCategory.estimateCategory(10, 8);
		
		assertEquals(ActivityChangeCategory.D, category);
	}


	@Test
	public void testE() {
		int category = ActivityChangeCategory.estimateCategory(10, 1);
		
		assertEquals(ActivityChangeCategory.E, category);
	}


	@Test
	public void testF() {
		int category = ActivityChangeCategory.estimateCategory(10, 0);
		
		assertEquals(ActivityChangeCategory.F, category);
	}
}
