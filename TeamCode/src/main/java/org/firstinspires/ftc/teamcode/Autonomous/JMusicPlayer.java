package org.firstinspires.ftc.teamcode.Autonomous;

import android.media.MediaPlayer;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Disabled
@SuppressWarnings("unused")
@Autonomous
public final class JMusicPlayer extends LinearOpMode {

    private static final Object _lock = new Object();

    /**
     * Override this method and place your code here.
     * <p>
     * Please do not swallow the InterruptedException, as it is used in cases
     * where the op mode needs to be terminated early.
     *
     */
    @Override
    public void runOpMode() {

        this.waitForStart();

        ExecutorService executor = Executors.newCachedThreadPool();

        while (this.opModeIsActive()) {

            if (this.gamepad1.x) {
                Future<?> task = executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        playMusic("/storage/emulated/0/music/test.mp3");
                    }
                });
            }
        }

    }

    private void playMusic(String path) {
        synchronized (_lock) {
            MediaPlayer mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepare();
                mediaPlayer.start();
                while (true) {
                    if (!mediaPlayer.isPlaying() ||
                            this.isStopRequested() ||
                            !this.opModeIsActive()) {
                        break;
                    }
                }
                mediaPlayer.stop();
            } catch (Exception e) {
                Log.e("JMusicPlayer", e.getMessage(), e);
            } finally {
                mediaPlayer.release();
            }
        }
    }
}
