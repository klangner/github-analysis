package com.matrobot.gha.model;

/**
 * Static (dummy) model. This model assumes that nothing changes.
 * 
 * @author Krzysztof Langner
 *
 */
public class StaticModel implements IModel {

	/**
	 * Just return the same value.
	 */
	@Override
	public int makePrediction(int currentValue) {
		return currentValue;
	}

}
