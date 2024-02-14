package mainPackage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Driver {

	@SuppressWarnings("deprecation")
	public static WebDriver launchDriver()
	{
		// Setting new download directory path
        Map<String, Object> prefs = new HashMap<>();

        // Use File.separator as it will work on any OS
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("download.prompt_for_download", "false");
        prefs.put("download.default_directory", System.getProperty("user.dir") + "\\downloadFiles");

        // Adding cpabilities to ChromeOptions
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--disable-notifications");
        DesiredCapabilities cap = DesiredCapabilities.chrome();
        cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        cap.setCapability(ChromeOptions.CAPABILITY, options);

		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver(cap);
		driver.get("https://www.teaauction.gov.in/teaboardadmin/Home.aspx");
		//driver.get("https://www.teaauction.gov.in/teaboardadmin/(S(4wfbjc14k1p3zdp03tizosni))/Home.aspx");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		return driver;
	}

	public static void driverTearDown(WebDriver driver)
	{
		driver.quit();
		File file = new File(System.getProperty("user.dir") + "\\downloadFiles");
		file.delete();
	}

}
