package mainPackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class Utility {

	public List<Map<String, String>> readExcel(String filePath, String fileName, String sheetName) {

		File file = new File(filePath + "\\" + fileName);
		
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		Workbook workbook = null;

		String fileExtensionName = fileName.substring(fileName.indexOf("."));

		if (fileExtensionName.equals(".xlsx")) {
			try {
				workbook = WorkbookFactory.create(file, "test");
			} catch (EncryptedDocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		else if (fileExtensionName.equals(".xls")) {
			try {
				workbook = new HSSFWorkbook(inputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Sheet sheet = workbook.getSheet(sheetName);
		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
		
		List<Map<String,String>> tableData = new ArrayList<Map<String,String>>();
		for (int i = 1; i < rowCount + 1; i++) {
			Row keyRow = sheet.getRow(0);
			Row valueRow = sheet.getRow(i);
			Map<String,String> rowData = new HashMap<String,String>();
			
			// Create a loop to print cell values in a row
			for (int j = 0; j < keyRow.getLastCellNum(); j++) {
				String key = "";
				String value = "";
				key = keyRow.getCell(j).getStringCellValue();
				Cell cell = valueRow.getCell(j);
				CellType celltype;
				try {
					celltype = cell.getCellType();
				} catch (Exception e) {
					break;
				}
				switch (celltype) {
				case STRING:
					value = cell.getStringCellValue();
					break;
				case NUMERIC:
					value = String.valueOf(cell.getNumericCellValue());
					if(value.contains("."))
					{
						value = value.split("\\.")[0];
					}
					break;
				default:
					break;
				}
				if(!value.isEmpty())
				{
					rowData.put(key, value);
				}
			}
			if(rowData.size()>0)
			{
				tableData.add(rowData);
			}
			
		}
		return tableData;
	}
}
