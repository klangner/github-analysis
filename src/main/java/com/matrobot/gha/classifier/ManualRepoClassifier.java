package com.matrobot.gha.classifier;

import com.matrobot.gha.category.ActivityRating;

public class ManualRepoClassifier implements IClassifier {

	/**
	 * Manual classifier for repository activity for learning dataset.
	 * 
	 * Expected features:
	 * 	features[0] = current activity in log10 scale
	 * 	features[1] = current activity rating (from previous month)
	 * 
	 */
	@Override
	public int classify(int[] features) {

		if(features[1] == ActivityRating.UNKNOWN){
			return ActivityRating.UNKNOWN;
		}
		else{
			return ActivityRating.DECAYING;
		}
	}

}
