package client;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * {@author Szymon}
 */
public class GPIOStarter {


    private LCDDisplay display;

    private ApplicationContext context;
    private Object object;/*changed it here*/
    final GpioController gpio;
    final GpioPinDigitalInput input;

    public GPIOStarter()
    {
        context = Client.context;
        object = (Object) context.getBean("object");
        display = (LCDDisplay) context.getBean("display");

        gpio = GpioFactory.getInstance();
        input = gpio.provisionDigitalInputPin(RaspiPin.GPIO_28, PinPullResistance.PULL_DOWN);

        input.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                synchronized (object) {
                    object.notifyAll();
                }
            }
        });
    }


    public void start()
    {
        AuthenticationProcess process = new AuthenticationProcess();

        process.run();


        try
        {
            process.join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        finally
        {
            display.shutdown();
            gpio.shutdown();
        }
    }

}
