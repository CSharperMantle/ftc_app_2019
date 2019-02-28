package org.firstinspires.ftc.teamcode.Autonomous;

import android.media.MediaPlayer;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.JTeamCode_Shared;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.AUTONOMOUS_FINISH_MUSIC_PATH;

@SuppressWarnings("unused")
public final class JAutonomousFinal_PhasePipeline {

    /**
     * An instance of {@link JAutonomousFinal_Facade} for reference
     */
    private final JAutonomousFinal_Facade facadeRef;

    /**
     * An instance of {@link LinearOpMode} for reference
     */
    private final LinearOpMode opModeRef;

    /**
     * An instance of {@link Object} in order to provide locks
     */
    private static final Object _lock = new Object();

    private static final String TAG = "JAutonomousPipeline";


    public JAutonomousFinal_PhasePipeline(JAutonomousFinal_Facade facadeForRef,
                                          LinearOpMode opModeForRef) {
        this.facadeRef = facadeForRef;
        this.opModeRef = opModeForRef;
    }

    private boolean lowerToGround() {
        //TODO: Finish this phase



        return false;
    }

    private JTeamCode_Shared.Position detectSamplingMineral(boolean isLastPhaseSuccessful) {

        if (!isLastPhaseSuccessful)
            return JTeamCode_Shared.Position.Unknown;

        this.facadeRef.mineralDetector.activate();
        List<Recognition> updatedRecogs;
        while (this.opModeRef.opModeIsActive()) {
            updatedRecogs = this.facadeRef.mineralDetector.getUpdatedRecognitions();
            if (updatedRecogs != null) {
                if (updatedRecogs.size() == 3) {
                    int goldMineralX = -1;
                    int silverMineral1X = -1;
                    int silverMineral2X = -1;
                    for (Recognition recog : updatedRecogs) {
                        if (recog.getLabel().equals(JTeamCode_Shared.LABEL_GOLD_MINERAL)) {
                            goldMineralX = (int) recog.getLeft();
                        } else if (silverMineral1X == -1) {
                            silverMineral1X = (int) recog.getLeft();
                        } else {
                            silverMineral2X = (int) recog.getLeft();
                        }
                    }
                    if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                        if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                            return JTeamCode_Shared.Position.Left;
                        } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                            return JTeamCode_Shared.Position.Right;
                        } else {
                            return JTeamCode_Shared.Position.Center;
                        }
                    }
                }
            }
        }

        return JTeamCode_Shared.Position.Unknown;
    }

    private boolean pushSamplingMineral(JTeamCode_Shared.Position mineralPosition) {
        //TODO: Finish this phase

        if (mineralPosition == JTeamCode_Shared.Position.Unknown)
            return false;

        return false;
    }

    private boolean placeTeamMarker(boolean isLastPhaseSuccessful) {
        //TODO: Finish this phase

        if (!isLastPhaseSuccessful)
            return false;

        return false;
    }

    private boolean parkInCrater(boolean isLastPhaseSuccessful) {
        //TODO: Finish this phase

        if (!isLastPhaseSuccessful)
            return false;

        return false;
    }

    private boolean playMusicOfVictory(boolean isLastPhaseSuccessful) {
        //TODO: Finish this phase

        if (!isLastPhaseSuccessful)
            return false;

        boolean isSuccessful;

        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<?> future = executor.submit(new MusicPlayerRunnable());
            isSuccessful = true;
        } catch (RejectedExecutionException ex) {
            Log.e(TAG, "Execution rejected. Music will not be played.", ex);
            isSuccessful = false;
        }

        return isSuccessful;
    }

    public boolean pipelinePhasesAndExecute() {
        boolean isLastPhaseSuccessful;
        JTeamCode_Shared.Position mineralPosition;

        isLastPhaseSuccessful = this.lowerToGround();
        mineralPosition = this.detectSamplingMineral(isLastPhaseSuccessful);
        isLastPhaseSuccessful = this.pushSamplingMineral(mineralPosition);
        isLastPhaseSuccessful = this.placeTeamMarker(isLastPhaseSuccessful);
        isLastPhaseSuccessful = this.parkInCrater(isLastPhaseSuccessful);
        isLastPhaseSuccessful = this.playMusicOfVictory(isLastPhaseSuccessful);
        return isLastPhaseSuccessful;
    }

    private class MusicPlayerRunnable implements Runnable {

        /**
         * Starts executing the active part of the class' code. This method is
         * called when a thread is started that has been created with a class which
         * implements {@code Runnable}.
         */
        @Override
        public void run() {
            synchronized (_lock) {
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(AUTONOMOUS_FINISH_MUSIC_PATH);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    while (true) {
                        if (!mediaPlayer.isPlaying() ||
                                opModeRef.isStopRequested() ||
                                !opModeRef.opModeIsActive()) {
                            break;
                        }
                    }
                } catch (IOException ex) {
                    Log.e(TAG, "IO error occurred. Music will not be played.", ex);
                } finally {
                    mediaPlayer.release();
                }
            }
        }
    }
}
