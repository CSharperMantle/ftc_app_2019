package org.firstinspires.ftc.teamcode.Autonomous;

import android.media.MediaPlayer;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@SuppressWarnings("unused")
@Autonomous
public final class JMusicPlayer extends LinearOpMode {
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

        while (this.opModeIsActive()) {
            while (this.gamepad1.x) {
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource("/mnt/enmulated/0/music/test.mp3");
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    while (true) {
                        if (!mediaPlayer.isPlaying()) {
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
}
