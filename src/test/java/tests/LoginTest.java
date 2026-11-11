package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.time.Duration;

public class LoginTest {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://opensource-demo.orangehrmlive.com/");
    }

    // Helper: types username and password, then clicks Login
    public void login(String username, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")))
            .sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    // Test 1: correct password shows the Dashboard
    @Test
    public void validLogin() {
        login("Admin", "admin123");
        WebElement heading = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h6[text()='Dashboard']")));
        Assert.assertTrue(heading.isDisplayed());
    }

    // Test 2: wrong password shows an error
    @Test
    public void invalidLogin() {
        login("Admin", "wrong123");
        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[text()='Invalid credentials']")));
        Assert.assertEquals(error.getText(), "Invalid credentials");
    }

    // Test 3: open the PIM page (uses a fluent wait)
    @Test
    public void openPimPage() {
        login("Admin", "admin123");

        Wait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(15))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class);

        WebElement pimMenu = fluentWait.until(d -> d.findElement(By.xpath("//span[text()='PIM']")));
        pimMenu.click();

        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h6[text()='PIM']")));
        Assert.assertEquals(header.getText(), "PIM");
    }

    // Test 4: open the Admin page (uses a normal explicit wait)
    @Test
    public void openAdminPage() {
        login("Admin", "admin123");

        WebElement adminMenu = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[text()='Admin']")));
        adminMenu.click();

        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h6[text()='Admin']")));
        Assert.assertEquals(header.getText(), "Admin");
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}