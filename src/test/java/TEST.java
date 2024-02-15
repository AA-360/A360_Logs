import com.automationanywhere.botcommand.samples.commands.basic.AddLog;
import com.automationanywhere.botcommand.samples.commands.basic.Start_Logging;
import org.testng.annotations.Test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class TEST {
    @Test
    public void logging(){
        Start_Logging a = new Start_Logging();
        AddLog b = new AddLog();

        a.action("C:\\Temp\\logs",true,"BOT NAME","DEV","sf","myMainTask");

        b.action("currTask","SUCCESS",null,"MY MESSAGE",true);
        b.action("currTask","INFO","","MY MESSAGE",false);
        b.action("currTask","WARNING","","MY MESSAGE",false);
        b.action("currTask","FAILURE","","MY MESSAGE",false);

    }

    public void teste(){
        Start_Logging a = new Start_Logging();

        //a.action("C:\\Temp\\logs",true,"DEV","sf","myMainTask");
    }

    private void alert(String text){
        JOptionPane.showMessageDialog(null, text, "InfoBox: Title", JOptionPane.INFORMATION_MESSAGE);
    }
}

