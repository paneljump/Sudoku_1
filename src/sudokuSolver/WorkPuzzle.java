package sudokuSolver;

import java.util.ArrayList;
import java.util.LinkedHashSet;

/*
 * This class controls the logic to solve a Sudoku puzzle. 
 * 
 * It currently supports a simple strategy of locating naked or hidden singles, pairs, threes,
 * up to order N (specified by the user) within a given row, column, or square (field) and
 * systematically eliminating possibilities until values can be determined.
 * 
 * The Order N strategy cannot solve every puzzle. Future development will add more
 * advanced strategies.
 * 
 */

public class WorkPuzzle {
	private Puzzle P;
	private LinkedHashSet<Integer> indexQueue;
	private int[][] indexRCSmap;
	private OrderNFixer[][] orderNMatrix;
	
	public WorkPuzzle(Puzzle p) {
		this.P=p;
		this.indexQueue = new LinkedHashSet<Integer>();
		this.indexRCSmap = RCS.mapIndicesToRCS();
		this.makeOrderNMatrix();
	}
	
	private void makeOrderNMatrix() {
		OrderNFixer[][] out=new OrderNFixer[3][9];
		int[][] thisRCS;
		char[] type={'r','c','s'};
		for(int i=0;i<3;i++) {
			thisRCS=RCS.listRCSbyType(type[i]);
			for(int j=0;j<9;j++)
				out[i][j]=new OrderNFixer(this.P,thisRCS[j]);
		}
		this.orderNMatrix=out;
	}
	
	public void printPuzzle() { this.P.printPuzzle(); }
	
	/* For future development, if I choose to target indices that have been changed.
	 * This is not used for order N logic.
	 * 
	// keeps in order and avoids duplicates. Overloaded to accommodate ArrayList and int[]
	private void addIndicesToQueue(ArrayList<Integer> list) {
		for(int i=0;i<list.size();i++)
			this.indexQueue.add(list.get(i));
	}
	private void addIndicesToQueue(int[] list) {
		for(int i=0;i<list.length;i++)
			this.indexQueue.add(list[i]);
	}
	*/
	
	public boolean solveByOrderN(int limit) {
		if(this.P.getSolvedQ())
			return true;
		boolean changeQ=false;
		boolean solvedQ=false;
		
		for(int i=1;i<=limit;i++) {
			changeQ=workOrderN(i);
			solvedQ=this.P.getSolvedQ();
			if(solvedQ){
				System.out.println("Puzzle solved status="+String.valueOf(solvedQ));
				return true;
			}
			if(changeQ) {
				System.out.println("Puzzle changed, re-setting Order...");
				i=0; // if anything changes, ever, start with order 1. i will increment so use 0
			}
		}
		System.out.println("Puzzle solved status="+String.valueOf(solvedQ));
		return solvedQ;
	}
	
	public boolean workOrderN(int order) { // returns true if any options were eliminated
		OrderNFixer oNf;
		ArrayList<CleanThese> cleanThese=new ArrayList<CleanThese>();
		ArrayList<CleanThese> cleanThese2=new ArrayList<CleanThese>();
		ArrayList<int[]> out=new ArrayList<int[]>();
		ArrayList<int[]> out2=new ArrayList<int[]>();
		boolean keepGoing=true;
		boolean madeChange=false;
		boolean isSolved=false;
		int counter=0;
		do {
			keepGoing=false;
			counter++;
			for(int rcs=0;rcs<3;rcs++) { //do for row, column, and square
				for(int i=0;i<9;i++) {
					oNf=this.orderNMatrix[rcs][i];
					cleanThese=oNf.findOrderNCleanList(oNf.getIClist(), order, new ArrayList<CleanThese>(), 'i');
					cleanThese2=oNf.findOrderNCleanList(oNf.getVClist(), order, new ArrayList<CleanThese>(),'v' );
					for(int j=0;j<cleanThese.size();j++) {
						out=P.applyCleaner(cleanThese.get(j));
						if(out.size()>0)
							keepGoing=true;
					}
					for(int j=0;j<cleanThese2.size();j++) {
						out2=P.applyCleaner(cleanThese2.get(j));
						if(out2.size()>0)
							keepGoing=true;
					}
					
				}
			}
			isSolved=this.P.checkSolved();
			if(isSolved) {
				keepGoing=false;
			}
			if(keepGoing)
				madeChange=true;
		} while(keepGoing);
		System.out.println("Order "+order+" cycled "+counter+" times.");
		return madeChange;
	}



}
