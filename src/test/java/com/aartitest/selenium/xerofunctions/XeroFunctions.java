package com.aartitest.selenium.xerofunctions;

import java.util.Map;
import com.aartitest.selenium.objectids.*;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aartitest.selenium.helpers.Page;
import com.aartitest.selenium.helpers.WindowManagingWebDriver;
import static com.aartitest.selenium.objectids.LoginPage_ObjectIds.*;
import static com.aartitest.selenium.objectids.XeroDashboard_ObjectIds.*;
import org.jboss.aerogear.security.otp.Totp;

/**
 * This class contains the functions to be used for Xerologin, XeroLogout, perform2wayAuthentication, addBankAccount
 * @author Aarti Arya
 */
public class XeroFunctions extends Page
{

	public static Map<Object, Object> loginMap;
	String loginYaml = getProperty("login");
	public static Map<Object, Object> xeroMap;
	String xeroYaml = getProperty("xero");
	public static String PARENTWINDOW_ID = null;
	
	public XeroFunctions(WindowManagingWebDriver driver)
	{

		super(driver);
		xeroMap = init(xeroYaml);

	}

	/**
	 * This functions is for logging into Xero Application
	 * @param ymlTSRef
	 * @throws Exception
	 */
	public void xeroLogin(String ymlTSRef) throws Exception
	{
		try
		{
			WebDriverWait wait = new WebDriverWait(driver, 3);			
			openApplication(getData(xeroMap, ymlTSRef, "url"));
			TakeScreenshot(TESTCASE_NAME);
			driver.findElement(GO_TO_LOGIN).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(EMAILADDRESS));
			logger.info("Login Page is dislpayed");
			setValueInTextField(EMAILADDRESS, getData(xeroMap, ymlTSRef, "emailAddress"));
			
			setValueInTextField(PASSWORD, getData(xeroMap, ymlTSRef, "password"));
			
			clickLink(LOGIN);
			waitForSeconds(5);
			TakeScreenshot(TESTCASE_NAME);
		
		} catch (Exception e)
		{
			logger.info("Login failed to xero application");
		}

	}
	
	/**
	 * This function is for launching URL and maximize the window
	 * @param url
	 */
	public void openApplication(String url)
	{
		driver.get(url);
		driver.manage().window().maximize();
		PARENTWINDOW_ID = driver.getWindowHandle();
	}
	
	/**
	 * This function is to generate twoWayAuthentication Code using AeroGear Two-Factor Authentication
	 * @param ymlTSRef
	 * @return
	 */
	
	public String twoFactorAuth(String ymlTSRef)
	{	
		String twoFactorCode=null;
		try {
			String otpKeyStr = getData(xeroMap, ymlTSRef, "TwoFactorAuthenticationKey"); // <- this 2FA secret key.

			Totp totp = new Totp(otpKeyStr);
			twoFactorCode = totp.now(); // <- got 2FA coed at this time!
			
			if(!twoFactorCode.isEmpty())
			{
				logger.info("Two factor authentication code is generated: " + twoFactorCode);
			}
			else {
				logger.info("Two factor authentication code is not generated" + twoFactorCode);
			}
		}
		catch (Exception e)
		{
			logger.info("Two factor authentication code is not generated");
		}
		return twoFactorCode;

	}
	
	/**
	 * This function is for getting Two factor authentication pin and entering into the UI 
	 * @param ymlTSRef
	 * @return
	 */
	public Boolean performTwoWayAuth(String ymlTSRef)
	{	
		Boolean flag=false;
		try {
			String towfactorCode = twoFactorAuth(ymlTSRef);
			if(!towfactorCode.isEmpty())
			{
			waitForSeconds(6);
			waitTillPageIsLoadedInSameWindow("Login | Xero Accounting Software");
			
			setValueInTextField(TWOFACTORAUTHENTICATIONCODE, towfactorCode);
			TakeScreenshot(TESTCASE_NAME);
			clickLink(CONTINUEBUTTON);
			waitTillPageIsLoadedInSameWindow("Xero | Dashboard | "+getData(xeroMap, ymlTSRef, "username"));
			if(driver.getTitle().contains("Xero | Dashboard | "+getData(xeroMap, ymlTSRef, "username")))
			{
			flag=true;
			}
			else {
			flag=false;
			TakeScreenshot(TESTCASE_NAME);
			logger.info("Test stopped as Two factor authentication is not generated");
			driver.quit();
			}
			}
			else {
				TakeScreenshot(TESTCASE_NAME);
				logger.info("Test stopped as Two factor authentication is not generated");
				driver.quit();
			}	
		}
		catch (Exception e)
		{
			logger.info("Two factor authentication code is not generated");
		}
		return flag;

	}
	
	/**
	 * This function is for navigating to the 'Bank Account' option from Accounts menu
	 * @param ymlTSRef
	 */
	public void goToBankAccounts(String ymlTSRef)
	{
		try {
			TakeScreenshot(TESTCASE_NAME);
			clickLink(ACCOUNTSMENU);
			waitForSeconds(2);
			clickLink(NAVIGATETOBANKACCOUNT);
			
			waitTillPageIsLoaded("Xero | Bank accounts | "+getData(xeroMap, ymlTSRef, "username"));
			TakeScreenshot(TESTCASE_NAME);
			
		}
		catch (Exception e)
		{
			logger.info("Unable to navigate to Bank Account page");
		}
		
	}
	
	/**
	 * This function is for adding the Bank Account by giving bank Account details
	 * @param ymlTSRef
	 * @return
	 */
	public Boolean addBankAccount(String ymlTSRef)
	{
		Boolean flag=false;
		try {			

			clickLink(CLICKTOADD);
			waitForSeconds(3);
			waitTillPageIsLoaded("Xero | Find your bank | "+getData(xeroMap, ymlTSRef, "username"));
			clickLink(SELECTBANK);
			
			waitForSeconds(3);
			waitTillPageIsLoaded("Xero | Enter your ANZ (AU) account details | "+getData(xeroMap, ymlTSRef, "username"));
			
			setValueInTextField(ACCOUNTNAME, getData(xeroMap, ymlTSRef, "accountName"));
			TakeScreenshot(TESTCASE_NAME);

			selectValueFromDropDownField(ACCOUNTTYPE,getData(xeroMap, ymlTSRef, "accountType"));

			
			setValueInTextField(BSB, getData(xeroMap, ymlTSRef, "bsb"));
			setValueInTextField(ACCOUNTNUMBER, getData(xeroMap, ymlTSRef, "accountNumber"));
			
			TakeScreenshot(TESTCASE_NAME);
			
			driver.findElement(CONTINUEB).click();
			
			waitForSeconds(3);
			
			if(driver.findElement(SUCCESSBANNER).getText().equalsIgnoreCase(getData(xeroMap, ymlTSRef, "username")+" has been added."))
			{
				flag=true;
				logger.info(getData(xeroMap, ymlTSRef, "username")+" Account has been added sucessfully");
				TakeScreenshot(TESTCASE_NAME);
			}		
			else {
				flag=false;
				logger.info("Unable to add the account");
				TakeScreenshot(TESTCASE_NAME);
			}
		}
		catch (Exception e)
		{
			logger.info("Unable to add the Bank Account");
		}
		return flag;
		
	}
	/**
	 * This function is for logging out from Xero
	 * @throws Exception
	 */
	public void xeroLogout() throws Exception
	{
		try
		{
			clickLink(USERMENU);
			waitForSeconds(3);
			clickLink(LOGOUT);
			waitForSeconds(3);
			TakeScreenshot(TESTCASE_NAME);
		
		} catch (Exception e)
		{
			logger.info("Logout failed for xero application");
		}

	}
}