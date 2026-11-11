package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import utils.ConfigReader;

public class LoginPageTest extends BaseTest {

    // Read from config.properties
    String username = ConfigReader.get("username");
    String password = ConfigReader.get("password");

    @Test
    public void validLogin() {
        DashboardPage dashboard = new LoginPage(driver).loginAs(username, password);
        Assert.assertTrue(dashboard.isOnPage("Dashboard"));
    }

    @Test
    public void invalidLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterUsername(username);
        loginPage.enterPassword("wrong123");
        loginPage.clickLogin();
        Assert.assertEquals(loginPage.getErrorMessage(), "Invalid credentials");
    }

    @Test
    public void openPimPage() {
        DashboardPage dashboard = new LoginPage(driver).loginAs(username, password);
        dashboard.openPim();
        Assert.assertTrue(dashboard.isOnPage("PIM"));
    }

    @Test
    public void openAdminPage() {
        DashboardPage dashboard = new LoginPage(driver).loginAs(username, password);
        dashboard.openAdmin();
        Assert.assertTrue(dashboard.isOnPage("Admin"));
    }

    @Test
    public void logoutTest() {
        DashboardPage dashboard = new LoginPage(driver).loginAs(username, password);
        Assert.assertTrue(dashboard.isOnPage("Dashboard"));
        LoginPage loginPage = dashboard.logout();
        Assert.assertTrue(loginPage.isLoginPageShown());
    }

    @DataProvider(name = "invalidLogins")
    public Object[][] invalidLogins() {
        return new Object[][] {
            {"Admin", "wrong123"},
            {"wronguser", "admin123"},
            {"wronguser", "wrong123"}
        };
    }

    @Test(dataProvider = "invalidLogins")
    public void invalidLoginWithData(String user, String pass) {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterUsername(user);
        loginPage.enterPassword(pass);
        loginPage.clickLogin();
        Assert.assertEquals(loginPage.getErrorMessage(), "Invalid credentials");
    }
}