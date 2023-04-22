package com.automationanywhere.botcommand.samples.commands.utils;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.ListValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.sun.jna.platform.win32.Kernel32;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.ini4j.Ini;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SessionVars {
    public SessionVars(){}

    public String fileName(){
        int pid = Kernel32.INSTANCE.GetCurrentProcessId();
        String s = System.getProperty("java.io.tmpdir") + "LogsSessionStorage/"+ Integer.toString(pid) + "_global.ini";
        return s;
    }

    public Ini getIniFile() {
        String fileName = this.fileName();
        Ini ini;

        try{
            File iniFile = new File(fileName);
            if(!iniFile.exists()){
                iniFile.getParentFile().mkdirs();
                FileUtils.cleanDirectory(iniFile.getParentFile());
                iniFile.createNewFile();
            }
            return new Ini(iniFile);
        }
        catch (Exception e){
            throw new BotCommandException("Sessao nao iniciada!:\n" + e.getMessage());
        }
    }

    public boolean variableExists(Ini ini,String section){

        for (String sectionName: ini.keySet()) {
            if(sectionName.equals(section)){
                return true;
            }
        }
        return false;
    }

    public void validate(Ini ini,String varName,String type_){
        if(this.variableExists(ini,varName)){
            Boolean constant = Boolean.parseBoolean(ini.get(varName,"constant"));
            if(!constant) {
                this.validateType(ini,varName,type_);
            }else{
                throw new BotCommandException("Esta variavel ja foi declarada como constante e nao pode ser alterada!");
            }
        }
    }

    public void validateType(Ini ini,String varName,String type_){
        String type = ini.get(varName,"type");
        if (!type.equals(type_)) {
            throw new BotCommandException("Tipos incompativeis: a variavel '" + varName + "'(" + type + ") nao corresponde ao tipo "+ type_);
        }
    }
    public ListValue<String> getKeyListValue(Ini ini,String key){
        ListValue<String> OUTPUT = new ListValue<String>();
        List<Value> RowListValues = new ArrayList<>();

        String csv = ini.get(key,"value");
        String str[] =csv.split("-;-");

        for(int i=0;i<str.length;i++){
            String rw = str[i];
            RowListValues.add(new StringValue(rw));
        }

        OUTPUT.set(RowListValues);
        return OUTPUT;
    }
    public List<String> getKeyListString(Ini ini,String key){
        List<String> OUTPUT = new ArrayList<>();

        String csv = ini.get(key,"value");
        String str[] =csv.split("-;-");

        for(int i=0;i<str.length;i++){
            String rw = str[i];
            OUTPUT.add(rw);
        }
        return OUTPUT;
    }
    public void storeKeyListString(Ini ini,String key,List<String> list,Boolean isConstant){
        try{
            String csv = ini.get(key,"value");
            String listStr = "";
            for(String rw: list){
                listStr += rw + "-;-";
            }
            listStr = StringUtils.substring(listStr, 0, listStr.length() - 3);

            ini.put(key,"value",listStr);
            ini.put(key,"type","list");
            ini.put(key,"constant",isConstant);
            ini.store();
        }
        catch (Exception e){
            throw new BotCommandException("Erro ao gravar variavel: " + e.getMessage());
        }
    }
    public void storeKeyListValue(Ini ini,String key,List<Value> list,Boolean isConstant){
        try{
            String csv = ini.get(key,"value");
            String listStr = "";
            for(Value rw: list){
                listStr += rw.toString() + "-;-";
            }
            listStr = StringUtils.substring(listStr, 0, listStr.length() - 3);

            ini.put(key,"value",listStr);
            ini.put(key,"type","list");
            ini.put(key,"constant",isConstant);
            ini.store();
        }
        catch (Exception e){
            throw new BotCommandException("Erro ao gravar variavel: " + e.getMessage());
        }
    }

}