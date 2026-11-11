package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By usernameBox  = By.name("username");
    private By passwordBox  = By.name("password");
    private By loginButton  = By.cssSelector("button[type='submit']");
    private By errorMessage = By.xpath("//p[text()='Invalid credentials']");

    // Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Actions
    public void enterUsername(String username) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameBox)).sendKeys(username);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordBox).sendKeys(password);
    }

    public void clickLogin() {
        driver.findElement(loginButton).click();
    }

    public DashboardPage loginAs(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        return new DashboardPage(driver);
    }

    public String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
    }

    // NEW: true if the login page is showing (username box is visible)
    public boolean isLoginPageShown() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(usernameBox));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}