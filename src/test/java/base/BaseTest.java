package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import utils.ConfigReader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BaseTest {
    protected WebDriver driver;
    protected static ExtentReports extent;
    protected ExtentTest test;

    // Runs ONCE before all tests: creates the report
    @BeforeSuite
    public void startReport() {
        ExtentSparkReporter spark = new ExtentSparkReporter("reports/ExtentReport.html");
        spark.config().setDocumentTitle("OrangeHRM Automation Report");
        spark.config().setReportName("OrangeHRM Selenium Tests");
        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Tester", "Varun Kumar R");
        extent.setSystemInfo("Browser", "Chrome");
    }

    // Runs before EACH test: adds the test to the report, opens Chrome
    @BeforeMethod
    public void setUp(Method method) {
        test = extent.createTest(method.getName());
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(ConfigReader.get("url"));
        test.info("Opened " + ConfigReader.get("url"));
    }

    // Runs after EACH test: writes pass or fail, screenshot on failure, closes Chrome
    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            test.fail(result.getThrowable());
            String path = takeScreenshot(result.getName());
            if (path != null) {
                try {
                    test.addScreenCaptureFromPath(path);
                } catch (Exception e) {
                    test.warning("Could not attach screenshot: " + e.getMessage());
                }
            }
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.pass("Test passed");
        } else {
            test.skip("Test skipped");
        }
        driver.quit();
    }

    // Runs ONCE after all tests: saves the report file
    @AfterSuite
    public void endReport() {
        extent.flush();
    }

    // Saves a screenshot and returns its full path
    public String takeScreenshot(String testName) {
        File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        Path destination = Paths.get("screenshots", testName + "_" + time + ".png");
        try {
            Files.createDirectories(destination.getParent());
            Files.copy(source.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Screenshot saved: " + destination.toAbsolutePath());
            return destination.toAbsolutePath().toString();
        } catch (IOException e) {
            System.out.println("Could not save screenshot: " + e.getMessage());
            return null;
        }
    }
}