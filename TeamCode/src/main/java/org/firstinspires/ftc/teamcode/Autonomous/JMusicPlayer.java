package org.firstinspires.ftc.teamcode.Autonomous;

import android.media.MediaPlayer;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.TEST_MUSIC_PATH;

@Disabled
@SuppressWarnings("unused")
@Autonomous
public final class JMusicPlayer extends LinearOpMode {

    public static final String TAG = "JMusicPlayer";

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

        ExecutorService executor = Executors.newFixedThreadPool(3);

        while (this.opModeIsActive()) {

            if (this.gamepad1.x) {
                try {
                    Future<?> task = executor.submit(new Runnable() {
                        @Override
                        public void run() {
                            playMusic(TEST_MUSIC_PATH);
                        }
                    });
                } catch (RejectedExecutionException e) {
                    Log.e(TAG, "Submit rejected.", e);
                }
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
            } catch (IOException e) {
                Log.e("JMusicPlayer", "IO Error occurred.", e);
            } finally {
                mediaPlayer.release();
            }
        }
    }
}
