package mainPackage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class LoginPage {
	
	WebDriver driver = null;
	
	public LoginPage(WebDriver driver)
	{
		this.driver = driver;
	}
	
	public void selectRole(String rl)
	{
		String rolexpath = "//td[contains(text(),'Role')]/following-sibling::td/select";
		Select role = new Select(driver.findElement(By.xpath(rolexpath)));
		role.selectByVisibleText(rl.trim());
	}
	
	public void selectUserCode(String ucode)
	{
		String userCodeXpath = "//td[./span[contains(text(),'User Code')]]/following-sibling::td/input";
		driver.findElement(By.xpath(userCodeXpath)).sendKeys(ucode.trim());
	}
	
	public void selectPassword(String pass)

	{
		String passwordXpath = "//td[./span[contains(text(),'Password')]]/following-sibling::td/input";
		driver.findElement(By.xpath(passwordXpath)).sendKeys(pass.trim());
	}
	
	public void selectAuctionCenter(String auc)
	{
		String auctionCenterxpath = "//td[./span[contains(text(),'Auction Center')]]/following-sibling::td/select";
		Select auctionCenter = new Select(driver.findElement(By.xpath(auctionCenterxpath)));
		auctionCenter.selectByVisibleText(auc.trim());
	}
	
	public void clickLogin()
	{
		String loginXpath = "//input[@type='submit']";
		driver.findElement(By.xpath(loginXpath)).click();
	}
	
	public String isLoginSuccess()
	{
		String errorXpath = "//tr[./td/input[@type='submit']]/following-sibling::tr[2]/td/span";
		String error = null;
		String title = driver.getTitle();
		System.out.println("title: " +title);
		if(title.contains("Tea Board of India"))
		{
			error = driver.findElement(By.xpath(errorXpath)).getText();
			return error;
		}
		else
		{
			System.out.println("login successfull");
			return null;
		}
//		try {
//			error = driver.findElement(By.xpath(errorXpath)).getText();
//		} catch (Exception e) {
//			System.out.println("login successfull");
//			return null;
//		}
//		return error;
	}
}
