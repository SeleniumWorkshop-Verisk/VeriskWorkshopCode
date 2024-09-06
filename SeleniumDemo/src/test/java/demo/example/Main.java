package demo.example;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.MediaEntityBuilder;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Main {
    static WebDriver driver;
    static ExtentReports extent;
    static ExtentTest test;
    private static JavascriptExecutor js;

    public static void main(String[] args) throws IOException, InterruptedException {
        try{
            initializeReport();
            setupDriver();
            navigateToSite();
            performLogin();
            verifyLogin();
            searchAndSelectItem();
            handleMultipleWindows();
            performCheckout();
            handleAlert();
        }
        catch(Exception e){
            test.fatal(e.toString());
        }
        finally{
            cleanup();
        }
    }

    public static void initializeReport() {
        // Generate timestamp for report and screenshots
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "\\target\\ExtentReports\\ExtentReport.html");
        htmlReporter.config().setDocumentTitle("Automation Report - " + timeStamp);
        htmlReporter.config().setReportName("Login Test Report - " + timeStamp);
        htmlReporter.config().setTheme(Theme.STANDARD);

        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        test = extent.createTest("E-commerce Login Test", "Sample test for login functionality");
    }

    public static void setupDriver() {
        WebDriverManager.chromedriver().clearDriverCache().driverVersion("128.0.6613.120").setup();
        driver = new ChromeDriver();
        test.info("Browser launched");
        driver.manage().window().maximize();
        test.info("Window maximized");
        js = (JavascriptExecutor) driver;
    }

    public static void navigateToSite() {
        driver.get("https://ecommerce.tealiumdemo.com");
        String title = driver.getTitle();
        System.out.println("Navigated to site: " + title);
        test.info("Navigated to ecommerce site with title: " + title);
    }

    public static void performLogin() throws InterruptedException {
        WebElement account = driver.findElement(By.xpath("//*[@data-target-element = \"#header-account\"]"));
        account.click();
        test.pass("Account clicked");

        WebElement loginButton = driver.findElement(By.xpath("//*[@title = \"Log In\"]"));
        Thread.sleep(5000);
        loginButton.click();
        test.pass("Login button clicked");

        Thread.sleep(5000);
        WebElement emailTextBox = driver.findElement(By.id("email"));
        WebElement passwordTextBox = driver.findElement(By.id("pass"));

        emailTextBox.sendKeys("vnseleniumdemo@test.com");
        passwordTextBox.sendKeys("Verisk");
        test.info("Entered credentials");
        js.executeScript("window.scrollBy(0, 300)");

        Thread.sleep(5000);
        WebElement submitButton = driver.findElement(By.id("send2"));
        submitButton.click();
        test.pass("Submit button clicked");
    }

    public static void verifyLogin() throws IOException, InterruptedException {
        Thread.sleep(3000);
        String welcomeMessage = driver.findElement(By.className("welcome-msg")).getText();
        System.out.println(welcomeMessage);

        try {
            Assert.assertEquals("WELCOME, VERISK NEPAL!", welcomeMessage.toUpperCase());
            System.out.println("Login Successful, Test Passed");
            test.pass("Login successful");
        } catch (AssertionError e) {
            System.out.println("Login Failed, Test Failed");
            String screenshotPath = takeScreenshot("loginFailure.png");
            test.fail("Login failed: " + e.getMessage(), MediaEntityBuilder.createScreenCaptureFromBase64String(captureScreenshot()).build());
            extent.flush();
            throw e;
        }
    }

    public static void searchAndSelectItem() throws IOException {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        WebElement searchField = driver.findElement(By.xpath("//input[@id='search']"));
        searchField.click();
        searchField.sendKeys("shirt");
        searchField.sendKeys(Keys.ENTER);
        test.info("Searched for 'shirt'");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Core Striped Sport Shirt')]")));
        js.executeScript("window.scrollBy(0, 100)");
        WebElement plaidShirt = driver.findElement(By.xpath("//a[contains(text(),'Core Striped Sport Shirt')]"));
        plaidShirt.click();
        test.pass("Core Striped Sport Shirt selected");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='h1' and text()='Core Striped Sport Shirt']")));
        WebElement headingCheck = driver.findElement(By.xpath("//span[text()='Core Striped Sport Shirt']"));
        Assert.assertTrue(headingCheck.getText().equalsIgnoreCase("Core Striped Sport Shirt"));
        test.pass("Verified correct shirt selected");

        takeScreenshot("ShirtSelected.png");
    }

    public static void handleMultipleWindows() {
        js.executeScript("window.scrollBy(0, 1000)");
        String parentWindowHandle = driver.getWindowHandle();

        WebElement footerMyAccount = driver.findElement(By.xpath("//div[@class='block-title']/following-sibling::ul[1]//a[@title='My Account']"));
        String openInNewTab = Keys.chord(Keys.CONTROL, Keys.RETURN);
        footerMyAccount.sendKeys(openInNewTab);

        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        switchToNewWindow(parentWindowHandle);

        driver.switchTo().window(parentWindowHandle);
        js.executeScript("window.scrollBy(0, -1000)");
    }

    public static void switchToNewWindow(String parentWindowHandle) {
        Set<String> allWindowHandles = driver.getWindowHandles();
        for (String windowHandle : allWindowHandles) {
            if (!windowHandle.equals(parentWindowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
    }

    public static void performCheckout() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        WebElement cart = driver.findElement(By.xpath("//*[@data-target-element = '#header-cart']"));
        cart.click();
        test.info("Cart clicked");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@title='Checkout' and contains(@class, 'button') and contains(@class, 'checkout-button')]")));
        WebElement checkout = driver.findElement(By.xpath("//a[@title='Checkout' and contains(@class, 'button') and contains(@class, 'checkout-button')]"));
        checkout.click();

        completeBillingAndShipping(wait);
    }

    public static void completeBillingAndShipping(WebDriverWait wait) throws InterruptedException {
        WebElement billingContinue = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(@onclick, 'billing.save()')]")));
        billingContinue.click();
        js.executeScript("window.scrollBy(0, 400)");

        WebElement shippingMethodContinue = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(@onclick, 'shippingMethod.save()')]")));
        shippingMethodContinue.click();

    }

    public static void handleAlert() {
        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        System.out.println("Alert Text: " + alertText);
        test.info("Alert displayed with text: " + alertText);
        alert.accept();
        test.pass("Alert accepted");
    }

    public static void cleanup() throws IOException {
        takeScreenshot("FinalState.png");
        driver.quit();
        test.info("Driver quit");
        extent.flush();
    }

    public static String captureScreenshot() {
        TakesScreenshot screenshot = (TakesScreenshot) driver;
        System.out.println("Screenshot taken successfully");
        return screenshot.getScreenshotAs(OutputType.BASE64);
    }

    public static String takeScreenshot(String fileName) {
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String screenshotDir = System.getProperty("user.dir") + "/target/Screenshots/";
        createDirectory(screenshotDir);
        String filePath = screenshotDir + fileName;
        try {
            FileUtils.copyFile(srcFile, new File(filePath));
            test.addScreenCaptureFromPath(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }

    public static void createDirectory(String dirPath) {
        File directory = new File(dirPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
}