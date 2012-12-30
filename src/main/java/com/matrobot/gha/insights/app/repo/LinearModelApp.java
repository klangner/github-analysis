package com.matrobot.gha.insights.app.repo;

import java.io.IOException;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import com.matrobot.gha.Configuration;
import com.matrobot.gha.ICommand;
import com.matrobot.gha.archive.event.EventReader;
import com.matrobot.gha.archive.event.FilteredEventReader;
import com.matrobot.gha.archive.event.IEventReader;
import com.matrobot.gha.archive.repotimeline.ITimelineRepoReader;
import com.matrobot.gha.archive.repotimeline.RepoTimeline;
import com.matrobot.gha.archive.repotimeline.TimelineRepoReader;
import com.matrobot.gha.insights.ml.EvaluationMetrics;

/**
 * Hypothesis:
 * Linear model will predict if next month activity is higher or lower then current month
 * 
 * Results (2012-5 - 2012-11):
 *  Accuracy: 0.9491334942467083
 *  Precision: 0.10961678684274488
 *  Recall: 0.5852076124567474
 *  F score: 0.18464687819856707 
 *   
 * @author Krzysztof Langner
 */
public class LinearModelApp implements ICommand{

	private EvaluationMetrics metrics;
	
	
	@Override
	public void run(Configuration params) throws IOException {

		metrics = new EvaluationMetrics();
		IEventReader eventReader = createEventReader(params);
		ITimelineRepoReader reader = createRepoReader(params, eventReader);
		analize(reader);
	}


	/**
	 * Filter by repository if repository param provided
	 */
	private IEventReader createEventReader(Configuration params) {
		IEventReader eventReader = new EventReader(params.getMonthFolders());
		if(params.getRepositories().size() > 0){
			FilteredEventReader filteredEventReader = new FilteredEventReader(eventReader);
			for(String repo : params.getRepositories()){
				filteredEventReader.addRepoFilter(repo);
			}
			eventReader = filteredEventReader;
		}
		return eventReader;
	}

	
	/**
	 * Create repository reader. 
	 * Add:
	 * - min activity filter
	 * - ordered reader
	 */
	private ITimelineRepoReader createRepoReader(Configuration params, IEventReader eventReader) {
		
		ITimelineRepoReader repoReader = new TimelineRepoReader(eventReader);
		
		return repoReader;
	}
	
	
	private void analize(ITimelineRepoReader reader){

		RepoTimeline record;
		while((record = reader.next()) != null){
			boolean isPositive = isPositiveSample(record);
			boolean expected = getExpectedValue(record);
			if(isPositive){
				if(expected){
					metrics.addTruePositive();
				}
				else{
					metrics.addFalsePositive();
				}
			}
			else{
				if(expected){
					metrics.addFalseNegative();
				}
				else{
					metrics.addTrueNegative();
				}
			}
		}
	}


	private boolean isPositiveSample(RepoTimeline record) {

		SimpleRegression regression = new SimpleRegression();
		int[] values = record.getDataPoints();
		int count = values.length;
		int prevValue = values[count-2];
		
		for(int i = 0; i < count-1; i++){
			regression.addData(i, values[i]);
		}
		
		int lastValue = (int) regression.predict(count-1);
		return (lastValue - prevValue > 0);
	}


	private boolean getExpectedValue(RepoTimeline record) {
		
		int count = record.getDataPoints().length;
		int lastValue = record.getDataPoints()[count-1];
		int prevValue = record.getDataPoints()[count-2];
		return (lastValue - prevValue > 0);
	}


	/**
	 * for local testing
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		Configuration params = new Configuration("configs/lm.yaml");
		
		LinearModelApp app = new LinearModelApp();
		app.run(params);
		app.metrics.print();
	}
	
}
