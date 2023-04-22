package com.automationanywhere.botcommand.samples.commands.basic;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.ListValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.samples.commands.utils.SessionVars;
import com.automationanywhere.botcommand.samples.commands.utils.Uteis;
import com.automationanywhere.botcommand.samples.commands.utils.WorkbookHelper;
import com.automationanywhere.commandsdk.model.DataType;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.platform.win32.User32;

import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.commandsdk.annotations.*;

import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.*;
import org.ini4j.Ini;

import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static com.automationanywhere.commandsdk.model.AttributeType.*;
import static com.automationanywhere.commandsdk.model.DataType.LIST;
import static com.automationanywhere.commandsdk.model.DataType.STRING;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;

@BotCommand
@CommandPkg(label = "Start_Logging",
        description = "Start logging process", icon = "pkg.svg", name = "Start_Logging")


public class Start_Logging {

    @Execute
    public void action(
            @Idx(index = "1", type = TEXT)
            @Pkg(label = "Directory:",description = "",default_value = "",default_value_type = STRING)
            @NotEmpty
                    String dir,
            @Idx(index = "2", type = CHECKBOX)
            @Pkg(label = "Create directories:",description = "Force directories to exists",default_value = "false",default_value_type = DataType.BOOLEAN)
            @NotEmpty
                    Boolean forceDir,
            @Idx(index = "3", type = TEXT)
            @Pkg(label = "BotName:",description = "",default_value = "",default_value_type = STRING)
            @NotEmpty
                    String bot_name,
            @Idx(index = "4", type = TEXT)
            @Pkg(label = "Env:",description = "DEV,UAT,PRD",default_value = "",default_value_type = STRING)
            @NotEmpty
                    String envName,
            @Idx(index = "5", type = SELECT, options = {
                    @Idx.Option(index = "5.1", pkg = @Pkg(label = "Single File", value = "sf",description = "DEV.xlsx")),
                    @Idx.Option(index = "5.2", pkg = @Pkg(label = "Daily File", value = "df",description = "DEV_yyyy_MM_dd.xlsx")),
                    @Idx.Option(index = "5.3", pkg = @Pkg(label = "Exec File", value = "ef",description = "DEV_01_yyyy_MM_dd hh_mm.xlsx"))})
            @Pkg(label = "Type", description = "", default_value = "sf", default_value_type = DataType.STRING)
            @NotEmpty
                    String log_type,
            @Idx(index = "6", type = TEXT)
            @Pkg(label = "MainTask:",description = "",default_value = "",default_value_type = STRING)
            @NotEmpty
                    String mainTask


    ) {
        // ======================================== DIR CREATION
        if(forceDir) {
            try {
                Files.createDirectories(Paths.get(dir));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        File folder = new File(dir);
        if (!(folder.exists() && folder.isDirectory())) {
            throw new BotCommandException("Directory not found:" + dir);
        }
        // ======================================== PROCESS DI
        Integer id = process_id(dir,log_type,envName);
        // ======================================== FILE NAME
        String file_name = fileName(id,log_type,envName);
        //========================================= CREATE FILE
        createFile(dir,file_name);


        setSessionVar("dir", dir);
        setSessionVar("env_name", envName);
        setSessionVar("proc_id", id.toString());
        setSessionVar("file_name", file_name);
        setSessionVar("main_task", mainTask);
        setSessionVar("bot_name", bot_name);


//
//        String accessToken = getTokenFIS(authToken).get("access_token").toString();
//        JSONObject sign = signOnFIS(organizationId,uuid,accessToken,user,pwd);
//
//        String signonToken = "";
//        if(JsonUtils.getKeyJson("SignonRs.ResponseData.ReturnDesc", sign).toString().equals("Ready")) {
//            signonToken = JsonUtils.getKeyJson("SignonRs.ResponseData.SignonTKN", sign).toString();
//        }else{
//            String err = JsonUtils.getKeyJson("SignonRs.ResponseData.ReturnDesc", sign).toString();
//            throw new BotCommandException("Error at Signon:" + err);
//        }
//
//        Data.put("authToken", authToken);
//        Data.put("uuid", uuid);
//        Data.put("organizationId", organizationId);
//        Data.put("accessToken", accessToken);
//        Data.put("signonToken", signonToken);
//        Data.put("status", true);
//        Data.put("errorMessage", "");
//        sessionMap.put(SessionName, Data);
    }

    private void createFile(String dir,String file_name){
        try{
            if(!new File(dir + "/" + file_name).isFile()){
                XSSFWorkbook workBook = new XSSFWorkbook();
                XSSFSheet sheet = workBook.createSheet("Sheet1");

                //CELL COLOR
                XSSFCellStyle blueStyle = workBook.createCellStyle();
                blueStyle.setFillForegroundColor(new XSSFColor(java.awt.Color.decode("#D9E1F2")));
                blueStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                int ColNum=0;
                String[] cols = {"ID_PROC", "DATE", "MAIN_TASK", "TASK","STATUS","MESSAGE","USER_NAME","HOSTNAME","BOTNAME","VM_RAM","RAM_IM_USING","CPU_IM_USING"};
                XSSFRow currentRowCol=sheet.createRow(0);
                for(String schema : cols){
                    XSSFCell cell = currentRowCol.createCell(ColNum++);
                    cell.setCellValue(schema);
                    cell.setCellStyle(blueStyle);
                }

                FileOutputStream fileOutputStream =  new FileOutputStream(dir + "/" + file_name);
                workBook.write(fileOutputStream);
                fileOutputStream.close();
                System.out.println("Done");

            }
        }catch (Exception e){
            throw new BotCommandException("Erro ao criar o arquivo: " + e.getMessage());
        }
    }

    private void setSessionVar(String var_name,String value){
        SessionVars ut = new SessionVars();
        Ini ini = ut.getIniFile();

        if(ut.variableExists(ini,var_name)){
            throw new BotCommandException("Log session already started before!");
        }

        try{
            //======================VALIDANDO SE JA EXISTE============
            ut.validate(ini,var_name,"text");
            //=======================GRAVANDO VARIAVEL================
            ini.put(var_name,"value",value);
            ini.put(var_name,"type","text");
            ini.put(var_name,"constant",true);
            ini.store();
        }
        catch (Exception e){
            throw new BotCommandException("Error: " + e.getMessage());
        }
    }


    private Integer process_id(String dir,String log_type,String env){

        if(log_type.equals("sf")){
            if(new File(dir + "/" + env + ".xlsx").isFile()){
                Integer last_id = get_id_from_xlsx(dir + "/" + env + ".xlsx");
                return last_id + 1;
            }else{
                return 0;
            }
        }
        if(log_type.equals("df")){
            Date  date = Calendar.getInstance().getTime();
            String file_name = Uteis.DateToString(date,"'" + env + "'_yyyy_MM_dd'.xlsx'");
            if(new File(dir + "/" + file_name).isFile()){
                Integer last_id = get_id_from_xlsx(dir + "/" + file_name);
                return last_id + 1;
            }else{
                return 0;
            }
        }

        if(log_type.equals("ef")){
            Date  date = Calendar.getInstance().getTime();
            String strDate = "^'" + env + "_\\d{2}'_yyyy_MM_dd'.*.xlsx'$";
            String patternString = Uteis.DateToString(date,strDate);
            List<String> files = getListFiles(dir,patternString);
            if(files.size() >0){
                return files.size()+1;
            }else{
                return 0;
            }
        }
        return 99;


    }

    private Integer get_id_from_xlsx(String file){
        try {
            WorkbookHelper wbH = new WorkbookHelper(file);
            Sheet mySheet  = wbH.wb.getSheetAt(0);
            List<Row> ROWS = wbH.getRows(mySheet);
            Integer numOfRows = mySheet.getLastRowNum();
            Row last = ROWS.get(numOfRows);
            String lastId = wbH.getColumns(last).get(0).toString();
            Double last_id = lastId.equals("ID_PROC")?0:Double.parseDouble(lastId);

            wbH.wb.close();

            return last_id.intValue();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> getListFiles(String folderPath, String patternString){
        List<String> ret = new ArrayList<>();

        // Set the regular expression pattern
        //String patternString = "^" + env + "_\\d{2}_\\d{4}_.*.xlsx$";

        // Create the pattern object
        Pattern pattern = Pattern.compile(patternString);

        // Create a file object for the folder
        File folder = new File(folderPath);

        // Get a list of all files in the folder
        File[] files = folder.listFiles();

        // Loop through the files and print the names of the files that match the pattern
        for (File file : files) {
            if (pattern.matcher(file.getName()).matches()) {
                ret.add(file.getName());
            }
        }
        return ret;
    }


    private String fileName(Integer id,String log_type,String env){
        Date  date = Calendar.getInstance().getTime();
        String strDate = "";

        if(log_type.equals("sf")){
            strDate = "'" + env + ".xlsx'";
        }
        if(log_type.equals("df")){
            strDate = "'" + env + "'_yyyy_MM_dd'.xlsx'";
        }
        if(log_type.equals("ef")){
            strDate = "'" + env + "_" +  String.format("%02d", id) + "'_yyyy_MM_dd hh_mm'.xlsx'";
        }
        return Uteis.DateToString(date,strDate);
    }



}
