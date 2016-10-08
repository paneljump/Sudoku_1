package inputLayer;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.MarionetteDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class DriverMaker {
	
	public DriverMaker(){} // at this time, constructor is very simple
	
	public WebDriver makeDriver(String browser, String executableFilePath){
		
		WebDriver driver=null;
		File file=null;
		if(!browser.equalsIgnoreCase("Firefox"))
			file=new File(executableFilePath);
		
		try{
			if(browser.equalsIgnoreCase("Firefox")){
				driver=new FirefoxDriver();
				// built-in default for Selenium, no system configuration needed
				// WARNING: this will fail in versions 47+. Use Marionette instead
			}
			else if(browser.equalsIgnoreCase("Chrome")){
				System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
				driver=new ChromeDriver();
			}
			else if(browser.equalsIgnoreCase("IE") || browser.equalsIgnoreCase("Internet Explorer")
					|| browser.equalsIgnoreCase("InternetExplorer")){
				System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
				driver=new InternetExplorerDriver();
			}
			else if(browser.equalsIgnoreCase("Marionette")){
				System.setProperty("webdriver.gecko.driver", file.getAbsolutePath());
				driver=new MarionetteDriver();
			}
			else{
				System.out.println("Error: browser type "+browser+" not supported.");
			}
			return driver;
		}
		catch(Exception e){
			System.out.println("Error: could not make driver with browser "+browser+
					" and file "+executableFilePath);
			return null;
		}
		
		
	}
	
	// overloaded makeDriver() method changes the implicitlyWait time
	public WebDriver makeDriver(String browser, String executableFilePath, int tO){
		WebDriver driver=makeDriver(browser, executableFilePath);
		if(driver==null)
			return driver;
		driver.manage().timeouts().implicitlyWait(tO, TimeUnit.SECONDS);
		return driver;
	}

}
