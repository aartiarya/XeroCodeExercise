package com.aartitest.selenium.tests.xeroProject;

/**
 * This class contains the test script for Adding Bank Account To Xero Application
 * @author Aarti Arya
 */

import static org.junit.Assert.fail;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.aartitest.selenium.helpers.Page;
import com.aartitest.selenium.helpers.WindowManagingWebDriver;
import com.aartitest.selenium.xerofunctions.XeroFunctions;
import com.relevantcodes.extentreports.ExtentReports;

public class AddBankAccountToXero extends Page
{

	public static String url;
	public static String emailAddress;
	public static String password;
	boolean TCflag = false;	
	public static String ymlTSRef = "AddBankAccountToXero";
	WindowManagingWebDriver driver = new WindowManagingWebDriver();
	public String xeroYaml = getProperty("xero");
	public static Map<Object, Object> xeroMap;
	XeroFunctions xero_reuse = new XeroFunctions(driver);
	
	ExtentReports extent;
	
	public AddBankAccountToXero()
	{
		// TODO Auto-generated constructor stub
		xeroMap = init(xeroYaml);
	}

	@Before
	public void init()
	{
		extent = Page.Instance();
		test = extent.startTest("AddBankAccountToXeroTest", "validate that Xero user is able to add ANZ account to his or her Xero account")
				.assignCategory("Module Name : XeroTest");
		test = test.assignAuthor("Aarti Arya");
		driver.manage().deleteAllCookies();
		new Page().setTestcaseName(getClass().getSimpleName());
	}

	/**
	 * @throws Throwable
	 */
	@Test
	public void testAddBankAccountToXero() throws Throwable
	{
		try
		{
			String className = getClass().getSimpleName().toString();
			logger.info("Test case name :" + className);
			
			  xero_reuse.xeroLogin(ymlTSRef);		
			
			  xero_reuse.performTwoWayAuth(ymlTSRef);	  
			
			  xero_reuse.goToBankAccounts(ymlTSRef);
			  
			  TCflag=xero_reuse.addBankAccount(ymlTSRef);
			  
			  xero_reuse.xeroLogout();
			 			 
		}

		catch (Throwable t)
		{
			TCflag = false;
			logger.info("Test is failed", t);
		}
	}

	@After
	public void afterTest() throws Throwable
	{
		driver.quit();
		extent.endTest(test);
		extent.flush();
		extent.close();
		new Page().ScreenshotToPDf(getClass().getSimpleName());
		if (!TCflag)
		{
		fail();
		}
	}

}
