package com.automationanywhere.botcommand.samples.commands.utils;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WorkbookHelper {
    public Workbook wb = null;
    private FormulaEvaluator evaluator = null;
    private DataFormatter formatter = new DataFormatter(true);

    public WorkbookHelper(XSSFWorkbook myWorkBook){
        this.wb = myWorkBook;
        this.evaluator = this.wb.getCreationHelper().createFormulaEvaluator();
    }
    public WorkbookHelper(HSSFWorkbook myWorkBook){
        this.wb = myWorkBook;
        this.evaluator = this.wb.getCreationHelper().createFormulaEvaluator();
    }
    public WorkbookHelper(Workbook myWorkBook){
        this.wb = myWorkBook;
    }
    public WorkbookHelper(String file){
        try{
            File myFile = new File(file);
            if(!myFile.exists()){
                throw new BotCommandException("File '" + file + "' not found!");
            }
            FileInputStream fis = new FileInputStream(myFile);

            XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
            this.wb = myWorkBook;
        }catch (IOException e){
            throw new BotCommandException("Error reading/crearing excel file:" + e.getMessage());
        }
    }


    public List<String> getSheetsName(){
        List<String> listSheets = new ArrayList();
        Iterator<Sheet> sheetIterator = this.wb.sheetIterator();
        while(sheetIterator.hasNext()){
            Sheet sheet = sheetIterator.next();
            listSheets.add(sheet.getSheetName());
        }

        return listSheets;
    }

    public Boolean sheetExists(String sheetName){
        return this.getSheetsName().contains(sheetName);
    }
    public Boolean sheetExists(Integer sheetIndex){
        return sheetIndex <= (this.getSheetsName().size()-1);
    }
    public StringValue getCellValue(Cell cell){
        String value = "";
        if (cell == null) return new StringValue("");
        try{
            switch (cell.getCellType()) {
                case STRING:
                    value = cell.getStringCellValue();
                    break;
                case NUMERIC:
                    value = Double.toString(cell.getNumericCellValue());
                    break;
                case BOOLEAN:
                    value = Boolean.toString(cell.getBooleanCellValue());
                    break;
                case FORMULA:
                    value = this.formatter.formatCellValue(cell, this.evaluator);
                    break;
                case BLANK:
                    value = "";
                    break;
                default:
                    cell.setCellType(CellType.STRING);
                    value = cell.getStringCellValue();
                    value = value==null?"":value;
                    //throw new BotCommandException("ERROR PARSING VALUE TYPE :" + cell.getCellType());
            }
        }catch(Exception e){
            cell.setCellType(CellType.STRING);
            value = cell.getStringCellValue();
        }
        return new StringValue(value);
    }

    public List<Row> getRows(Sheet sheet){
        List<Row> listRows = new ArrayList();
        Integer LastRowNumb = sheet.getLastRowNum();

        for(Integer rowIdx = 0; rowIdx <= LastRowNumb;rowIdx++){
            listRows.add(sheet.getRow(rowIdx));
        }

//        Iterator<Row> rowIterator = sheet.iterator();
//
//        while (rowIterator.hasNext()) {
//            listRows.add(rowIterator.next());
//        }

        return listRows;
    }

    public List<Cell> getColumns(Row row){
        List<Cell> listCells = new ArrayList();
        Iterator<Cell> cellIterator;
        if(row == null) return listCells;
        //System.out.println(":::::" + row);

        for(Integer colIdx = 0; colIdx < row.getLastCellNum();colIdx++){
            listCells.add(row.getCell(colIdx));
        }

//        row.getLastCellNum();
//
//        try {
//            cellIterator = row.cellIterator();
//        }catch(Exception e){
//            return listCells;
//        }
//
//        while (cellIterator.hasNext()) {
//            listCells.add(cellIterator.next());
//        }
        return listCells;
    }

    public Integer ColumnToIndex(String col){
        String Letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Integer multiplicador = 1;
        Integer numb = 0;

        char ch;
        for(int i=col.toCharArray().length-1;i>=0;i--) {
            ch = Array.getChar(col.toCharArray(), i);
            numb += multiplicador * (Letters.indexOf(ch)+1);
            multiplicador *=26;
        }
        if(numb > 16384){
            throw new BotCommandException("Column '" + col + "' value invalid!");
        }
        return numb;
    }

//    public List<Cell> getCellsByRange(String Pattern){
//        CellRangeAddress a = CellRangeAddress.valueOf(Pattern);
//        this.wb.getSheetAt(0).addMergedRegion(CellRangeAddress.valueOf("B5:C6"));
//
//    }

    public List<Integer> PatternColumnsToListIndex(String Columns){
        List<Integer> colsIndex = new ArrayList<>();
        Columns = Columns.toUpperCase().trim();
        Boolean pattern1= Columns.matches("^([A-Z]{1,3}):([A-Z]{1,3})$");
        Boolean pattern2= Columns.matches("^(([A-Z]{1,3})\\|)*[A-Z]{1,3}$");

        if(!(pattern1 || pattern2)){
            throw new BotCommandException("Columns (" + Columns + ") has not a valid format try to use as A:C or A|B|C");
        }
        if(pattern1){
            String[] addrs = Columns.split(":");
            colsIndex = this.getNumbersInRange(this.ColumnToIndex(addrs[0])-1,this.ColumnToIndex(addrs[1])-1);
        }else{
            String[] addrs = Columns.split("\\|");
            for(String cel: addrs){
                colsIndex.add(this.ColumnToIndex(cel)-1);
            }
        }
        return colsIndex;
    }

    public List<Integer> getNumbersInRange(int start, int end) {
        List<Integer> result = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            result.add(i);
        }
        return result;
    }



}
