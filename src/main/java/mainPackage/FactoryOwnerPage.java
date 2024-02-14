package mainPackage;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class FactoryOwnerPage {
	WebDriver driver = null;
	Logger logger = null;

	public FactoryOwnerPage(WebDriver driver) {
		this.driver = driver;
		logger = Logger.getLogger(Initiator.class);
	}
	
	public void selectReportType(String report)
	{
		String reportxpath = "//a[contains(.,'Reports')]";
		String reportNameXpath = "//a[contains(.,'"+ report +"')]";
		
		Actions actions = new Actions(driver);
		actions.moveToElement(driver.findElement(By.xpath(reportxpath))).click(driver.findElement(By.xpath(reportNameXpath))).build().perform();
	}
	
	public void selectSaleyear(String syr) {
		String saleYearxpath = "//td[./span[contains(text(),'Sale Year')]]/following-sibling::td[1]/select";
		Select saleYear = new Select(driver.findElement(By.xpath(saleYearxpath)));
		saleYear.selectByVisibleText(syr.trim());
	}
	
	public void selectSaleNo(String sno) {
		String saleNoxpath = "//td[./span[contains(text(),'Sale No')]]/following-sibling::td[1]/select";
		Select saleNo = new Select(driver.findElement(By.xpath(saleNoxpath)));
		saleNo.selectByVisibleText(sno.trim());
	}
	
	public void selectActionCenter(List<String> auc) throws InterruptedException
	{
		
		for(String aucCenter:auc)
		{
			String auctionCenterxpath = "//td[./span[contains(text(),'Auction Centers')]]/following-sibling::td[1]/input";
			driver.findElement(By.xpath(auctionCenterxpath)).click();
			Thread.sleep(2000);
			String aucValueXpath = "//table[@id='MainContent_filterControlID_cmbParticipant']//td[.//label[contains(.,'"+aucCenter+"')]]/input";
			driver.findElement(By.xpath(aucValueXpath)).click();
			Thread.sleep(2000);
		}
	}
	
	public void clickRefresh() {

		String RefreshXpath = "//input[@value='Refresh']";
		driver.findElement(By.xpath(RefreshXpath)).click();
	}
	
	public void clickExport() {

		String ExportXpath = "//input[@value='Export']";
		driver.findElement(By.xpath(ExportXpath)).click();
	}

}
