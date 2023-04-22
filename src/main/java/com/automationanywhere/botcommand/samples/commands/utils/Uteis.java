package com.automationanywhere.botcommand.samples.commands.utils;

import com.automationanywhere.botcommand.exception.BotCommandException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Uteis {

    public static Date StringToDate(String date, String format){
        try {
            SimpleDateFormat formatterInput = new SimpleDateFormat(format);
            return formatterInput.parse(date);
        } catch (NullPointerException | ParseException e) {
            throw new BotCommandException(e.getMessage());
        }
    }

    public static String DateToString(Date date,String format){
        try {
            SimpleDateFormat formatterOutput = new SimpleDateFormat(format);
            return formatterOutput.format(date);
        } catch (NullPointerException e) {
            throw new BotCommandException(e.getMessage());
        }
    }

}