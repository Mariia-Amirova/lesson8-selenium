package net.absoft;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import static org.testng.Assert.*;

public class LoginPanelTest {

    private WebDriver driver;
    private static final String ERROR_MESSAGE_LOGIN =
            "Epic sadface: Username and password do not match any user in this service";
    private static final String ERROR_MESSAGE_LOCKED_OUT_USER =
            "Epic sadface: Sorry, this user has been locked out.";

    @BeforeSuite
    public void downloadDriver() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeMethod
    public void setUp() {
        System.setProperty("webdriver.chrome.driver",
                "C:\\Users\\mariia.amirova\\Downloads\\chromedriver_win32\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://www.saucedemo.com/");
    }

    @Test
    public void testSuccessfulAuthorisation() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");

        driver.findElement(By.cssSelector("[data-test='login-button']")).click();

        assertFalse(driver.findElements(By.id("shopping_cart_container")).isEmpty(),
                "User isn't logged in, shopping cart icon not found");
    }

    @Test
    public void testLoginWithWrongPassword() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce123");

        driver.findElement(By.cssSelector("[data-test='login-button']")).click();

        WebElement element = driver.findElement(By.cssSelector("[data-test='error']"));

        assertEquals(element.getText(), ERROR_MESSAGE_LOGIN,
                "Error message does not match {ERROR_MESSAGE_LOGIN}");
    }

    @Test
    public void testLockedOutUser() {
        driver.findElement(By.id("user-name")).sendKeys("locked_out_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");

        driver.findElement(By.cssSelector("[data-test='login-button']")).click();

        WebElement element = driver.findElement(By.cssSelector("[data-test='error']"));

        assertEquals(element.getText(), ERROR_MESSAGE_LOCKED_OUT_USER,
                "Error message does not match {ERROR_MESSAGE_LOCKED_OUT_USER}");
    }

    @AfterMethod
    public void afterClass() {
        driver.quit();
    }
}
