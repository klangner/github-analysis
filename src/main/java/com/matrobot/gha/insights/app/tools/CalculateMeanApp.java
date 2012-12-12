package com.matrobot.gha.insights.app.tools;

import java.io.IOException;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class CalculateMeanApp {

	public static void main(String[] args) throws IOException {

		DescriptiveStatistics stats = new DescriptiveStatistics();
		stats.addValue(.80);
		stats.addValue(.81);
		stats.addValue(.81);
		stats.addValue(.82);
		stats.addValue(.84);
		stats.addValue(.81);
		stats.addValue(.84);
		stats.addValue(.83);
		stats.addValue(.81);
		stats.addValue(.82);
		
		System.out.println("Mean: " + stats.getMean());
		System.out.println("SD: " + stats.getStandardDeviation());
	}
}
