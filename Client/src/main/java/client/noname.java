package client;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by simon on 2015-10-14.
 */
public class noname {

    private ApplicationContext context;
    private Object object = new Object();

    public noname()
    {
        context = Client.context;
        object = (Object) context.getBean("object");
    }


    public void start()
    {
        final GpioController gpio = GpioFactory.getInstance();
        final GpioPinDigitalInput input = gpio.provisionDigitalInputPin(RaspiPin.GPIO_28, PinPullResistance.PULL_DOWN);

        input.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                System.out.println("Someone is here");
                synchronized (object) {
                    object.notifyAll();
                }
            }
        });

        AuthenticationProcess process = new AuthenticationProcess();

        process.run();


        try {
            process.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            gpio.shutdown();
        }
    }

}
