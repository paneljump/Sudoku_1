package sudokuSolver;

import java.util.ArrayList;

/*
 * The Puzzle class has two main objects: a "puzzle", an int[81] object to hold 
 * the values for the 81 puzzle indices, and a "trackMatrix", an int[81][9] object
 * to hold the 9 possible values for each puzzle index.
 * 
 * Public methods are currently limited to:
 *   printPuzzle()
 *   checkSolved()
 *   applyCleaner(CleanThese cleanThis)
 *   setOneValue(int index, int value)  <-- public for future development
 *   removeThisOption(int index, int value)  <-- public for future development
 * 
 * NOTE: The "puzzle" uses 0 for unknown. The "trackMatrix" uses -1 for unknown,
 * 0 for not possible, and 1 for confirmed.
 * 
 */

public class Puzzle {
	private int[] puzzle;
	private int[][] trackMatrix;
	private boolean solvedQ;
	private boolean errorQ;
	
	// NOTE this creates its own internal puzzle rather than changing something external
	public Puzzle(int[] puzz){
		int[] p=new int[81];
		for(int i=0;i<81;i++)
			p[i]=puzz[i];
		this.puzzle=p;
		this.makeTrackMatrix();
		this.errorQ=false;
		this.solvedQ=false;
		this.checkSolved();
		this.setTrackMat();
		
	}
	
	public Puzzle(int[][] webInput) {
		int[] p=new int[81];
		for(int i=0;i<9;i++) {
			for(int j=0;j<9;j++)
				p[i*9+j]=webInput[i][j];
		}
		this.puzzle=p;
		this.makeTrackMatrix();
		this.errorQ=false;
		this.solvedQ=false;
		this.checkSolved();
		this.setTrackMat();
	}
	
	// snapshot accessors: copies NOT pointers, not intended to persist
	public int getPuzzByIndex(int ind) { return puzzle[ind]; }
	public int[] getTMbyIndex(int ind) { return trackMatrix[ind].clone(); }
	public boolean getSolvedQ() { return this.solvedQ; }
	public void setSolvedQ(boolean isSolved) { this.solvedQ=isSolved; }
	
	public int[] getPuzzle() {return puzzle;}
	
	private void makeTrackMatrix(){
		int[][] tm=new int[81][9];
		for(int i=0;i<81;i++)
			for(int j=0;j<9;j++)
				tm[i][j]=-1;
		this.trackMatrix=tm;
	}
	// eliminates initial values from tracking matrix and sets initial queue
	private void setTrackMat() {
		ArrayList<int[]> iQ=new ArrayList<int[]>();
		ArrayList<int[]> temp;
		for(int i=0;i<81;i++) {
			if(this.puzzle[i]>0) {
				temp=this.cleanSetNum(i, this.puzzle[i]);
				for(int j=0;j<temp.size();j++)
					iQ.add(temp.get(j));
			}
		}
		//this.initialQueue=RCS.removeDuplicates(iQ, false);
	}
	
	public void printPuzzle(){
		System.out.println("");
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				System.out.print(this.puzzle[i*9+j] + " ");
			}
			System.out.println("");
		}
	}
	
	public boolean checkSolved(){
		if(this.solvedQ)
			return true;
		
		boolean isSolved=true;
		for(int i=0;i<81;i++)
			if(this.puzzle[i]==0)
				isSolved=false;
		//NOTE: sets internal AND returns value
		this.solvedQ=isSolved;
		return isSolved;
	}
	
	public ArrayList<int[]> setOneValue(int index, int value){
		this.puzzle[index]=value;
		return this.cleanSetNum(index, value);
	}
	
	// Returns {index,value} to support future development of more advanced logic
	// NOTE: index is 0-80, value is 1-9.
	public int[] removeThisOption(int index, int value){
		int n = this.trackMatrix[index][value-1];
		int[] out={};
		if(n==-1){
			this.trackMatrix[index][value-1]=0;
			out=new int[2];
			out[0]=index;
			out[1]=value;
			return out;
		}
		else if(n==1) {
			this.errorQ=true;
		}
		return out;
	}
	
	
	private ArrayList<int[]> cleanOuterOrderN(int[] inIndices, int[] values, int[] rcsIndices) {
		
		if(values.length!=inIndices.length) {
			ArrayList<int[]> out=new ArrayList<int[]>();
			return out;
		}
		
		int[] remValues = values.clone();
		int[] fromIndices = RCS.arrayValuesNotIncluded(rcsIndices, inIndices);
		
		// NOTE this operates on puzzle and returns ArrayList of indices affected
		return this.removeValuesFromIndices(fromIndices, remValues); 
		
	}
	
	// this cleans out hidden pairs, threes, etc. Works for singles too.
	// returns ArrayList of {index,value} that have been changed
	// note this doesn't need rcsIndices parameter but I'm tempted to leave it for symmetry
	private ArrayList<int[]> cleanInnerOrderN(int[] inIndices, int[] values, int[] rcsIndices){
		
		if(values.length!=inIndices.length) {
			ArrayList<int[]> out=new ArrayList<int[]>();
			return out;
		}
		
		int[] fromIndices=inIndices.clone();   // fromIndices is rcs INSIDE inIndices
		int[] allValues = {1,2,3,4,5,6,7,8,9}; // all possible values
		int[] remValues = RCS.arrayValuesNotIncluded(allValues, values);
		
		// NOTE this operates on puzzle and returns ArrayList of indices affected
		return this.removeValuesFromIndices(fromIndices, remValues); 
				
	}
	
	private ArrayList<int[]> removeValuesFromIndices(int[] indices, int[] values) {
		int limVal=values.length;
		int limInd=indices.length;
		int[] addThis={};
		boolean testChange;
		ArrayList<int[]> out=new ArrayList<int[]>();
		
		for(int i=0;i<limInd; i++) {
			testChange=false;
			for(int j=0;j<limVal;j++) {
				addThis = removeThisOption(indices[i],values[j]);
				if( addThis.length > 0 )
					testChange=true;
			}
			if(testChange) {
				out.add(addThis);
			}
		}
		return out;
	}
	
	// when a number in the puzzle is set, this will clean out the tracking matrix
	private ArrayList<int[]> cleanSetNum(int index, int value){
		if(this.trackMatrix[index][value-1]==0)
			this.errorQ=true;
		
		for(int i=0;i<9;i++) {
			this.trackMatrix[index][i]=0;
		}
		this.trackMatrix[index][value-1]=1;
		
		int[] values=new int[1];
		values[0]=value;
		int[] inIndices=new int[1];
		inIndices[0]=index;
			
		ArrayList<int[]> out,temp;
		int[] rcsIndices; //for row, col, sq
			
		// clean tracking matrix and collect list of {index,value} with changes
		rcsIndices=RCS.findSameRow(index);
		out=this.cleanOuterOrderN(inIndices, values, rcsIndices);
		rcsIndices=RCS.findSameColumn(index);
		temp=this.cleanOuterOrderN(inIndices, values, rcsIndices);
		for(int i=0;i<temp.size(); i++)
			out.add(temp.get(i));
		rcsIndices=RCS.findSameSquare(index);
		temp=this.cleanOuterOrderN(inIndices, values, rcsIndices);
		for(int i=0;i<temp.size(); i++)
			out.add(temp.get(i));
			
		for(int i=0;i<9;i++){
			if(i+1==value)
				this.trackMatrix[index][i]=1;
			else
				this.trackMatrix[index][i]=0;
		}
			
		return out;
			
	}
	
	// currently used by OrderN logic
	// consider checking cleanThis for null/errors before calling this
	public ArrayList<int[]> applyCleaner(CleanThese cleanThis) {
		ArrayList<int[]> out=new ArrayList<int[]>();
		if(cleanThis.cleanType=='r' && 
				cleanThis.indices.length==1 && cleanThis.values.length==1) {
			this.removeThisOption(cleanThis.indices[0], cleanThis.values[0]);
			out=new ArrayList<int[]>();
			int[] put=new int[2];
			put[0]=cleanThis.indices[0];
			put[1]=cleanThis.indices[1];
			out.add(put);
		}
		else if( (cleanThis.cleanType=='o' || cleanThis.cleanType=='i') &&
				cleanThis.indices.length==1 && cleanThis.values.length==1) {
			out=this.setOneValue(cleanThis.indices[0], cleanThis.values[0]);
		}
		else if( cleanThis.cleanType=='o') {
			out=this.cleanOuterOrderN(cleanThis.indices, cleanThis.values, cleanThis.rcs);
		}
		else if( cleanThis.cleanType=='i') {
			out=this.cleanInnerOrderN(cleanThis.indices, cleanThis.values, cleanThis.rcs);
		}
		
		return out;
	}

}
