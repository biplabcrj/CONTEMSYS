package mainPackage;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DownloadPage {

	WebDriver driver = null;
	Logger logger = null;

	public DownloadPage(WebDriver driver) {
		this.driver = driver;
		logger = Logger.getLogger(Initiator.class);
	}

	public void selectSaleyear(String syr) {
		String saleYearxpath = "//td[./span[contains(text(),'Sale Year')]]/following-sibling::td/select";
		Select saleYear = new Select(driver.findElement(By.xpath(saleYearxpath)));
		saleYear.selectByVisibleText(syr.trim());
	}

	public void selectSaleNo(String sno) {
		String saleNoxpath = "//td[./span[contains(text(),'Sale No')]]/following-sibling::td/select";
		Select saleNo = new Select(driver.findElement(By.xpath(saleNoxpath)));
		saleNo.selectByVisibleText(sno.trim());
	}

	public void selectIssuer() {
		String issuerXpath = "//input[@value='Issuer']";
		driver.findElement(By.xpath(issuerXpath)).click();
		;
	}
	
	public void selectRecipient() {
		String issuerXpath = "//input[@value='Recipient']";
		driver.findElement(By.xpath(issuerXpath)).click();
		;
	}

	public void selectGSTIRN() {
		String GSTIRNXpath = "//input[@value='GST IRN']";
		driver.findElement(By.xpath(GSTIRNXpath)).click();
		;
	}

	public void selectAuctionCenter(String ac) {
		String acxpath = "//td[./span[contains(text(),'Auction Center')]]/following-sibling::td[1]/select";
		Select aucCen = new Select(driver.findElement(By.xpath(acxpath)));
		aucCen.selectByVisibleText(ac.trim());
	}

	public void clickDownload() {

		String downloadXpath = "//input[@value='Download']";
		driver.findElement(By.xpath(downloadXpath)).click();
	}

	public void clickExportIRN() {

		String ExportIRNXpath = "//input[@value='Export IRN Details']";
		driver.findElement(By.xpath(ExportIRNXpath)).click();
	}

	public boolean isDataPresent(String action) {
		String noRowXpath = null;
		if (action.equals("Download Excel"))
		{
			noRowXpath = "//table[@id='MainContent_GridView1']/tbody/tr[2]/td";
		}
		else if(action.equals("Download PDF"))
		{
			noRowXpath = "//table[@id='MainContent_GridSellerDownload']/tbody/tr[2]/td";
		}
		else if(action.equals("Download Recipient"))
		{
			//noRowXpath = "//table[@id='MainContent_GridSellerDownload']/tbody/tr[2]/td";
		}
		
		if (action.equals("Download PDF") || action.equals("Download Excel") )  {
		int noRows = driver.findElements(By.xpath(noRowXpath)).size();
		if(noRows>1)
		{
			return true;
		}
		else
		{
			return false;
		}
		}
		else {
			return true;
		}
	}

	public void clickRefresh() {
		String refreshXpath = "//input[@value='Refresh']";
		driver.findElement(By.xpath(refreshXpath)).click();
	}

	public void clickSelectAll() {
		String selectAllXpath = "//input[./following-sibling::label[contains(text(),'Select All')]]";
		driver.findElement(By.xpath(selectAllXpath)).click();
	}

	public void chooseFile(String file) {
		String chooseFileXpath = "//input[@type='file']";
		driver.findElement(By.xpath(chooseFileXpath)).sendKeys(file);
	}

	public String clickUpload() {
		String alertMessage = null;
		String uploadXpath = "//input[@value='Upload']";
		driver.findElement(By.xpath(uploadXpath)).click();
		try {
			alertMessage = handleAlert();
			System.out.println(alertMessage);
			return alertMessage;
		} catch (Exception e) {
			return alertMessage;
		}
	}

	public String clickIRNUpload() {
		String alertMessage = null;
		String IRNuploadXpath = "//input[@value='Upload IRN Details']";
		driver.findElement(By.xpath(IRNuploadXpath)).click();
		try {
			alertMessage = handleAlert();
			return alertMessage;
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public String handleAlert() throws InterruptedException {
		Thread.sleep(1000);
		WebDriverWait wait=new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.alertIsPresent());
		String alertMessage = driver.switchTo().alert().getText();
		driver.switchTo().alert().accept();
		return alertMessage;
	}
	
	public void uploadExcelFile(String uploadfilePath)
	{
		File directoryPath = new File(uploadfilePath);
		String fileNames[] = directoryPath.list();
		
		String tableXpath = "//table[@id='MainContent_GridView1']//tr";
		List<WebElement> tableRow = driver.findElements(By.xpath(tableXpath));
		for(int i = 2; i<tableRow.size(); i++)
		{
			String alertMessage = "Upload error";
			String checkBoxXpath = tableXpath + "["+ i + "]/td[1]/input";
			driver.findElement(By.xpath(checkBoxXpath)).click();
			
			String invoiceNoxpath = tableXpath + "["+ i + "]/td[7]";
			String invoiceNo = driver.findElement(By.xpath(invoiceNoxpath)).getText();
			
			for(String fileName:fileNames)
			{
				if(fileName.startsWith(invoiceNo))
				{
					chooseFile(uploadfilePath + "\\" + fileName);
					alertMessage = clickIRNUpload();
					logger.info("Uploading file: "+fileName +" : "+ alertMessage);
					moveToUploaded(uploadfilePath,fileName);
					break;
				}
			}
			driver.findElement(By.xpath(checkBoxXpath)).click();
		}
	}
	
	public void uploadPDFFile(String uploadfilePath)
	{
		
		File directoryPath = new File(uploadfilePath);
		String fileNames[] = directoryPath.list();
		
		String tableXpath = "//table[@id='MainContent_GridSellerDownload']//tr";
		List<WebElement> tableRow = driver.findElements(By.xpath(tableXpath));
		for(int i = 2; i<tableRow.size(); i++)
		{
			String alertMessage = "Upload error";
			String checkBoxXpath = tableXpath + "["+ i + "]/td[1]/input";
			driver.findElement(By.xpath(checkBoxXpath)).click();
			
			String invoiceNoxpath = tableXpath + "["+ i + "]/td[10]";
			String invoiceNo = driver.findElement(By.xpath(invoiceNoxpath)).getText();
			
			for(String fileName:fileNames)
			{
				if(fileName.contains(invoiceNo))
				{
					chooseFile(uploadfilePath + "\\" + fileName);
					alertMessage = clickUpload();
					logger.info("Uploading file: "+fileName +" : "+ alertMessage);
					moveToUploaded(uploadfilePath,fileName);
					break;
				}
			}
			driver.findElement(By.xpath(checkBoxXpath)).click();
		}
	}

	public void moveToUploaded(String filePth, String fileName)
	{
		String uploadedFilePath = System.getProperty("user.dir") + "\\Uploaded";
		File directory = new File(uploadedFilePath);
		if (! directory.exists())
		{
			directory.mkdir();
		}
		
		File fileToMove = new File(filePth + "\\" + fileName);
		fileToMove.renameTo(new File(directory + "\\" +fileName));
		fileToMove.deleteOnExit();
		logger.info(fileName + " moved to Uploaded filder");
	}
}
