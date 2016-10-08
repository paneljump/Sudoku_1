package inputLayer;

import org.openqa.selenium.By;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class WebSudokuDotCom implements PageLike {
	private String url="http://www.websudoku.com";
	private WebDriver driver;
	private int[][] puzzle;
	private boolean isOpen;
	
	public WebSudokuDotCom(WebDriver passedDriver){
		driver=passedDriver;
		puzzle=new int[9][9];
		driver.get(url);
		isOpen=true;
	}

	@Override
	public void readPuzzle() {
		int i,j;
		String x;
		//WebElement baseTable=driver.findElement(By.id("table"));
		//WebElement frame=driver.findElement(By.xpath("//iframe[contains(@src,'show.websudoku.com')]"));
		//driver.switchTo().defaultContent();
		// /html/frameset/frame
		driver.switchTo().frame(0); //first frame of page...I wish I could get WebElement to work too, but it won't
		String maxLen;
		for(i=0;i<9;i++){
			for(j=0;j<9;j++){
				maxLen=driver.findElement(By.xpath("//table[@id='puzzle_grid']/tbody/tr["+(i+1)+"]/td["+(j+1)+"]/input"))
						.getAttribute("maxlength");
				if("1".equals(maxLen) ){ //this is supposed to be null-safe...only if it STARTS with the literal, weird!
					x="0";
				}
				else{
					x=driver.findElement(By.xpath("//table[@id='puzzle_grid']/tbody/tr["+(i+1)+"]/td["+(j+1)+"]/input")).getAttribute("value");
				}
					//System.out.print(x+" ");
				puzzle[i][j]=Integer.parseInt(x);
				System.out.print(puzzle[i][j]+" ");
			}
			System.out.println("");
		}

	}


	@Override
	public void writePuzzle(int[][] completePuzzle) throws InterruptedException {
		driver.switchTo().defaultContent();
		driver.switchTo().frame(0);
		int i,j;
		for(i=0;i<9;i++){
			for(j=0;j<9;j++){
				if(puzzle[i][j]==0){
					//System.out.println(String.valueOf(completePuzzle[i][j]));
					driver.findElement(By.xpath("//table[@id='puzzle_grid']/tbody/tr["+(i+1)+"]/td["+(j+1)+"]/input"))
					.click();
					driver.findElement(By.xpath("//table[@id='puzzle_grid']/tbody/tr["+(i+1)+"]/td["+(j+1)+"]/input"))
					.sendKeys(String.valueOf(completePuzzle[i][j]));
					//Thread.sleep(5000);
				}
			}
		}
	}

	@Override
	public int[][] getReadPuzzle() {
		// TODO Auto-generated method stub
		return this.puzzle;
	}

	@Override
	public void closeSite() {// for now I'm assuming one driver per page and site
		if(this.isOpen)
			this.driver.close();
	}

	@Override
	public void submitPuzzle() {
		// TODO Auto-generated method stub
		driver.switchTo().defaultContent();
		driver.switchTo().frame(0);
		driver.findElement(By.name("submit")).click();
	}

	@Override
	public void getNewPuzzle() {
		// TODO Auto-generated method stub
		driver.get(url);
		
	}

	@Override
	public void getNewPuzzleFromWin() {
		// TODO Auto-generated method stub
		//driver.switchTo().defaultContent();
		//driver.switchTo().frame(0);
		// /html/body/table/tbody/tr/td[3]/table/tbody/tr[2]/td/form/p[3]/input[4]
		// html body table tbody tr td table tbody tr td form p input
		driver.findElement(By.name("newgame")).click();
		
	}





}
