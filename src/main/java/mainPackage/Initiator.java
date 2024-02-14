package mainPackage;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.WebDriver;

public class Initiator {

	private static final String LOG_FILE = "log4j.properties";

	public static void main(String[] args) throws Exception {

		Logger logger = Logger.getLogger(Initiator.class);
		Properties properties = new Properties();
		properties.load(new FileInputStream(LOG_FILE));
		PropertyConfigurator.configure(properties);

		Utility objExcelFile = new Utility();
		String filePath = System.getProperty("user.dir") + "\\resources";
		List<Map<String, String>> data = objExcelFile.readExcel(filePath, "testData.xlsx", "data");

		for (Map<String, String> rowData : data) {
			WebDriver driver = Driver.launchDriver();
			String action = rowData.get("Action").trim();
			LoginPage loginPage = new LoginPage(driver);
			loginPage.selectRole(rowData.get("Role"));
			loginPage.selectUserCode(rowData.get("UserCode"));
			logger.info("=======Garden:" +rowData.get("Garden")+"======User:" + rowData.get("UserCode") + "===========");
			loginPage.selectPassword(rowData.get("Password"));
			
			
			System.out.println("role: "+rowData.get("Role"));
			
			if(rowData.get("Role").equalsIgnoreCase("Seller"))
			{
			
				loginPage.selectAuctionCenter(rowData.get("AuctionCenter"));
				loginPage.clickLogin();
				String error = loginPage.isLoginSuccess();
				if (error != null) {
					logger.error(error);
					Driver.driverTearDown(driver);
					continue;
				}
				logger.info("login successful");
				HomePage homePage = new HomePage(driver);
				homePage.clickPublicIssuer(driver);
	
				DownloadPage downloadPage = new DownloadPage(driver);
				try {
					downloadPage.selectSaleyear(rowData.get("SaleYear"));
				} catch (Exception e) {
					logger.error("SaleYear "+rowData.get("SaleYear")+" is not present");
					Driver.driverTearDown(driver);
					continue;
				}
				try {
					downloadPage.selectSaleNo(rowData.get("SaleNo"));
				} catch (Exception e) {
					logger.error("SaleNo "+rowData.get("SaleNo")+" is not present");
					Driver.driverTearDown(driver);
					continue;
				}
				if (action.contains("PDF")) {
					downloadPage.selectIssuer();
				} else if (action.contains("Excel")) {
					downloadPage.selectGSTIRN();
				}
	
				int aucIndex = 1;
				while (rowData.get("Auc" + aucIndex) != null) {
					if (action.equals("Download PDF") || action.equals("Download Excel") || action.equals("Upload PDF") || action.equals("Upload Excel") )  {
					String aucValue = rowData.get("Auc" + aucIndex);
					downloadPage.selectAuctionCenter(aucValue);
					downloadPage.clickRefresh();
					}
					if (action.equals("Upload PDF")) {
						logger.info("Start uploading PDF");
						String uploadfilePath = System.getProperty("user.dir") + "\\PDFUpload";
						downloadPage.uploadPDFFile(uploadfilePath);
						Thread.sleep(1000);
						logger.info("Uploading PDF completed");
						//break;
					} else if (action.equals("Upload Excel")) {
						logger.info("Start uploading Excel");
						String uploadfilePath = System.getProperty("user.dir") + "\\ExcelUpload";
						downloadPage.uploadExcelFile(uploadfilePath);
						Thread.sleep(1000);
						logger.info("Uploading Excel completed");
						//break;
					}
					if (action.equals("Download PDF") || action.equals("Download Excel") || action.equals("Download Recipient"))  {
						if (downloadPage.isDataPresent(action)) {
							
							if (action.equals("Download PDF")) {
								downloadPage.clickSelectAll();
								downloadPage.clickDownload();
								logger.info("PDF file downloaded");
							} else if (action.equals("Download Excel")) {
								downloadPage.clickSelectAll();
								downloadPage.clickExportIRN();
								logger.info("Excel file downloaded");
							}
							else if (action.equals("Download Recipient")) {
								if (aucIndex == 1) {
								downloadPage.clickDownload();
								logger.info("Recipient file downloaded");
								}
							}
							
	
							if (action.equals("Download PDF") || action.equals("Download Excel") )  {
								Thread.sleep(5000);
								
								String downloadPath = System.getProperty("user.dir") + "\\downloadFiles";
								File directoryPath = new File(downloadPath);
								String contents[] = directoryPath.list();
								String oldFileName = contents[0];
								String newFileName = rowData.get("Garden") + "_" + rowData.get("SaleNo") + "_" + rowData.get("Auc" + aucIndex) + "_"
										+ contents[0];
								File oldFile = new File(System.getProperty("user.dir") + "\\downloadFiles\\" + oldFileName);
								File newFile = null;
								
								
								if (action.equals("Download PDF")) {
									File directory = new File(System.getProperty("user.dir") + "\\PDFDownload\\" + rowData.get("SaleNo"));
									if (! directory.exists())
									{
										directory.mkdir();
									}
									newFile = new File(System.getProperty("user.dir") + "\\PDFDownload\\" + rowData.get("SaleNo")+ "\\"+ newFileName);
								} else if (action.equals("Download Excel")) {
									File directory = new File(System.getProperty("user.dir") + "\\ExcelDownload\\" + rowData.get("SaleNo"));
									if (! directory.exists())
									{
										directory.mkdir();
									}
									newFile = new File(System.getProperty("user.dir") + "\\ExcelDownload\\" + rowData.get("SaleNo")+ "\\" + newFileName);
								}
								 
								
								if (newFile.exists() && newFile.isFile()) {
									newFile.delete();
								  }
	
								boolean flag = oldFile.renameTo(newFile);
	
								if (flag == true) {
									logger.info("File successfully renamed for " + rowData.get("Garden") + "_"
											+ rowData.get("Auc" + aucIndex));
								} else {
									logger.info("File rename failed for " + rowData.get("Garden") + "_"
											+ rowData.get("Auc" + aucIndex));
								}
								FileUtils.cleanDirectory(directoryPath); 
	
								logger.info("Download completed for Auction center " + rowData.get("Auc" + aucIndex));
								
							}
							
							if (action.equals("Download Recipient") )  {
								if (aucIndex == 1) {
									Thread.sleep(5000);
								String downloadPath = System.getProperty("user.dir") + "\\downloadFiles";
								File directoryPath = new File(downloadPath);
								String contents[] = directoryPath.list();
								 if (contents != null)
								 {
									 String oldFileName = contents[0];
										String newFileName = rowData.get("Garden") + "_" + contents[0];
										File oldFile = new File(System.getProperty("user.dir") + "\\downloadFiles\\" + oldFileName);
										File newFile = null;
										File directory = new File(System.getProperty("user.dir") + "\\RecipientDownload\\" + rowData.get("SaleNo"));
										if (! directory.exists())
										{
											directory.mkdir();
										}
										
										newFile = new File(System.getProperty("user.dir") + "\\RecipientDownload\\" + rowData.get("SaleNo")+ "\\" + newFileName);
										if (newFile.exists() && newFile.isFile()) {
											newFile.delete();
										  }
	
										boolean flag = oldFile.renameTo(newFile);
	
										if (flag == true) {
											logger.info("File successfully renamed for " + rowData.get("Garden") + "_"
													+ rowData.get("Auc" + aucIndex));
										} else {
											logger.info("File rename failed for " + rowData.get("Garden")  );
										}
										FileUtils.cleanDirectory(directoryPath); 
	
										logger.info("Download completed for Auction center "  );
								 }
								
								
							}
							}
							
						} else {
							logger.info("No file present for Auction center "  );
						}
					}
					
					aucIndex = aucIndex + 1;
				}
	
				
			}
			else if(rowData.get("Role").trim().equalsIgnoreCase("Factory Owner"))
			{
				loginPage.clickLogin();
				String error = loginPage.isLoginSuccess();
				if (error != null) {
					logger.error(error);
					Driver.driverTearDown(driver);
					continue;
				}
				logger.info("login successful");
				System.out.println("Factory Owner started");
				String actionFO = rowData.get("Action").trim();
				
				FactoryOwnerPage factoryOwnerPage = new FactoryOwnerPage(driver);
				factoryOwnerPage.selectReportType(actionFO);
				
				String saleYear = rowData.get("SaleYear");
				factoryOwnerPage.selectSaleyear(saleYear);
				String saleNo = rowData.get("SaleNo");
				factoryOwnerPage.selectSaleNo(saleNo);
				
				List<String> auc = new ArrayList<String>();
				int index = 1;
				while(rowData.get("Auc"+index) != null)
				{
					auc.add(rowData.get("Auc"+index));
					index = index + 1;
				}
				System.out.println("Auc: "+auc);
				factoryOwnerPage.selectActionCenter(auc);
				
				factoryOwnerPage.clickRefresh();
				Thread.sleep(2000);
				factoryOwnerPage.clickExport();
				Thread.sleep(10000);
						
				//move download file to new location
				String downloadPath = System.getProperty("user.dir") + "\\downloadFiles";
				File directoryPath = new File(downloadPath);
				String contents[] = directoryPath.list();
				String oldFileName = contents[0];
				System.out.println("oldFileName: "+oldFileName);
				String newFileName = rowData.get("Action") + "_" + rowData.get("Garden") + "_" + rowData.get("SaleNo") + "_" + contents[0];
				System.out.println("newFileName: "+newFileName);
				File oldFile = new File(System.getProperty("user.dir") + "\\downloadFiles\\" + oldFileName);
				File newFile = null;
				

					File directory = new File(System.getProperty("user.dir") + "\\FODownload");
					if (! directory.exists())
					{
						boolean f = directory.mkdir();
					}
					newFile = new File(System.getProperty("user.dir") + "\\FODownload\\"+ newFileName);
	
				if (newFile.exists() && newFile.isFile()) {
					newFile.delete();
				  }

				boolean flag = oldFile.renameTo(newFile);
				Thread.sleep(2000);

			}
			Driver.driverTearDown(driver);
		}
		
		logger.info("All tasks completed");
		JFrame jFrame = new JFrame();
        JOptionPane.showMessageDialog(jFrame, "All tasks completed");
        System.exit(0);
	}

}
