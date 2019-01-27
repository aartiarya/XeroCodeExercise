package com.aartitest.selenium.helpers;

import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import com.relevantcodes.extentreports.ExtentReports;

import static java.io.File.separator;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;
/**
 * @author Aarti Arya
 *
 */

public class Page extends GenericTest{

	protected Logger logger = new GenericTest().logger;
	private static byte[] sharedvector = {0x01, 0x02, 0x03, 0x05, 0x07, 0x0B, 0x0D, 0x11};
	protected int waitShort = Integer.parseInt(getProperty("waitShort"));
	protected int waitMedium = Integer.parseInt(getProperty("waitMedium"));
	protected int waitLong = Integer.parseInt(getProperty("waitLong")); protected
	int waitVLong = Integer.parseInt(getProperty("waitVLong"));
	 

	public Page()
	{

	}

	public Page(WindowManagingWebDriver driver) {

		super(driver);
	}

	//***********************Initialization****************************

	public WebDriver openBrowser() {
		return new WindowManagingWebDriver();
	}

	protected WindowManagingWebDriver getDriver() {
		return driver;
	}


	public static ExtentReports Instance()
    {
    ExtentReports extent;
    String currentEnvironment = new EnvironmentProperties().getCurrentEnvironmentName();
    DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmssZ");
    Date date = new Date();
    String Path = System.getProperty("user.home")+ getProperty("path") + separator + currentEnvironment + separator + "ExtentReport_" + reportDate+ separator +"ExtentReport.html";
    extent = new ExtentReports(Path, false);
    extent.addSystemInfo("Selenium Version", "2.45");
    extent.addSystemInfo("Environment",currentEnvironment);

    return extent;
 }


	protected void clickSubMenu1(final By byMainMenu, final By bySubMenu, final By bySubmenu1) {
		//set focus on the object first
		Actions builder = new Actions(driver);
		builder.moveToElement(findObject(byMainMenu)).build().perform();
		logger.info("Menu: '" + byMainMenu.toString() + "' clicked");
		waitForSeconds(waitShort);
		builder.moveToElement(findObject(bySubMenu)).click().build().perform();
		logger.info("Submenu: '" + bySubMenu.toString() + "' clicked");
		waitForSeconds(waitShort);
		builder.moveToElement(findObject(bySubmenu1)).click().build().perform();
		logger.info("Submenu: '" + bySubMenu.toString() + "' clicked");
	}


	protected void waitTillPageIsLoaded(String pageTitle) {
		try {

			Integer timeOut = Integer.parseInt(getProperty("page.navigation.timeout"));
			FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					.withTimeout(timeOut, TimeUnit.SECONDS)
					.pollingEvery(1, TimeUnit.SECONDS);

			Boolean pageDisplayed = wait.until(ExpectedConditions.titleContains(pageTitle));
			//driver.manage().window().maximize();
			if (pageDisplayed == true) {
				logger.info("Page - '" + pageTitle + "' => Displayed");
			} else {
				logger.error("Page - '" + pageTitle + "' => Not Displayed");
			}
			//driver.manage().window().maximize();
		} catch (Throwable t) {
			// Terminate the test case in case exception is thrown
			handleException(t);
		}
	}



	protected void waitTillPageIsLoadedInSameWindow(String pageTitle) {
		waitTillPageIsLoaded(pageTitle);
	}

	protected void waitTillPageIsLoadedInSeparateWindow(String pageTitle) {
		/*
		 * First of all, the driver instance needs to be switched to the new window
		 */
		try {
			Integer loopCounter = 0;
			Integer timeOut = Integer.parseInt(getProperty("page.navigation.timeout"));
			//Loop through the window handles to find the correct handle -
			//that matches the window title. Set driver to required window using the handle
			do {
				//Get all the window handles that are open
				Set<String> windowhandles = driver.getWindowHandles();
				//Put all the handles in an Iterator
				Iterator<String> iterator = windowhandles.iterator();
				while (iterator.hasNext()) {
					String windowHandle = (String) iterator.next();
					driver.switchTo().window(windowHandle);
					if (driver.getTitle().contains(pageTitle)) {
						break;
					} else if (driver.getTitle().contains("Certificate Error: Navigation Blocked")) {
						dismissSecurityCertificateWarning(pageTitle);
						windowhandles = driver.getWindowHandles();
						iterator = windowhandles.iterator();
						break;
					}
					waitForSeconds(waitShort);
					loopCounter = loopCounter + 1;
					if (loopCounter > timeOut) {
						break;
					}
				}
			} while (!(driver.getTitle().contains(pageTitle)));
			driver.manage().window().maximize();
		} catch (Throwable t) {
			// Terminate the test case in case exception is thrown
			handleException(t);
		}
	}

	protected void waitTillDialogBoxIsLoaded(String dialogBoxTitle) {
		/*
		 * First of all, the driver instance needs to be switched to the new window
		 */
		try {
			Integer loopCounter = 0;
			Integer timeOut = Integer.parseInt(getProperty("page.navigation.timeout"));
			//Loop through the window handles to find the correct handle -
			//that matches the window title. Set driver to required window using the handle
			do {
				//Get all the window handles that are open
				Set<String> windowhandles = driver.getWindowHandles();
				//Put all the handles in an Iterator
				Iterator<String> iterator = windowhandles.iterator();
				while (iterator.hasNext()) {
					String windowHandle = (String) iterator.next();
					driver.switchTo().window(windowHandle);
					if (driver.getTitle().contains(dialogBoxTitle)) {
						break;
					} else if (driver.getTitle().contains("Certificate Error: Navigation Blocked")) {
						dismissSecurityCertificateWarning(dialogBoxTitle);
						windowhandles = driver.getWindowHandles();
						iterator = windowhandles.iterator();
						break;
					}

					waitForSeconds(waitShort);
					loopCounter = loopCounter + 1;
					if (loopCounter > timeOut) {
						break;
					}
				}
			} while (!(driver.getTitle().contains(dialogBoxTitle)));
		} catch (Throwable t) {
			// Terminate the test case in case exception is thrown
			handleException(t);
		}
	}


	
	protected WebElement findObject(final By by) {
		try {
			Integer timeOut = Integer.parseInt(getProperty("page.navigation.timeout"));
			FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					.withTimeout(timeOut, TimeUnit.SECONDS)
					.pollingEvery(5, TimeUnit.SECONDS);
			wait.until(ExpectedConditions.presenceOfElementLocated(by));
			WebElement pageDisplayed = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(by)));


			return pageDisplayed;
		} catch (Throwable t) {
			// Terminate the test case in case exception is thrown
			handleException(t);
		}
		return null;
	}

	protected void setFocusOnObject(final By by) {
		driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
		try {
			System.out.println("Byh:" + by);

			findObject(by).sendKeys();
		} catch (Throwable t) {
			// Terminate the test case in case exception is thrown
			handleException(t);
		}
	}

	protected void setValueInTextField(final By by, String value) {
		WebDriverWait wait = new WebDriverWait(driver, 15); 
		wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		findObject(by).click();
		findObject(by).sendKeys(value);
		/*JavascriptExecutor jse = (JavascriptExecutor) driver;
    		jse.executeScript("document.getElementById('username_id').value ='"+ value +"';");*/
		logger.info("Text Field: '" + by.toString() + "'. Value '" + value + "' entered");
	}

	protected String getValueFromTextField(final By by) {
		clickLink(by);

		String value = findObject(by).getAttribute("value");

		logger.info("Text Field: '" + by.toString() + "'. Value '" + value + "' found");
		return value;
	}

	protected void selectValueFromDropDownField(final By by, String value) {
		findObject(by).click();
		driver.getKeyboard().sendKeys(Keys.ENTER);
		Boolean valueFound = true;
		String dropDownValue = findObject(by).getAttribute("value");
		System.out.println("dropDownValue" + dropDownValue);
		String firstValue = dropDownValue;
		while (!dropDownValue.equals(value)) {
			System.out.println("Loop" + dropDownValue);
			driver.getKeyboard().sendKeys(Keys.DOWN);
			dropDownValue = findObject(by).getAttribute("value");
			if (firstValue.contains(dropDownValue)) {
				System.out.println("firstValue" + dropDownValue);
				valueFound = false;
				break;
			}
		}

		if (valueFound == true) {
			logger.info("Drop Down Field: '" + by.toString() + "'. Value '" + value + "' selected");
		} else {
			logger.error("Drop Down Field: '" + by.toString() + "'. Value '" + value + "' NOT found");
		}

	}

	protected void selectValueFromDropDownFieldByCount(final By by, int count) {
		findObject(by).click();
		waitForSeconds(waitShort);
		for (int i = count; i > 0; i--) {
			driver.getKeyboard().sendKeys(Keys.DOWN);
		}
		driver.getKeyboard().sendKeys(Keys.ENTER);

		String dropDownValue = findObject(by).getAttribute("value");
		logger.info("Drop Down Field: '" + by.toString() + "'. Value '" + dropDownValue + "' selected");
	}


	protected void selectValueFromTextAreaDropDownField(final By byDropDownArrow, final By byDropDownField, String value) {
		int downKeyPress = 1;
		findObject(byDropDownArrow).click();
		driver.getKeyboard().sendKeys(Keys.DOWN);
		driver.getKeyboard().sendKeys(Keys.ENTER);

		Boolean valueFound = true;
		String dropDownValue = findObject(byDropDownField).getAttribute("value");
		String firstValue = dropDownValue;
		while (!dropDownValue.equals(value)) {
			downKeyPress = downKeyPress + 1;
			//System.out.println(downKeyPress);
			findObject(byDropDownArrow).click();
			for (int i = 0; i < downKeyPress; i++) {
				driver.getKeyboard().sendKeys(Keys.DOWN);
				waitForMilliSeconds(500);
			}
			driver.getKeyboard().sendKeys(Keys.ENTER);
			dropDownValue = findObject(byDropDownField).getAttribute("value");
			if (firstValue.equals(dropDownValue)) {
				valueFound = false;
				break;
			}
		}

		if (valueFound == true) {
			logger.info("Text Area DropDown Field: '" + byDropDownField.toString() + "'. Value '" + value + "' selected");
		} else {
			logger.error("Text Area DropDown Field: '" + byDropDownField.toString() + "'. Value '" + value + "' NOT found");
		}

	}

	protected void selectFirstValueFromTextAreaDropDownField(final By byDropDownArrow, final By byDropDownField) {
		findObject(byDropDownArrow).click();
		driver.getKeyboard().sendKeys(Keys.DOWN);
		waitForSeconds(waitShort);
		driver.getKeyboard().sendKeys(Keys.ENTER);
		waitForSeconds(waitShort);
	}

	protected void clickLink(final By by) {
		//set focus on the object first
		//setFocusOnObject(by);

		findObject(by).click();
		logger.info("Link: '" + by.toString() + "' clicked");

	}


	protected void clickLinkByJavaExecutor(final By by) {
		//set focus on the object first
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("document.getElementById('arid_WIN_4_1000000000').style.display='block';");
		logger.info("Link: '" + by.toString() + "' clicked");

	}

	protected void clearValueFromDropDownField(final By by) {
		findObject(by).click();

		driver.getKeyboard().sendKeys(Keys.DOWN);
		driver.getKeyboard().sendKeys(Keys.ENTER);
		System.out.println("by" + by);

		Boolean valueFound = true;
		String dropDownValue = findObject(by).getAttribute("value");
		System.out.println("dropDownValue" + dropDownValue);
		String firstValue = dropDownValue;
		while (!dropDownValue.equals("")) {
			System.out.println("Loop" + dropDownValue);
			driver.getKeyboard().sendKeys(Keys.DOWN);
			dropDownValue = findObject(by).getAttribute("value");
			if (firstValue.equals(dropDownValue)) {
				System.out.println("firstValue" + dropDownValue);
				valueFound = false;
				break;
			}

		}
	}


	//Author : Datta Nikrad 16june2014
	protected void clickSubMenu(final By byMainMenu, final By bySubMenu) {
		//set focus on the object first
		Actions builder = new Actions(driver);
		builder.moveToElement(findObject(byMainMenu)).click().build().perform();
		logger.info("Menu: '" + byMainMenu.toString() + "' clicked");
		waitForSeconds(waitShort);
		TakeScreenshot(TESTCASE_NAME);
		builder.moveToElement(findObject(bySubMenu)).doubleClick().build().perform();
		logger.info("Submenu: '" + bySubMenu.toString() + "' clicked");
		waitForSeconds(waitVLong);
	}

	protected void switchtoFrame1(String frametagName, String frame)
	{
		final List<WebElement> frames=driver.findElements(By.tagName(frametagName));
        for(WebElement Reqframe: frames)
        {
               System.out.println("Frame Name: "+Reqframe.getAttribute("id"));
        if(Reqframe.getAttribute("id").equals(frame))
               {
               driver.switchTo().frame(Reqframe);
               break;
               }
        }
	}
	
	protected void switchToFrame(String frame) {
		driver.switchTo().frame(frame);
	}


	//Window=true : for switching to child window , /Window=false : for switching to main window
	protected void switchToWindow(boolean window) {
		waitForSeconds(waitLong);
		Set<String> windowIds1 = driver.getWindowHandles();
		Iterator<String> iter1 = windowIds1.iterator();
		String mainWindowId = iter1.next();
		String tabbedWindowId = iter1.next();
		System.out.println("window value" + driver.getWindowHandles().size());
		System.out.println("window value" + window);
		System.out.println("tabbedWindowId value" + tabbedWindowId);
		if (window == true) {
			System.out.println("child" + window);
			driver.switchTo().window(tabbedWindowId);
			if (driver.getWindowHandles().size() == 3) {
				String tabbedWindowId1 = iter1.next();
				System.out.println("child" + window);
				driver.switchTo().window(tabbedWindowId1);
			}
		} else {
			System.out.println("parent" + window);
			driver.switchTo().window(mainWindowId);
		}
	}

	public void popupmessage() {
		System.out.println("IN frame");
		List<WebElement> li = driver.findElements(By.tagName("iframe"));
		System.out.println(li.size());
		if (li.size() >= 1) {
			driver.switchTo().frame(li.size() - 1);
			TakeScreenshot(TESTCASE_NAME);
			waitForSeconds(waitShort);
			// driver.findElement(By.linkText("OK")).click();
			driver.getKeyboard().sendKeys(Keys.ENTER);  
			TakeScreenshot(TESTCASE_NAME);
			driver.switchTo().defaultContent();
		}
	}

	public void popupmessageWindow() {
		System.out.println("IN frame");
		List<WebElement> li = driver.findElements(By.tagName("iframe"));
		System.out.println(li.size());
		if (li.size() >= 1) {
			driver.switchTo().frame(li.size() - 1);
			// System.out.println(li.size());
			TakeScreenshot(TESTCASE_NAME);
			waitForSeconds(waitShort);
			driver.findElement(By.linkText("OK")).click();
			waitForSeconds(waitShort);

		}
	}
	
	public void closeCurrentPage() {
		try {
			driver.getSessionId().toString();
			driver.close();
		} catch (Throwable t)

		{
			handleException(t);
		}
	}

	protected void closePageWithTitle(String pageTitle) {
		try {
			waitTillPageIsLoadedInSeparateWindow(pageTitle);
			driver.close();
			logger.info("Page: " + "'" + pageTitle + "' closed");
		} catch (Throwable t) {
			// Terminate the test case in case exception is thrown
			handleException(t);
		}
	}

	protected void switchBackToPageWithTitle(String pageTitle) {
		waitForSeconds(waitShort);
		waitTillPageIsLoadedInSeparateWindow(pageTitle);
	}

	public void waitForSeconds(Integer seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			handleException(e);
		}
	}

	protected void waitForMilliSeconds(Integer milliSeconds) {
		try {
			Thread.sleep(milliSeconds);
		} catch (Throwable t) {
			handleException(t);
		}
	}

	public void dismissSecurityCertificateWarning(String pageTitle) {
		getDriver().navigate().to("javascript:document.getElementById('overridelink').click();");
		waitTillPageIsLoadedInSameWindow(pageTitle);
	}

	public void failureerror(String Error) {
		try 
		{
			driver.quit();
		} 
		catch (Throwable t) 
		{
			handleException(t);
		}
	}


	public void handleException(Throwable e) 
	{	
		try 
		{
			if(driver!= null)
			{
				logger.error(e.getLocalizedMessage());
				TakeScreenshot(TESTCASE_NAME);
				driver.quit();
			}
		} catch (Throwable ioe) 
		{
			ioe.printStackTrace();
		}

	}

	public String DecryptText(String EncText)
	{

		String RawText = "";
		byte[] keyArray = new byte[24];
		byte[] temporaryKey;
		String key = "regressionsuite";
		Scanner scanner = new Scanner(System.in);
		String continueStr = "N";

		try
		{
			MessageDigest m = MessageDigest.getInstance("MD5");
			temporaryKey = m.digest(key.getBytes("UTF-8"));           

			if(temporaryKey.length < 24) // DESede require 24 byte length key
			{
				int index = 0;
				for(int i=temporaryKey.length;i< 24;i++)
				{                  
					keyArray[i] =  temporaryKey[index];
				}
			}

			Cipher c = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyArray, "DESede"), new IvParameterSpec(sharedvector));
			byte[] decrypted = c.doFinal(Base64.decodeBase64(EncText));   

			RawText = new String(decrypted, "UTF-8");                    
		}
		catch(Throwable t)
		{
			JOptionPane.showMessageDialog(null, t);
		}      

		return RawText; 

	}

	public String EncryptText(String RawText)
	{
		String EncText = "";
		byte[] keyArray = new byte[24];
		byte[] temporaryKey;
		String key = "regressionsuite";
		byte[] toEncryptArray = null;

		try
		{

			toEncryptArray =  RawText.getBytes("UTF-8");        
			MessageDigest m = MessageDigest.getInstance("MD5");
			temporaryKey = m.digest(key.getBytes("UTF-8"));

			if(temporaryKey.length < 24) 
			{
				int index = 0;
				for(int i=temporaryKey.length;i< 24;i++)
				{                   
					keyArray[i] =  temporaryKey[index];
				}
			}        

			Cipher c = Cipher.getInstance("DESede/CBC/PKCS5Padding");            
			c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyArray, "DESede"), new IvParameterSpec(sharedvector));            
			byte[] encrypted = c.doFinal(toEncryptArray);            
			EncText = Base64.encodeBase64String(encrypted);

		}
		catch(Throwable t)
		{
			JOptionPane.showMessageDialog(null, t);
		}

		return EncText;        
	}

}
