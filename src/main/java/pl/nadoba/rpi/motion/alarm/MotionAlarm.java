package pl.nadoba.rpi.motion.alarm;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.trigger.GpioCallbackTrigger;
import com.pi4j.io.gpio.trigger.GpioSyncStateTrigger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.concurrent.Callable;

public class MotionAlarm {

    private final static int CHECK_DURATION = 2000;

    private final GpioController gpio = GpioFactory.getInstance();
    private final GpioPinDigitalOutput led = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "LED", PinState.LOW);
    private final GpioPinDigitalInput motionSensor = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03, "MOTION");

    private boolean isAudioPlaying = false;

    private Timestamp startTime = new Timestamp(1460637000000L);
    private Timestamp endTime = new Timestamp(1460644200000L);

    public MotionAlarm() {
        init();
    }

    private void init() {
	led.setShutdownOptions(true, PinState.LOW);
        motionSensor.addTrigger(new GpioSyncStateTrigger(led));
        motionSensor.addTrigger(new GpioCallbackTrigger(new Callable<Void>() {
            public Void call() throws Exception {
                if (!isAudioPlaying && isTheRightTime()) {
                    playAudio();
                }
                return null;
            }
        }));
    }

    public void loop() throws InterruptedException {
        for (; ; ) {
            Thread.sleep(CHECK_DURATION);
        }
    }

    private boolean isTheRightTime() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        return now.after(startTime) && now.before(endTime);
    }

    private void playAudio() throws FileNotFoundException, JavaLayerException {
        FileInputStream fis = new FileInputStream("/home/pi/Downloads/bach.mp3");
        AdvancedPlayer player = new AdvancedPlayer(fis);
        player.setPlayBackListener(new PlaybackListener() {
            @Override
            public void playbackFinished(PlaybackEvent event) {
                isAudioPlaying = false;
            }
        });
        player.play(2700);
        isAudioPlaying = true;
    }
}
