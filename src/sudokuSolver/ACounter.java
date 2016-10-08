package sudokuSolver;

import java.util.ArrayList;

/*
 * This counter can be used for an index (track number of value options) 
 * or a value (track possible indices within RCS). The symmetry of the sudoku problem
 * allows me to use this structure for either view.
 * 
 * NOTE: Operations that change the Puzzle object do not affect ACounter objects. 
 * These must be refreshed from the Puzzle object's trackMatrix before use.
 * 
 */

public class ACounter {
	
	private int ofInterest;
	private int[] itsOptions;
	private char indexOrValue; // 'i' or 'v', indicates whether "ofInterest" is index or value
	private boolean setQ=false;
	private boolean errorQ=false;
	private int occurrences; // for v it's #/places it could be, for i it's #/possible vals
	
	// pointers to outside objects:
	private Puzzle P;
	private int[] rcs; 
	
	// constructor, no error checking yet
	public ACounter(int oI, char iOv, int[] RCS, Puzzle p) {
		this.indexOrValue = iOv;
		this.ofInterest = oI;
		this.P = p;
		this.rcs = RCS;
		this.updateCounter();
	}
	
	// accessors
	public int getOfInterest() { return this.ofInterest; }
	public int[] getItsOptions() { return this.itsOptions; }
	public char getIndexOrValue() { return this.indexOrValue; }
	public boolean getSetQ() { return this.setQ; }
	public int getOccurrences() { return this.occurrences; }
	public boolean getErrorQ() { return this.errorQ; }
	
	public void updateCounter() {
		if(this.indexOrValue=='i')
			updateForIndex();
		else if(this.indexOrValue=='v')
			updateForValue();
	}
	
	private void updateForIndex() {
		if(setQ)
			return ;
		
		int[] TM = this.P.getTMbyIndex(this.ofInterest);
		int[] out = {0};
		ArrayList<Integer> allVals=new ArrayList<Integer>();
		for(int i=0;i<9;i++) {
			if(TM[i]==1) {
				setQ=true;
				out[0]=i+1;
				this.itsOptions=out;
				this.occurrences=1;
			}
			else if(TM[i]==-1)
				allVals.add(i+1);
		}
		
		// error checking
		if(setQ && allVals.size()>0) {
			this.errorQ=true;
			return;
		}
		else if(setQ)
			return;
		else if(allVals.size()==0) {
			this.errorQ=true;
			return;
		}
		
		out = new int[allVals.size()];
		for(int i=0;i<allVals.size();i++) 
			out[i]=(int) allVals.get(i);
		
		this.itsOptions = out;
		this.occurrences=out.length;
	}
	
	private void updateForValue() {
		if(setQ)
			return ;
		
		int[] TM;
		int[] out={0};
		int thisIndex;
		int indVal=ofInterest-1; // index of the value of interest
		ArrayList<Integer> allIndices = new ArrayList<Integer>();
		
		for(int i=0;i<rcs.length;i++) {
			thisIndex=rcs[i];
			TM=this.P.getTMbyIndex(thisIndex);
			if(TM[indVal]==1) {
				out[0]=thisIndex;
				setQ=true;
				this.itsOptions=out;
				this.occurrences=1;
			}
			else if(TM[indVal]==-1)
				allIndices.add(thisIndex);
		}
		
		// error checking
		if(setQ && allIndices.size()>0) {
			this.errorQ=true;
			return;
		}
		else if(setQ)
			return;
		else if(allIndices.size()==0) {
			this.errorQ=true;
			return;
		}
		
		out = new int[allIndices.size()];
		for(int i=0;i<allIndices.size();i++) 
			out[i]=(int) allIndices.get(i);
		
		this.itsOptions = out;
		this.occurrences=out.length;
		
	}
	
	// temporary, for testing 
	public void printACounterInfo() {
		System.out.println("Counter is type " + String.valueOf(this.indexOrValue) +
				" with ofInterest = " + this.ofInterest + " and has "+ 
				this.occurrences+" occurrences.");
		System.out.println("Possible values (or indices) are:");
		RCS.printArray(this.itsOptions);
		System.out.println("setQ = " + String.valueOf(setQ) + ", errorQ = "+String.valueOf(errorQ));
	}

}
