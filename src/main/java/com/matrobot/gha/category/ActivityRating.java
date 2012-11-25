package com.matrobot.gha.category;

public class ActivityRating {

	/** Grow bigger then 100% of previous month */
	public static final int EXPLODING = 5;
	/** Growing. Grow in range +10% - +100% */
	public static final int GROWING = 4;
	/** Active. Activity in range -10% +10% of previous month */
	public static final int STABLE = 3;
	/** Falling activity. Activity in range 90% - 10% when comparing to previous month */
	public static final int DECAYING = 2;
	/** Dying. Activity lower 10% of previous month */
	public static final int DYING = 1; 		
	/** Dead. 0 activity in the project */
	public static final int UNKNOWN = 0; 		
	
	/**
	 * 
	 * @param before - activity in previous month.
	 * @return after - activity next month
	 */
	public static int estimateCategory(double before, double after){
		
		if(before == 0){
			return UNKNOWN;
		}
		
		double diff = after/before;
		
		if(diff > 2){
			return EXPLODING;
		}
		else if(diff > 1.2){
			return GROWING;
		}
		else if(diff >= 0.8){
			return STABLE;
		}
		else if(diff >= 0.1){
			return DECAYING;
		}
		else if(diff > 0){
			return DYING;
		}
		
		return UNKNOWN;
	}
}
