package client;

import com.lcdfx.io.AdafruitLcdPlate;
import com.lcdfx.io.Lcd;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by simon on 2015-10-27.
 */

public class LCDDisplay {

    static final int BUS_NO = 1;
    static final int BUS_ADDRESS = 0x20;

    private static Lcd lcd;

    public LCDDisplay()
    {
        try {
            lcd = new AdafruitLcdPlate(BUS_NO, BUS_ADDRESS);
        } catch (Exception e) {
            lcd = null;
        }
    }



    public static void write(String message)
    {
        lcd.clear();
        if(message.length() > 16)
        {
            lcd.setCursorPosition(0,0);
            lcd.write(message.substring(0, 16).toUpperCase());
            lcd.setCursorPosition(1,0);
            lcd.write(message.substring(16,message.length()).toUpperCase());

        }
        else
        {
            lcd.write(message.toUpperCase());
        }



    }

    public void shutdown()
    {
        lcd.shutdown();
    }
}
