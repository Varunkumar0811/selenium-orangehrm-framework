package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class DashboardPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By pimMenu      = By.xpath("//span[text()='PIM']");
    private By adminMenu    = By.xpath("//span[text()='Admin']");
    private By userDropdown = By.cssSelector("span.oxd-userdropdown-tab");
    private By logoutLink   = By.xpath("//a[text()='Logout']");

    // Constructor
    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Actions
    public void openPim() {
        wait.until(ExpectedConditions.elementToBeClickable(pimMenu)).click();
    }

    public void openAdmin() {
        wait.until(ExpectedConditions.elementToBeClickable(adminMenu)).click();
    }

    // NEW: logout and go back to the login page
    public LoginPage logout() {
        wait.until(ExpectedConditions.elementToBeClickable(userDropdown)).click();
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
        return new LoginPage(driver);
    }

    // Returns true if the page heading shows this name
    public boolean isOnPage(String pageName) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//h6[text()='" + pageName + "']")));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}