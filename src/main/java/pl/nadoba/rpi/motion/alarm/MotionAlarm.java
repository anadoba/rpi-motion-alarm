package pl.nadoba.rpi.motion.alarm;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.trigger.GpioSyncStateTrigger;

public class MotionAlarm {

    private final static int CHECK_DURATION = 2000;

    private final GpioController gpio = GpioFactory.getInstance();
    private final GpioPinDigitalOutput led = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "LED", PinState.LOW);
    private final GpioPinDigitalInput motionSensor = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03, "MOTION");

    public MotionAlarm() {
        init();
    }

    private void init() {
        motionSensor.addTrigger(new GpioSyncStateTrigger(led));
    }

    public void loop() throws InterruptedException {
        for (; ;) {
            Thread.sleep(CHECK_DURATION);
        }
    }
}
