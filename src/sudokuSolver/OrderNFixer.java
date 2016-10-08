package sudokuSolver;

import java.util.ArrayList;

/* 
 * The OrderNFixer object has two lists of ACounter objects (one for index and one for value)
 * for each index in its rcs (all indices in one row, column, or square). Note that these 
 * lists are reduced as indices are solved.
 * 
 * The primary purpose is to return an ArrayList of CleanThese objects, which will be sent
 * to the Puzzle object to attempt to eliminate possibilities in the Puzzle's trackMatrix.
 * 
 * The public method findOrderNCleanList(ArrayList<ACounter> aList, int order, 
			ArrayList<CleanThese> cleanList, char iOv )
 * handles the general case, for naked or hidden singles, pairs, etc (i.e. order N) and is
 * recursive to ensure that all sets of order N within the rcs are collected. (For example,
 * it is possible to have two different hidden pairs in the same row.)
 */

public class OrderNFixer {
	private int[] rcs;
	private Puzzle P;
	private ArrayList<ACounter> icList; // index counter list
	private ArrayList<ACounter> vcList; // value counter list
	
	public OrderNFixer(Puzzle p, int[] rcess) {
		this.P=p;
		this.rcs=rcess;
		this.makeIndexList();
		this.makeValueList();
	}
	
	// accessors
	public ArrayList<ACounter> getIClist() { return icList; }
	public ArrayList<ACounter> getVClist() { return vcList; }
	
	private void makeIndexList() {
		ArrayList<ACounter> aL=new ArrayList<ACounter>();
		for(int i=0;i<rcs.length;i++) { // generally 9 but I might want option for shorter
			aL.add(new ACounter(rcs[i],'i',this.rcs,this.P));
		}
		this.icList=aL;
		this.removeSetCounters(this.icList);
	}
	private void makeValueList() {
		ArrayList<ACounter> aL=new ArrayList<ACounter>();
		for(int i=0;i<9;i++) {
			aL.add(new ACounter(i+1,'v',this.rcs,this.P));
		}
		this.vcList=aL;
		this.removeSetCounters(this.vcList);
	}
	private void updateList(ArrayList<ACounter> list) {
		int len=list.size();
		for(int i=0;i<len;i++) {
			list.get(i).updateCounter();
		}
	}
	private void removeSetCounters(ArrayList<ACounter> list) {
		for(int i=list.size()-1;i>=0;i--) {
			if(list.get(i).getSetQ()==true)
				list.remove(i);
		}
	}
	
	// extracts lists of ofInterest and options values from members of list corresponding to index list
	private ArrayList< ArrayList<Integer> > getListInfo(ArrayList<Integer> indices, ArrayList<ACounter> list) {
		ArrayList<Integer> listOfInterest = new ArrayList<Integer>();
		ArrayList<Integer> listOfOptions = new ArrayList<Integer>();
		int[] opts;
		int n;
		
		for(int i=0;i<indices.size();i++) {
			n=(int) indices.get(i); // index within list of ACounters
			listOfInterest.add( list.get(n).getOfInterest() );
			opts=list.get(n).getItsOptions();
			for(int j=0;j<opts.length;j++) {
				listOfOptions.add(opts[j]);
			}
		}
		listOfOptions=RCS.removeDuplicates(listOfOptions,true);
		ArrayList< ArrayList<Integer> > out=new ArrayList< ArrayList<Integer> >();
		out.add(listOfInterest);
		out.add(listOfOptions);
		return out;
	}
	
	// reduces list of ACounter objects where number of options could work for order desired
	private ArrayList<ACounter> getShortList(ArrayList<ACounter> aList, int order) {
		ArrayList<ACounter> shortList = new ArrayList<ACounter>();
		if(order<1)
			;
		else if(order==1) {
			for(int i=0;i<aList.size();i++)
				if(aList.get(i).getItsOptions().length==1 && aList.get(i).getSetQ()==false)
					shortList.add(aList.get(i));
		}
		else {
			for(int i=0;i<aList.size();i++) {
				if(aList.get(i).getItsOptions().length>=2 && 
						aList.get(i).getItsOptions().length<=order && aList.get(i).getSetQ()==false)
					shortList.add(aList.get(i));
			}
		}
		
		
		return shortList;
	}
	
	public ArrayList<CleanThese> findOrderNCleanList(ArrayList<ACounter> aList, int order, 
			ArrayList<CleanThese> cleanList, char iOv ) {
		
		this.updateList(aList); // refresh counters from puzzle
		
		// return if there aren't enough to make a CleanThese object.
		ArrayList<ACounter> shortList=this.getShortList(aList, order);
		if(shortList.size() < order)
			return cleanList;

		// declare variables to hold info extracted and compared within loop
		ArrayList<ArrayList<Integer>> ans; 
		ArrayList<Integer> interestList;
		ArrayList<Integer> optionList;
		
		// list of indices, can add or remove them and re-calculate
		ArrayList<Integer> indices= new ArrayList<Integer>();
		ArrayList<Integer> startList = new ArrayList<Integer>();
		
		for(int i=0; i<shortList.size(); i++) {
			indices.add(i);
			startList.add(i);
			ans=this.getListInfo(indices, shortList);
			interestList=ans.get(0);
			optionList=ans.get(1);
			
			// case 1: options exceed order, always reject option & then continue
			if(optionList.size()>order) {
				indices.remove(indices.size()-1);
				startList.remove(startList.size()-1);
			}
			// case 2: full successful match found
			if(optionList.size()==order && interestList.size()==order) {
				cleanList.add(new CleanThese(interestList,optionList,iOv,this.rcs));
				for(int j=indices.size()-1;j>=0;j--) {
					shortList.remove((int)indices.get(j));
				}
				return findOrderNCleanList(shortList, order, cleanList, iOv); // recursion in case there are more
			}
			// case 3: reached end of list, no match found
			if(optionList.size()<order && i==shortList.size()-1) {
				// know when to exit because all options exhausted
				if(startList.size()==0) {
					return cleanList;
				}
				if(startList.get(0)>shortList.size()-order) { //for efficiency, not really necessary
					return cleanList;
				}
				// remove last successful match and reset i to that match's index+1.
				i = startList.get(startList.size()-1); // note 'i' will be incremented before it's used again
				indices.remove(indices.size()-1);
				startList.remove(startList.size()-1); 
				
			}
			
		}
		
		return cleanList; // should never be reached but the error checker doesn't know that
		
	}

}
