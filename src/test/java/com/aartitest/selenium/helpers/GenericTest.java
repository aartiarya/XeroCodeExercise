package com.aartitest.selenium.helpers;

import static java.io.File.separator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class GenericTest
{

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	public static EnvironmentProperties environmentProperties = new EnvironmentProperties();
	public static YamlReader reader;
	public static YamlWriter writer;
	public static Object object;
	public static Map<Object, Object> map;
	public static ArrayList<String> ar;
	public static int SCREENSHOT_NUMBER = 1;
	public static String TESTCASE_NAME = null;
	public static WindowManagingWebDriver driver;
	public static Document document = new Document();
	public static String envirnment = null;
	public static ExtentTest test;
	public static DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	public static Date date = new Date();
	public static String reportDate = dateFormat.format(date);

	public GenericTest()
	{

	}

	public GenericTest(WindowManagingWebDriver driver)
	{
		this.driver = driver;
		this.envirnment = environmentProperties.getCurrentEnvironmentName();
	}

	// *************************Logger***************************************************************

	protected Logger getLogger()
	{
		return logger;
	}

	// *********************************YAML**********************************************************

	public static Map init(String file)
	{
		try
		{
			reader = new YamlReader(new FileReader(file));
			object = reader.read();
			map = (Map) object;
			reader.close();
		} catch (Throwable e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return map;
	}

	public String getData(Map<Object, Object> map, String mappingHeader, String sequence)
	{
		String expectedValue = null;
		try
		{

			for (int i = 0; i < map.size(); i++)
			{
				if (map.get(mappingHeader) != null)
				{
					ar = (ArrayList<String>) (map.get(mappingHeader));
					for (int j = 0; j < ar.size(); j++)
					{
						String str = ar.get(j);
						String[] split = str.split(">>");
						String key = split[0];
						String val = split[1];
						if (key.equals(sequence))
						{
							expectedValue = val;
							// reader.close();
							break;
						}
					}
				}
			}
		} catch (Throwable t)
		{
			t.printStackTrace();
		}
		return expectedValue;
	}

	public void writeData(String file, Map<Object, Object> map, String mappingHeader, String sequence,
			String sequenceValue)
	{
		try
		{
			/*
			 * reader = new YamlReader(new FileReader(file)); object =
			 * reader.read(); map = (Map)object;
			 */

			for (int i = 0; i < map.size(); i++)
			{
				if (map.get(mappingHeader) != null)
				{
					ar = (ArrayList<String>) map.get(mappingHeader);
					for (int j = 0; j < ar.size(); j++)
					{
						String str = ar.get(j).toString();
						String[] split = str.split(">>");
						String key = split[0];
						// String val = split[1];
						if (key.equals(sequence))
						{
							ar.set(j, key + ">>" + sequenceValue);
							// reader.close();
							break;
						}
					}
				}
			}
			writer = new YamlWriter(new FileWriter(file));
			writer.write(map);
			writer.close();
		} catch (Throwable t)
		{
			t.printStackTrace();
		}
	}

	// ****************************************Screenshot*******************************************
	public void ScreenshotToPDf(String Testcase)
	{
		  document=new Document(); String currentEnvironment = new
		  EnvironmentProperties().getCurrentEnvironmentName();
		  File srcDirTarget = new File(System.getProperty("user.home")+ getProperty("path")+ separator + currentEnvironment + separator +
		  "Screenshot_" + reportDate); 
		  File destDirReport = new File(System.getProperty("user.home") + getProperty("path") +separator + currentEnvironment + separator +
		  "ExtentReport_" + reportDate + separator + "Screenshot_" + reportDate);

		copyFilesToDrive(srcDirTarget, destDirReport);

		String path = System.getProperty("user.home")+ getProperty("path") + separator + currentEnvironment + separator + "Screenshot_"
				+ reportDate + separator + Testcase + separator;
		// int count = new File(path).list().length;
		int count = FilesCount(path);
		System.out.println("count" + count);
		try
		{
			PdfWriter.getInstance(document, new FileOutputStream(path + Testcase + ".pdf"));
		} catch (FileNotFoundException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (DocumentException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		document.open();
		Font font1 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
		for (int i = 1; i <= count; i++)
		{
			String namefile = path + Testcase + "_" + i + ".jpg";
			System.out.println("namefile" + namefile);
			try
			{
				if ((i % 2 == 1) && (i > 1))
				{
					document.newPage();
				}
				document.add(new Paragraph(Testcase + "_" + i, font1));

				// document.add(new Paragraph(Testcase+"_"+i+".jpg"));
				document.add(Chunk.NEWLINE);
				// document.add(Chunk.NEWLINE );
				Image image1 = Image.getInstance(namefile);
				image1.scaleAbsolute(500f, 300f);
				document.add(image1);
				document.add(Chunk.NEWLINE);
				new File(namefile).delete();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		document.close();

		
		srcDirTarget = new File(System.getProperty("user.home")+ getProperty("path") + separator + currentEnvironment);
		destDirReport = new File(System.getProperty("user.home") + getProperty("sharePath") + separator + currentEnvironment + separator
				+ "ReportAndScreenshot_" + reportDate);
		copyFilesToDrive(srcDirTarget, destDirReport);

	}

	public void copyFilesToDrive(File srcDir, File destDir)
	{
		try
		{
			System.out.println("srcDir :" + srcDir);
			System.out.println("destDir :" + destDir);
			FileUtils.copyDirectory(srcDir, destDir);

		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void TakeScreenshot(String Testcase)
	{
		try
		{
			File scrFile = driver.getScreenshotAs(OutputType.FILE);

			// now save the screen shot to a file some place
			String currentEnvironment = new EnvironmentProperties().getCurrentEnvironmentName();

			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			Date date = new Date();

			String path = System.getProperty("user.home")+ getProperty("path") + separator + currentEnvironment + separator + "Screenshot_" + reportDate
					+ separator + Testcase + separator;
			
			String namefile = path + Testcase + "_" + SCREENSHOT_NUMBER + ".jpg";

			FileUtils.copyFile(scrFile, new File(namefile));

			String Screenshotpath = "Screenshot_" + reportDate + separator + Testcase + separator + Testcase + "_"
					+ SCREENSHOT_NUMBER + ".jpg";
			test.log(LogStatus.INFO, test.addScreenCapture("./" + Screenshotpath));
			SCREENSHOT_NUMBER++;
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	public int FilesCount(String folderPath) {
		//String namefile=path+Testcase+"_"+i+".jpg";
		System.out.println("folderPath" + folderPath);
		String[] fileNames = new File(folderPath).list();
		//System.out.println("fileNames"+fileNames);
		//System.out.println("fileNames.length"+fileNames.length);
		int total = 0;
		if(!(fileNames==null)){
			
		for (int i = 0; i < fileNames.length; i++) {
			String suffix = ".jpg";
			if (fileNames[i].toLowerCase().endsWith(suffix)) {
				total++;
			}

		}
		}
		//System.out.println("total"+total);
		return total;
	}

	public void setTestcaseName(String Testcase)
	{
		TESTCASE_NAME = Testcase;
	}

	public static String getProperty(String property)
	{
		EnvironmentProperties environmentProperties = new EnvironmentProperties();
		return environmentProperties.getProperty(property);
	}

}
