package com.aartitest.selenium.objectids;

import org.openqa.selenium.By;

/**
 * @author Aarti Arya
 * @purpose This interface contains all the objects used for adding bank account in Xero application
 */

public interface XeroDashboard_ObjectIds {
	
	By ACCOUNTSMENU = By.xpath("/html/body/div[1]/header/div/ol[1]/li[3]/button");
	By NAVIGATETOBANKACCOUNT = By.xpath("/html/body/div[1]/header/div/ol[1]/li[3]/div/div[2]/div/ol[1]/li[1]/a");
	By BANNER = By.cssSelector("a.xrh-banner");
	By CLICKTOADD = By.linkText("Click here to add one");
	By SELECTBANK = By.xpath("/html/body/div[2]/div/div[2]/section/div[2]/ul/li[1]");
	
	By ACCOUNTNAME = By.xpath("//input[@placeholder='e.g Business Account']");
	By ACCOUNTTYPE = By.xpath("//div[4]/div/div/div/input");
	By BSB = By.xpath("//input[@placeholder='BSB']");
	By ACCOUNTNUMBER = By.xpath("//div/div/div/div/div/div/div/div[2]/div/div/div/input");
	By CONTINUEB = By.xpath("//footer/a[2]/span/span/span");
	By SUCCESSBANNER = By.xpath("//div/div/p");
	By USERMENU = By.xpath("/html/body/div[1]/header/div/ol[2]/li[5]/button");
	By LOGOUT = By.xpath("/html/body/div[1]/header/div/ol[2]/li[5]/div/div[2]/div/ol/li[5]/a");
}
