package sudokuSolver;

import java.util.ArrayList;

/*
 * The CleanThese object is primarily intended for passing data between objects, rather than
 * to persist, so its instance variables are left public. 
 * 
 * The CleanThese(ArrayList<Integer>, ArrayList<Integer>, char, int[]) constructor
 * was written specifically for OrderN logic. Additional convenient constructors can be 
 * written as needed.
 * . 
 */

public class CleanThese {
	public int[] indices;
	public int[] values;
	public int[] rcs;
	public char cleanType; // 'i' or 'o', for an inner or outer clean; 'r' for remove
	public boolean errorQ=false; // so I can communicate an error if one comes up
	
	
	public CleanThese(int[] ind, int[] vals, char ior, int[] rcs) {
		this.indices=ind;
		this.values=vals;
		this.rcs=rcs;
		this.cleanType=ior;
	}
	
	public CleanThese(ArrayList<Integer> interestList, ArrayList<Integer> optionList, char iOv, int[] rcs) {
		int[] ofInt=RCS.arrayListToArray(interestList);
		int[] opts=RCS.arrayListToArray(optionList);
		this.rcs=rcs;
		if (iOv=='i') {
			this.indices=ofInt;
			this.values=opts;
			this.cleanType='o'; 
		}
		else if(iOv=='v') {
			this.values=ofInt;
			this.indices=opts;
			this.cleanType='i';
		}
		else if(iOv=='r') { // if using type 'r', assume that param order is indices then values. 
			this.indices=ofInt;
			this.values=opts;
			this.cleanType='r';
		}
		// can add else statement in case there is an error in 'i', 'v', or 'r' if it's worth it
	}
	
	// for testing
	public void printInfo() {
		System.out.println("Indices found are: ");
		RCS.printArray(this.indices);
		System.out.println("Values found are: ");
		RCS.printArray(this.values);
		System.out.println("Type is " + String.valueOf(this.cleanType) + 
				" and errorQ = " + String.valueOf(this.errorQ));
	}

}
