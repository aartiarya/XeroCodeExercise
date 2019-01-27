/**
 * 
 */
package com.aartitest.selenium.objectids;

import org.openqa.selenium.By;

/**
 * @author Aarti Arya
 * @purpose This interface contains all the objects used for Xero Login
 */
public interface LoginPage_ObjectIds {
	
	
	//Login Page Identifiers
		By GO_TO_LOGIN = By.xpath("//li[2]/a");
		By EMAILADDRESS = By.xpath("//input[@id='email']");
		By PASSWORD = By.xpath("//input[@id='password']");
		By LOGIN = By.xpath("//button[@id='submitButton']");
		By TWOFACTORAUTHENTICATIONCODE = By.cssSelector(".xui-textinput--input");
		By CONTINUEBUTTON = By.cssSelector(".xui-button-main");		

	
}
