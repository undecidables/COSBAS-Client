package client;

import com.pi4j.io.gpio.*;

/**
 * Created by simon on 2015-10-14.
 */
public class GPIOAccess {

    final GpioController gpio;
    final GpioPinDigitalOutput pin;

    public GPIOAccess()
    {
        gpio = GpioFactory.getInstance();
        pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29, "magnet", PinState.LOW);
    }

    public void allowAccess()
    {
        pin.toggle();
        try
        {
            Thread.sleep(500);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        pin.toggle();
    }

}
