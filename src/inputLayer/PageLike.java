package inputLayer;

public interface PageLike {
	public void readPuzzle();
	public void writePuzzle(int[][] completePuzzle) throws InterruptedException;
	public int[][] getReadPuzzle();
	public void closeSite();
	public void submitPuzzle();
	public void getNewPuzzle();
	public void getNewPuzzleFromWin();
}
