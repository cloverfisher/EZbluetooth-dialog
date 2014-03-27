package com.example.function;



import java.io.File;
import java.io.IOException;
import java.util.Locale;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import android.util.Log;

import com.example.source.Cargo;
import com.example.source.Output;

public class StringToXls {

	 private WritableCellFormat timesBoldUnderline;
	  private WritableCellFormat times;
	  private String inputFile;
	  
	public void setOutputFile(String inputFile) {
	  this.inputFile = inputFile;
	  }

	  public void write(Output output) throws IOException, WriteException {
	    File file = new File(inputFile);
	    Log.e("ysy", inputFile);
	    WorkbookSettings wbSettings = new WorkbookSettings();

	    wbSettings.setLocale(new Locale("en", "EN"));

	    WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
	    workbook.createSheet("Report", 0);
	    WritableSheet excelSheet = workbook.getSheet(0);
	    createLabel(excelSheet);
	    createContent(excelSheet,output);

	    workbook.write();
	    workbook.close();
	  }

	  private void createLabel(WritableSheet sheet)
	      throws WriteException {
	    // Lets create a times font
	    WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
	    // Define the cell format
	    times = new WritableCellFormat(times10pt);
	    // Lets automatically wrap the cells
	    times.setWrap(true);

	    // create create a bold font with unterlines
	    WritableFont times10ptBoldUnderline = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false,
	        UnderlineStyle.SINGLE);
	    timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
	    // Lets automatically wrap the cells
	    timesBoldUnderline.setWrap(true);

	    CellView cv = new CellView();
	    cv.setFormat(times);
	    cv.setFormat(timesBoldUnderline);
	    cv.setAutosize(true);

	    // Write a few headers
	    addCaption(sheet, 0, 0, "Customer #");
	    addCaption(sheet, 1, 0, "ShiptoCostCode");
	    addCaption(sheet, 2, 0, "Branch");
	    addCaption(sheet, 3, 0, "Disc");
	    addCaption(sheet, 4, 0, "Disc Days");
	    addCaption(sheet, 5, 0, "Net Days");
	    addCaption(sheet, 6, 0, "ShiptoNumber");
	    addCaption(sheet, 7,0,"ShiptoName");
	    addCaption(sheet, 8, 0, "ShiptoAddress");
	    addCaption(sheet, 9, 0, "Blank");
	    addCaption(sheet, 10, 0, "ShipToCity");
	    addCaption(sheet, 11, 0, "ShipToState");
	    addCaption(sheet, 12, 0, "ShipToZip");
	    addCaption(sheet, 13, 0, "Blank");
	    addCaption(sheet, 14, 0, "OrderTotal");
	    addCaption(sheet, 15, 0, "Warehouse");
	    addCaption(sheet, 16, 0, "WorkOrder");
	    
	    addCaption(sheet, 0, 1, "A");
	    addCaption(sheet, 1, 1, "Line #");
	    addCaption(sheet, 2, 1, "city");
	    addCaption(sheet, 3, 1, "price");
	    addCaption(sheet, 4, 1, "custpart");
	    addCaption(sheet, 5, 1, "item");
	    addCaption(sheet, 6, 1, "description");
	    addCaption(sheet, 7, 1, "enter date");
	    addCaption(sheet, 8, 1, "enter time");
	    addCaption(sheet, 9, 1, "on order");
	    addCaption(sheet, 10, 1, "Blank");
	    addCaption(sheet, 11, 1, "Blank");
	    addCaption(sheet, 12, 1, "Blank");
	    addCaption(sheet, 13, 1, "Blank");
	    addCaption(sheet, 14, 1, "Blank");   
	    addCaption(sheet, 15, 1, "Blank");
	    addCaption(sheet, 16, 1, "Blank");
	  }

	  private void createContent(WritableSheet sheet, Output output) throws WriteException,
	      RowsExceededException {
	    // Write a few number
		  int i=0,j=2;
		  addLabel(sheet, i, j, "H");
		  i++;		  
		  addLabel(sheet, i, j, output.getUser());
		  i++;
		  addLabel(sheet, i, j, output.getShipdate());
		  i++;
		  addLabel(sheet, i, j, output.getCustomerNumber());
		  i++;
		  addLabel(sheet, i, j, output.getShiptocode());
		  i++;
		  addLabel(sheet, i, j, output.getBranch());
		  i++;
		  addLabel(sheet, i, j, "1");
		  i++;
		  addLabel(sheet, i, j, "10");
		  i++;
		  addLabel(sheet, i, j, "30");
		  i++;
		  addLabel(sheet, i, j, output.getShipToNumber());
		  i++;
		  addLabel(sheet, i, j, output.getShipToName());
		  i++;
		  addLabel(sheet, i, j, output.getShipToAddress());
		  i++;
	//	  addLabel(sheet, i, j, output);
		  i++;
		  addLabel(sheet, i, j, output.getShipToCity());
		  i++;
		  addLabel(sheet, i, j, output.getShipToState());
		  i++;
		  addLabel(sheet, i, j, output.getShipToZip());
		  i++;
// blank		  addLabel(sheet, i, j, output);
		  i++;
		  addNumber(sheet, i, j, output.getOrderTotal());
		  i++;
		  addLabel(sheet, i, j, output.getWarehouse());
		  i++;
		  addLabel(sheet, i, j, output.getWorkOrder());

		  j++;
		  for(j=3;j<3+output.getOrderTotal();j++)
		  {
			  i=0;
			  Cargo cargo = output.getCargoList().get(j-3);
			  addLabel(sheet, i, j, "D");
			  i++;
			  addNumber(sheet, i, j, j-1);
			  i++;
			  addLabel(sheet, i, j, cargo.getQty());
			  i++;
			  addLabel(sheet, i, j, cargo.getUOM());
			  i++;
			  addLabel(sheet, i, j, cargo.getPrice());
			  i++;
			  addLabel(sheet, i, j, cargo.getCustPart());
			  i++;
			  addLabel(sheet, i, j, cargo.getItem());
			  i++;
			  addLabel(sheet, i, j, cargo.getDescription());
			  i++;
			  addLabel(sheet, i, j, cargo.getEnterdate());
			  i++;
			  addLabel(sheet, i, j, cargo.getEntertime());
			  i++;
			  addLabel(sheet, i, j, cargo.getOnOrder());
		  }
//	    for (int i = 1; i < 10; i++) {
//	      // First column
//	      addNumber(sheet, 0, i, i + 10);
//	      // Second column
//	      addNumber(sheet, 1, i, i * i);
//	      
//	    }
//	    // Lets calculate the sum of it
//	    StringBuffer buf = new StringBuffer();
//	    buf.append("SUM(A2:A10)");
//	    Formula f = new Formula(0, 10, buf.toString());
//	    sheet.addCell(f);
//	    buf = new StringBuffer();
//	    buf.append("SUM(B2:B10)");
//	    f = new Formula(1, 10, buf.toString());
//	    sheet.addCell(f);
//
//	    // now a bit of text
//	    for (int i = 12; i < 20; i++) {
//	      // First column
//	      addLabel(sheet, 0, i, "Boring text " + i);
//	      // Second column
//	      addLabel(sheet, 1, i, "Another text");
//	    }
	  }

	  private void addCaption(WritableSheet sheet, int column, int row, String s)
	      throws RowsExceededException, WriteException {
	    Label label;
	    label = new Label(column, row, s, timesBoldUnderline);
	    sheet.addCell(label);
	  }

	  private void addNumber(WritableSheet sheet, int column, int row,
	      Integer integer) throws WriteException, RowsExceededException {
	    Number number;
	    number = new Number(column, row, integer, times);
	    sheet.addCell(number);
	  }

	  private void addLabel(WritableSheet sheet, int column, int row, String s)
	      throws WriteException, RowsExceededException {
	    Label label;
	    label = new Label(column, row, s, times);
	    sheet.addCell(label);
	  }


	public String getOneSheet(Output output)
	{
		StringToXls test = new StringToXls();
	//	String nameString = output.get
	//	if(output.)
		String nameString = "/mnt/sdcard/Ezsource/test.xls";
	    test.setOutputFile(nameString);
	    try {
			test.write(output);
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return nameString;
	}
	
	public StringToXls()
	{
	
	}
}
