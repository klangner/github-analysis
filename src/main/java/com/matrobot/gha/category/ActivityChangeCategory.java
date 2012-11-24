package com.matrobot.gha.category;

public class ActivityChangeCategory {

	/** Grow bigger then 100% of previous month */
	public static final int A = 5;
	/** Growing. Grow in range +10% - +100% */
	public static final int B = 4;
	/** Active. Activity in range -10% +10% of previous month */
	public static final int C = 3;
	/** Falling activity. Activity in range 90% - 10% when comparing to previous month */
	public static final int D = 2;
	/** Dying. Activity lower 10% of previous month */
	public static final int E = 1; 		
	/** Dead. 0 activity in the project */
	public static final int F = 0; 		
	
	/**
	 * 
	 * @param before - activity in previous month.
	 * @return after - activity next month
	 */
	public static int estimateCategory(double before, double after){
		
		if(before == 0){
			return F;
		}
		
		double diff = after/before;
		
		if(diff > 2){
			return A;
		}
		else if(diff > 1.1){
			return B;
		}
		else if(diff > 0.9){
			return C;
		}
		else if(diff > 0.1){
			return D;
		}
		else if(diff > 0){
			return E;
		}
		
		return F;
	}
}
