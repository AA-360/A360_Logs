package com.automationanywhere.botcommand.samples.commands.basic;
import java.io.*;
import java.net.InetAddress;
import java.text.DecimalFormat;
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
import com.sun.jna.platform.win32.Kernel32;
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

import java.nio.file.Files;
import java.nio.file.Paths;

@BotCommand
@CommandPkg(label = "AddLog",
        description = "Writes a log", icon = "pkg.svg", name = "AddLog",
node_label = "logs a {{status}} log")


public class AddLog {

    @Execute
    public void action(
            @Idx(index = "1", type = TEXT)
            @Pkg(label = "CurrentTask:",description = "",default_value = "",default_value_type = STRING)
            @NotEmpty
                    String curr_task,
            @Idx(index = "2", type = SELECT, options = {
                    @Idx.Option(index = "2.1", pkg = @Pkg(label = "Success", value = "SUCCESS")),
                    @Idx.Option(index = "2.2", pkg = @Pkg(label = "Info", value = "INFO")),
                    @Idx.Option(index = "2.3", pkg = @Pkg(label = "Warning", value = "WARNING")),
                    @Idx.Option(index = "2.4", pkg = @Pkg(label = "Failure", value = "FAILURE")),
                    @Idx.Option(index = "2.5", pkg = @Pkg(label = "Custom", value = "CUSTOM"))})
            @Pkg(label = "Type", description = "", default_value = "SUCCESS", default_value_type = DataType.STRING)
            @NotEmpty
                    String status,
            @Idx(index = "2.5.1", type = TEXT)
            @Pkg(label = "Custom:",description = "",default_value = "",default_value_type = STRING)
            @NotEmpty
                    String custom_status,
            @Idx(index = "3", type = TEXT)
            @Pkg(label = "Message:",description = "",default_value = "",default_value_type = STRING)
            @NotEmpty
                    String message,
            @Idx(index = "4", type = BOOLEAN)
            @Pkg(label = "Disable:",description = "",default_value = "false",default_value_type = DataType.BOOLEAN)
            @NotEmpty
                    Boolean disableLogs

    ) {

        if(disableLogs)return;

        String log_dir = getSessionVar("dir");
        String env_name = getSessionVar("env_name");
        String ID_PROC = getSessionVar("proc_id");
        String file_name = getSessionVar("file_name");
        String main_task = getSessionVar("main_task");
        String bot_name = getSessionVar("bot_name");

        Date  date = Calendar.getInstance().getTime();
        String DATE = Uteis.DateToString(date,"yyyy-MM-dd hh:mm:ss");

        String USERNAME = System.getProperty("user.name");
        String HOSTNAME = getHostName();

        String VM_RAM = get_memory_installed() + "G";

        String RAM_IM_USING = get_memory_im_using() + "G";

        String CPU_IM_USING = get_cpu_im_using() + "MB";

        String STATUS = custom_status != null?custom_status:status;

        writeLog(log_dir,file_name,ID_PROC,DATE,main_task,curr_task,STATUS,message,USERNAME,HOSTNAME,bot_name,VM_RAM,RAM_IM_USING,CPU_IM_USING);


    }


    private void writeLog(String dir,String file_name,String id_proc,String DATE,String main_task,String curr_task,String status,String message,String USERNAME,String HOSTNAME,String bot_name,String VM_RAM,String RAM_IM_USING,String CPU_IM_USING){
        try{
            if(new File(dir + "/" + file_name).isFile()){
                FileInputStream fileInputStream = new FileInputStream(dir + "/" + file_name);
                XSSFWorkbook workBook = new XSSFWorkbook(fileInputStream);
                fileInputStream.close();
                XSSFSheet sheet = workBook.getSheet("Sheet1");

                XSSFRow currentRowCol=sheet.createRow(sheet.getLastRowNum()+1);



                writeCell(workBook,currentRowCol,0,id_proc,id_proc);
                writeCell(workBook,currentRowCol,1,id_proc,DATE);
                writeCell(workBook,currentRowCol,2,id_proc,main_task);
                writeCell(workBook,currentRowCol,3,id_proc,curr_task);
                writeInfo(workBook,currentRowCol,status);
                writeCell(workBook,currentRowCol,5,id_proc,message);
                writeCell(workBook,currentRowCol,6,id_proc,USERNAME);
                writeCell(workBook,currentRowCol,7,id_proc,HOSTNAME);
                writeCell(workBook,currentRowCol,8,id_proc,bot_name);
                writeCell(workBook,currentRowCol,9,id_proc,VM_RAM);
                writeCell(workBook,currentRowCol,10,id_proc,RAM_IM_USING);
                writeCell(workBook,currentRowCol,11,id_proc,CPU_IM_USING);



                FileOutputStream fileOutputStream =  new FileOutputStream(dir + "/" + file_name);
                workBook.write(fileOutputStream);
                fileOutputStream.close();
                System.out.println("Done");

            }
        }catch (Exception e){
            throw new BotCommandException("Erro ao criar o arquivo: " + e.getMessage());
        }
    }


    private void writeCell(XSSFWorkbook workBook,XSSFRow currentRowCol,Integer id,String proc_id,String value){

        XSSFCellStyle grayStyle = workBook.createCellStyle();
        grayStyle.setFillForegroundColor(new XSSFColor(java.awt.Color.decode("#D0CECE")));
        grayStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFCell cell = currentRowCol.createCell(id);

        cell.setCellValue(value);

        if(Integer.parseInt(proc_id)%2 == 0 && proc_id != "0"){
            cell.setCellStyle(grayStyle);
        }


    }

    private void writeInfo(XSSFWorkbook workBook,XSSFRow currentRowCol,String status){

        XSSFCell cell = currentRowCol.createCell(4);

        XSSFCellStyle greenStyle = workBook.createCellStyle();
        greenStyle.setFillForegroundColor(new XSSFColor(java.awt.Color.decode("#92D050")));
        greenStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFCellStyle blueStyle = workBook.createCellStyle();
        blueStyle.setFillForegroundColor(new XSSFColor(java.awt.Color.decode("#9BC2E6")));
        blueStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFCellStyle yellowStyle = workBook.createCellStyle();
        yellowStyle.setFillForegroundColor(new XSSFColor(java.awt.Color.decode("#FFD966")));
        yellowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFCellStyle redStyle = workBook.createCellStyle();
        redStyle.setFillForegroundColor(new XSSFColor(java.awt.Color.decode("#FF5353")));
        redStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);


        cell.setCellValue(status);

        if(status.equals("SUCCESS")){
            cell.setCellStyle(greenStyle);
        }
        if(status.equals("INFO")){
            cell.setCellStyle(blueStyle);
        }
        if(status.equals("WARNING")){
            cell.setCellStyle(yellowStyle);
        }
        if(status.equals("FAILURE")){
            cell.setCellStyle(redStyle);
        }


    }

    private String get_cpu_im_using(){
        int pid = Kernel32.INSTANCE.GetCurrentProcessId();
        double cpuUsage = 0.0;
        try {
            Process proc = Runtime.getRuntime().exec("tasklist /fi \"PID eq " + pid + "\" /nh");
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(Integer.toString(pid))) {
                    String[] parts = line.trim().split("\\s+");
                    cpuUsage = Double.parseDouble(parts[4].replace(",", "."));
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("CPU usage (%): " + cpuUsage);

        return double2decimals(cpuUsage/1024);
    }

    private String get_memory_installed(){
        long totalMemory = 0;
        try {
            Process proc = Runtime.getRuntime().exec("wmic memorychip get capacity");
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.strip().matches("\\d+")) {
                    totalMemory += Long.parseLong(line.strip());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        double totalMemoryInGB = (double) totalMemory / (1024.0 * 1024.0 * 1024.0);
        System.out.println("Total memory installed (GB): " + totalMemoryInGB);
        return double2decimals(totalMemoryInGB);
    }

    private String get_memory_available(){
        long totalMemory = 0;
        try {
            Process proc = Runtime.getRuntime().exec("wmic OS get FreePhysicalMemory");
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.strip().matches("\\d+")) {
                    totalMemory += Long.parseLong(line.strip()) *1024;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        double totalMemoryInGB = (double) totalMemory / (1024.0 * 1024.0 * 1024.0);
        System.out.println("Total memory available (GB): " + totalMemoryInGB);
        return double2decimals(totalMemoryInGB);
    }

    private String get_memory_im_using(){
        int pid = Kernel32.INSTANCE.GetCurrentProcessId();
        long memoryUsage = 0;
        try {
            Process proc = Runtime.getRuntime().exec("wmic process where ProcessId=" + pid + " get WorkingSetSize");
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.strip().matches("\\d+")) {
                    memoryUsage += Long.parseLong(line.strip());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        double memoryUsageInGB = (double) memoryUsage / (1024.0 * 1024.0 * 1024.0);
        System.out.println("Memory usage (GB): " + memoryUsageInGB);
        return double2decimals(memoryUsageInGB);
    }

    private String getHostName(){
        try{
            return InetAddress.getLocalHost().getHostName();
        }
        catch (Exception e){
            throw new BotCommandException("Error getting hostname: " + e.getMessage());
        }
    }

    private String getSessionVar(String var_name){
        SessionVars ut = new SessionVars();
        Ini ini = ut.getIniFile();

        //======================VALIDANDO SE JA EXISTE============
        if(!ut.variableExists(ini,var_name)){
            throw new BotCommandException("Please start logging first!");
        }

        ut.validateType(ini,var_name,"text");

        return ini.get(var_name,"value");
    }

    private String double2decimals(double value){
        DecimalFormat df = new DecimalFormat("#.##");

        // Use the format() method to round the value
        String formattedValue = df.format(value);

        return formattedValue.replace(",",".");
    }

    private void alert(String text){
        JOptionPane.showMessageDialog(null, text, "InfoBox: Title", JOptionPane.INFORMATION_MESSAGE);
    }
}
