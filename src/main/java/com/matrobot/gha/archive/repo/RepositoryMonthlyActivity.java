package com.matrobot.gha.archive.repo;

import java.util.ArrayList;
import java.util.List;

public class RepositoryMonthlyActivity {

	public String repository;
	public List<Integer> monthlyActivity = new ArrayList<Integer>();
	

	public RepositoryMonthlyActivity(String url) {
		repository = url;
		closeMonth();
	}


	/**
	 * Create new month
	 */
	public void closeMonth(){
		monthlyActivity.add(0);
	}

	
	/**
	 * Add 1 to last month
	 */
	public void incLastMonth(){
		int lastMonth = monthlyActivity.get(monthlyActivity.size()-1);
		monthlyActivity.set(monthlyActivity.size()-1, lastMonth+1);
	}


	public String toCSV() {
		String line = repository;
		
		for(int i = 0; i < monthlyActivity.size()-1; i++){
			line += ", " + monthlyActivity.get(i);
		}
		
		return line;
	}
}
