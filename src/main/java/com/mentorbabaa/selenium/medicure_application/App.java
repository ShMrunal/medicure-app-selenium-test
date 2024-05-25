package com.mentorbabaa.selenium.medicure_application;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.opentelemetry.exporter.logging.SystemOutLogRecordExporter;

public class App 
{
	private WebDriver driver;

    public void setUp() {
        //System.setProperty("webdriver.chrome.driver", "D:\\Software\\chromedriver-win64\\chromedriver.exe");

        // Set Chrome options for headless mode
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Add headless argument
        App.driver = new ChromeDriver(options);

        App.driver.get("http://65.1.84.157:8084/");
    }
    
    public void testFormSubmission() {
        // Valid input test
        testForm(driver, "John Doe", "1234567890", "john.doe@example.com", "This is a test message.");

        // Invalid input tests
        testForm(driver, "John123", "1234567890", "john.doe@example.com", "This is a test message."); // Invalid name
        testForm(driver, "John Doe", "12345abc", "john.doe@example.com", "This is a test message."); // Invalid mobile number
        testForm(driver, "John Doe", "1234567890", "john.doe@example", "This is a test message."); // Invalid email
        testForm(driver, "John Doe", "1234567890", "john.doe@example.com", ""); // Invalid message
    }

    public void testForm(WebDriver driver, String name, String mobile, String email, String message) {
        driver.navigate().refresh(); // Refresh the page before each test

        WebElement nameField = driver.findElement(By.xpath("//input[@placeholder='Your Name']"));
        WebElement mobileField = driver.findElement(By.xpath("//input[@placeholder='Phone Number']"));
        WebElement emailField = driver.findElement(By.xpath("//input[@placeholder='Email']"));
        WebElement messageField = driver.findElement(By.xpath("//input[@placeholder='Message']"));
        WebElement sendButton = driver.findElement(By.xpath("//button[normalize-space()='SEND']"));

        nameField.clear();
        mobileField.clear();
        emailField.clear();
        messageField.clear();

        if (isValidName(name)) {
            nameField.sendKeys(name);
        } else {
            System.out.println("Invalid name: " + name);
        }

        if (isValidMobile(mobile)) {
            mobileField.sendKeys(mobile);
        } else {
            System.out.println("Invalid mobile number: " + mobile);
        }

        if (isValidEmail(email)) {
            emailField.sendKeys(email);
        } else {
            System.out.println("Invalid email: " + email);
        }

        if (isValidMessage(message)) {
            messageField.sendKeys(message);
        } else {
            System.out.println("Invalid message: " + message);
        }

        sendButton.click();

        // Optionally, verify if the page scrolled to the top
        long scrollPosition = (Long) ((JavascriptExecutor) driver).executeScript("return window.pageYOffset;");
        if (scrollPosition == 0) {
            System.out.println("Page scrolled to the top successfully.");
        } else {
            System.out.println("Page did not scroll to the top.");
        }
    }

    // Validate that the name contains only alphabetic characters and spaces
    public boolean isValidName(String name) {
        return name.matches("[a-zA-Z\\s]+");
    }

    // Validate that the mobile number contains only numeric characters
    public boolean isValidMobile(String mobile) {
        return mobile.matches("\\d+");
    }

    // Validate that the email has a valid format
    public boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    // Validate that the message is not empty
    public boolean isValidMessage(String message) {
        return !message.trim().isEmpty();
    }

    public void tearDown() throws IOException {
    
    // Take screenshot	
	TakesScreenshot scrShot = ((TakesScreenshot)driver);
    File srcFile = scrShot.getScreenshotAs(OutputType.FILE);
    File desFile = new File("success-report.jpg");
    FileUtils.copyFile(srcFile, desFile);
    
    System.out.println("Screenshot is stored at" +desFile.getAbsolutePath());
    	
    	if (driver != null) {
            driver.quit();
        }
        
    }
    
    public static void main( String[] args ) throws IOException
    {
    	App app=new App();
    	app.setUp();   
    	app.testFormSubmission();
    	app.tearDown();
    }
}
