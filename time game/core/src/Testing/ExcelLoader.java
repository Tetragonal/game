package Testing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mygdx.time.manager.FileReader;

public class ExcelLoader {
	public static final String SPREADSHEET_LOCATION = "assets/place excel spreadsheet here/Srugs exell.xlsx";
	
	public static void main(String args[]) throws IOException{
		loadEntities();
		loadProjectiles();
		loadAttacks();
    }
	
	public static String getCellValue(Cell cell){
		if(cell != null){
	        switch (cell.getCellType()) {
	        case Cell.CELL_TYPE_STRING:
	            return cell.getStringCellValue();
	        case Cell.CELL_TYPE_NUMERIC:
	        	return "" + cell.getNumericCellValue();
	        case Cell.CELL_TYPE_BOOLEAN:
	        	return "" + cell.getBooleanCellValue();
	        default :
	        	return "";
	        }
		}
		return "";
	}
	
	public static void loadProjectiles() throws IOException{
		File myFile = new File(SPREADSHEET_LOCATION);
        FileInputStream fis = new FileInputStream(myFile);

        // Finds the workbook instance for XLSX file
        XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
       
        // Return 4th sheet from the XLSX workbook
        XSSFSheet mySheet = myWorkBook.getSheetAt(3);
       
        // Get iterator to all the rows in current sheet
        Iterator<Row> rowIterator = mySheet.iterator();
       
        //ignore first row
        rowIterator.next();
        
        
        // Traversing over each row of XLSX file
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            
            if(getCellValue(row.getCell(0)).equals("")) break; //if blank row

    		String path = "assets/projectiles/";
    		path += row.getCell(0).getStringCellValue() + ".json";
    		
    		JSONObject obj = new JSONObject();
    		obj.put("name", getCellValue(row.getCell(0)));
    		obj.put("sprite", getCellValue(row.getCell(1)));
    		obj.put("type", getCellValue(row.getCell(2)));
    		obj.put("type parameter", getCellValue(row.getCell(3)));
    		obj.put("airborne", getCellValue(row.getCell(4)));
    		obj.put("base damage", getCellValue(row.getCell(5)));
    		obj.put("damage multi", getCellValue(row.getCell(6)));
    		obj.put("on hit", getCellValue(row.getCell(7)));
    		obj.put("on hit parameter", getCellValue(row.getCell(8)));
    		obj.put("on expire", getCellValue(row.getCell(9)));
    		obj.put("on expire parameter", getCellValue(row.getCell(10)));
    		
    		
    		try {
    			FileReader.writeFile(path, obj.toString(4));
    		} catch (JSONException | IOException e) {
    			e.printStackTrace();
    		}
        }
	}
	
	public static void loadAttacks() throws IOException{
		File myFile = new File("assets/place excel spreadsheet here/Srugs exell.xlsx");
        FileInputStream fis = new FileInputStream(myFile);

        // Finds the workbook instance for XLSX file
        XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
       
        // Return 3rd sheet from the XLSX workbook
        XSSFSheet mySheet = myWorkBook.getSheetAt(2);
       
        // Get iterator to all the rows in current sheet
        Iterator<Row> rowIterator = mySheet.iterator();
       
        //ignore first row
        rowIterator.next();
        
        
        // Traversing over each row of XLSX file
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            
            if(getCellValue(row.getCell(0)).equals("")) break; //if blank row
    		String path = "assets/attacks/";
    		path += row.getCell(0).getStringCellValue() + ".json";
    		
    		JSONObject obj = new JSONObject();
    		obj.put("name", getCellValue(row.getCell(0)));
    		obj.put("sprite", getCellValue(row.getCell(1)));
    		obj.put("type", getCellValue(row.getCell(2)));
    		obj.put("type parameter", getCellValue(row.getCell(3)));
    		obj.put("airborne", getCellValue(row.getCell(4)));
    		obj.put("damage multi", getCellValue(row.getCell(5)));
    		obj.put("on hit", getCellValue(row.getCell(6)));
    		obj.put("on hit parameter", getCellValue(row.getCell(7)));
    		obj.put("on expire", getCellValue(row.getCell(8)));
    		obj.put("on expire parameter", getCellValue(row.getCell(9)));
    		
    		
    		try {
    			FileReader.writeFile(path, obj.toString(4));
    		} catch (JSONException | IOException e) {
    			e.printStackTrace();
    		}
        }
	}
	
	public static void loadEntities() throws IOException{
		File myFile = new File("assets/place excel spreadsheet here/Srugs exell.xlsx");
        FileInputStream fis = new FileInputStream(myFile);

        // Finds the workbook instance for XLSX file
        XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
       
        // Return 2nd sheet from the XLSX workbook
        XSSFSheet mySheet = myWorkBook.getSheetAt(1);
       
        // Get iterator to all the rows in current sheet
        Iterator<Row> rowIterator = mySheet.iterator();
       
        //ignore first row
        rowIterator.next();
        
        
        // Traversing over each row of XLSX file
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            
            if(getCellValue(row.getCell(0)).equals("")) break; //if blank row

    		String path = "assets/entities/";
    		path += row.getCell(0).getStringCellValue() + ".json";
    		
    		JSONArray category = new JSONArray((Arrays.asList(getCellValue(row.getCell(3)).split(", "))));
    		JSONArray mask = new JSONArray((Arrays.asList(getCellValue(row.getCell(4)).split(", "))));
    		JSONArray attacks = new JSONArray((Arrays.asList(getCellValue(row.getCell(7)).split(", "))));
    		
    		JSONObject obj = new JSONObject();
    		obj.put("name", getCellValue(row.getCell(0)));
    		obj.put("sprite", getCellValue(row.getCell(1)));
    		obj.put("airborne", getCellValue(row.getCell(2)));
    		obj.put("category", category);
    		obj.put("mask", mask);
    		obj.put("health", getCellValue(row.getCell(5)));
    		obj.put("base attack", getCellValue(row.getCell(6)));
    		obj.put("attacks", attacks);
    		obj.put("brain", getCellValue(row.getCell(8)));
    		obj.put("brain parameter", getCellValue(row.getCell(9)));
    		obj.put("base speed", getCellValue(row.getCell(10)));
    		
    		try {
    			FileReader.writeFile(path, obj.toString(4));
    		} catch (JSONException | IOException e) {
    			e.printStackTrace();
    		}
        }
	}
}


