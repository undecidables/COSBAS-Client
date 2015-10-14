package client;

import com.pi4j.io.gpio.*;

/**
 * {@author Szymon}
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
