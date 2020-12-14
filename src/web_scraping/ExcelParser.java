package web_scraping;

import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;

public class ExcelParser {
	
//	public Movie[] readExcel() throws Exception{
//		
//		XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream("lib/movie_index.xlsx"));
//		XSSFSheet sheet = wb.getSheetAt(0); 
//		int rows = sheet.getLastRowNum();
//		Movie result[] = new Movie[rows];
//		try {
//		for(int i = 1; i <= rows; ++i) {
//			//Get Excel cells
//			XSSFRow row = sheet.getRow(i);
//			XSSFCell idCell = row.getCell(0);
//			XSSFCell linkCell = row.getCell(1);
//			XSSFCell titleCell = row.getCell(2);
//			XSSFCell scoreCell = row.getCell(3);
//			XSSFCell genreCell = row.getCell(4);
//			XSSFCell posterCell = row.getCell(5);
//			
//			String id, link, title, genre, poster;
//			float score;
//			int year;
//			
//			//Get cells values
//			id = Integer.toString((int)(idCell.getNumericCellValue()));
//			link = linkCell.getStringCellValue();
//			title = titleCell.getStringCellValue();
//			
//			//Extract publication year from title
//			String parts[] = title.split(" \\(");
//			title = parts[0];
//			parts = parts[1].split("\\)");
//			year = Integer.parseInt(parts[0]);
//			
//			//Get score cell value
//			score = (float) scoreCell.getNumericCellValue();
//			
//			//Extract genre text from cell value
//			genre = genreCell.getStringCellValue();
//			String genreParts[] = genre.split("\\|");
//			genre = "";
//			for(int j = 0; j<genreParts.length; ++j) {
//				if(j != (genreParts.length -1)) {
//					genre += (genreParts[j] + " ");
//				} else {
//					genre += genreParts[j];
//				}
//			}
//			
//			//Get poster cell value
//			poster = posterCell.getStringCellValue();
//			
//			//Create Movie Object with the information
//			Movie movieObj = new Movie(id, link, title, genre, poster, score, year);
//			result[i-1] = movieObj;
//		}
//		
//		wb.close();
//		return result;
//		} catch(Exception e) {
//
//		}
//		return result;
//	}
//	
	public String[] getIds() throws Exception{
		XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream("lib/movie_index1_def.xlsx"));
		wb.setMissingCellPolicy(Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
		XSSFSheet sheet = wb.getSheetAt(0); 
		int rows = sheet.getLastRowNum();
		System.out.println(rows);
		String movieIds[] = new String[rows];
		for(int i= 1; i<=rows; ++i) {
			XSSFRow row = sheet.getRow(i);
			System.out.println("Row: " + i);
			XSSFCell idCell = row.getCell(0);
			if (idCell.getCellType() == CellType.NUMERIC) {
				movieIds[i-1] = Integer.toString((int)idCell.getNumericCellValue());
			} else {
				movieIds[i-1] = "0";
			}
			
		}
		wb.close();
		return movieIds;
	}

}
