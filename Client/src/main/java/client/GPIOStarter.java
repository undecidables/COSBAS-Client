package client;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * {@author Szymon}
 */
public class GPIOStarter {

    private ApplicationContext context;
    private Object object = new Object();
    final GpioController gpio;
    final GpioPinDigitalInput input;

    public GPIOStarter()
    {
        context = Client.context;
        object = (Object) context.getBean("object");

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
            gpio.shutdown();
        }
    }

}
