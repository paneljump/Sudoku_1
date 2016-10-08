package sudokuSolver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/* 
 * This style of sudoku is always 9x9, so the common indices in a row, column, 
 * or square (field) are always the same.
 * 
 * NOTE: This class contains other utilities:
 * 1. public static int[] arrayValuesNotIncluded(int[] fullList, int[] valsToExclude)
 * 2. public static int[] arrayListToArray(ArrayList<Integer> aList)
 * 3. public static int[] removeDuplicates(int[] arr, boolean sortQ)
 * 4. public static ArrayList<Integer> removeDuplicates(ArrayList<Integer> arr, boolean sortQ)
 * 5. public static void printArray(int[] arr)
 * 6. public static void printArrayList(ArrayList<Integer> arr)
 */

public class RCS {
	
	public RCS() {
		// no special initialization needed
	}
	
	public static int[] findSameRow(int  index){
		int rowNum,temp;
		int[] wholeRow=new int[9];
		rowNum=index/9;
		for(int i=0;i<9;i++){
			temp=(rowNum*9)+i;
			wholeRow[i]=temp;
		}
		return wholeRow;
	}
	
	public static int[] findSameColumn(int index){
		int colNum,temp;
		int[] wholeCol=new int[9];
		colNum=index%9;
		for(int i=0;i<9;i++){
			temp=colNum+(9*i);
			wholeCol[i]=temp;
		}
		return wholeCol;
	}
	
	public static int[] findSameSquare(int index){
		int vN,hN,temp;
		int[] wholeSquare=new int[9];
		hN=(index%9)/3;
		vN=(index/9)/3;
		int k=0;
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				temp=(hN*3+j) + (vN*3*9 + (i*9) );
				wholeSquare[k]=temp;
				k++;
			}
		}
		return wholeSquare;
	}

	
	// returns list of 9 RCS by type 'r' 'c' or 's'
	public static int[][] listRCSbyType(char type) {
		int[][] out=new int[9][9];
		if(type=='r')
			for(int i=0;i<9;i++)
				out[i]=findSameRow(9*i+i);
		else if(type=='c')
			for(int i=0;i<9;i++)
				out[i]=findSameColumn(9*i+i);
		else if(type=='s')
			for(int i=0;i<3;i++){
				for(int j=0;j<3;j++){
					out[(3*i+j)]=findSameSquare(27*i+3*j);
				}
			}
		return out;
	}
	
	// returns int[81][3] mapping index to r, c, and s
	public static int[][] mapIndicesToRCS() {
		int[][] out=new int[81][9];
		int[][] thisRCS;
		char[] ch={'r','c','s'};
		for(int i=0;i<3;i++) {
			thisRCS=listRCSbyType(ch[i]);
			for(int j=0;j<9;j++)
				for(int k=0;k<9;k++)
					out[thisRCS[j][k]][i]=j;
		}
		return out;
			
	}
	
	// returns int[] from fullList OUTSIDE of valsToExclude. Good for cleaning RCS.
	// works 10/4/16, even with empty values. Does not alter order.
	public static int[] arrayValuesNotIncluded(int[] fullList, int[] valsToExclude) {
		ArrayList<Integer> out=new ArrayList<Integer>();
		boolean addQ;
		
		for(int i=0;i<fullList.length;i++) {
			addQ=true;
			for(int j=0;j<valsToExclude.length;j++) {
				if(fullList[i]==valsToExclude[j])
					addQ=false;
			}
			if(addQ)
				out.add(fullList[i]);
		}
		
		int[] outArr = new int[out.size()];
		for(int i=0;i<out.size();i++) {
			outArr[i]=(int) out.get(i);
		}
		return outArr;
	}
	
	// works 10/4/16
	public static int[] arrayListToArray(ArrayList<Integer> aList) {
		int[] out = new int[aList.size()];
		for(int i=0;i<aList.size();i++) {
			out[i]=(int) aList.get(i);
		}
		return out;
	}
	
	// works 10/4/16. Sorts list, or not
	public static int[] removeDuplicates(int[] arr, boolean sortQ){
		int end = arr.length;
		Set<Integer> set;
		if(sortQ) {
			set = new HashSet<Integer>();
		}
		else
			set = new LinkedHashSet<Integer>();
		for(int i = 0; i < end; i++){
		  set.add(arr[i]);
		}
		Object[] outObj=set.toArray();
		int[] out=new int[outObj.length];
		for(int i=0;i<outObj.length;i++)
			out[i]=(int) outObj[i];
		return out;
		
	}
	
	// works 10/4/16. Sorts list, or not
	public static ArrayList<Integer> removeDuplicates(ArrayList<Integer> arr, boolean sortQ) {
		int end=arr.size();
		Set<Integer> set;
		if(sortQ) {
			set = new HashSet<Integer>();
		}
		else
			set = new LinkedHashSet<Integer>();
		for(int i=0;i<end;i++)
			set.add(arr.get(i));
		Object[] outObj=set.toArray();
		ArrayList<Integer> out=new ArrayList<Integer>();
		for(int i=0;i<outObj.length;i++)
			out.add((Integer) outObj[i]);
		return out;
	}

	public static int[][] get2DPuzzle(int[] puzzle){
		int[][] puzz=new int[9][9];
		for(int i=0;i<9;i++)
			for(int j=0;j<9;j++){
				puzz[i][j]=puzzle[i*9+j];
			}
		return puzz;
	}
	
	//for testing but could be useful
	public static void printArray(int[] arr) {
		for(int i=0;i<arr.length;i++)
			System.out.print(arr[i] + " ");
		System.out.println();
	}
	
	public static void printArrayList(ArrayList<Integer> arr) {
		for(int i=0;i<arr.size();i++)
			System.out.print( (int) arr.get(i) + " ");
		System.out.println();
	}


	
}
