package org.firstinspires.ftc.teamcode.Autonomous;

import android.media.MediaPlayer;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.SharedHelper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

import static org.firstinspires.ftc.teamcode.SharedHelper.AUTONOMOUS_FINISH_MUSIC_PATH;

@SuppressWarnings("unused")
public final class AutonomousFinal_PhasePipeline {

    private static final String TAG = "JAutonomousPipeline";

    /**
     * An instance of {@link AutonomousFacade} for reference
     */
    private final AutonomousFacade facadeRef;

    /**
     * An instance of {@link LinearOpMode} for reference
     */
    private final LinearOpMode opModeRef;

    /**
     * An instance of {@link Object} in order to provide locks
     */
    private static final Object _lock = new Object();

    private AutonomousFacade.RobotPosition lastPosition = null;

    private boolean targetVisible = false;

    public AutonomousFinal_PhasePipeline(AutonomousFacade facadeForRef,
                                         LinearOpMode opModeForRef) {
        this.facadeRef = facadeForRef;
        this.opModeRef = opModeForRef;
    }

    private boolean lowerToGround() {
        //TODO: Control the hardware to lower down to the ground

        return false;
    }

    private SharedHelper.Position detectSamplingMineral(boolean isLastPhaseSuccessful) {

        if (!isLastPhaseSuccessful || !this.opModeRef.opModeIsActive())
            return SharedHelper.Position.Unknown;

        //TODO: Head down to the mineral
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
                        if (recog.getLabel().equals(SharedHelper.LABEL_GOLD_MINERAL)) {
                            goldMineralX = (int) recog.getLeft();
                        } else if (silverMineral1X == -1) {
                            silverMineral1X = (int) recog.getLeft();
                        } else {
                            silverMineral2X = (int) recog.getLeft();
                        }
                    }
                    if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                        if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                            return SharedHelper.Position.Left;
                        } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                            return SharedHelper.Position.Right;
                        } else {
                            return SharedHelper.Position.Center;
                        }
                    }
                }
            }
        }

        return SharedHelper.Position.Unknown;
    }

    private boolean pushSamplingMineral(SharedHelper.Position mineralPosition) {
        if (mineralPosition == SharedHelper.Position.Unknown || !this.opModeRef.opModeIsActive())
            return false;

        //TODO: Control hardware arms to push away the gold

        return false;
    }

    private boolean placeTeamMarker(boolean isLastPhaseSuccessful) {
        if (!isLastPhaseSuccessful || !this.opModeRef.opModeIsActive())
            return false;

        //TODO: Drive and place the team marker

        this.refreshLocationBlocked();
        int dimension = this.lastPosition.getDimensionNumber();

        switch (dimension) {
            case 1:
                this.facadeRef.driveSeparated(SharedHelper.Direction.TurnLeft, 0.5, 0.5);
                while (true) {
                    this.refreshLocationBlocked();
                    if (this.lastPosition.Yaw <= SharedHelper.BogusObject.BOGUS_FLOAT) {
                        //TODO: Get the real number to replace the bogus number
                        break;
                    }
                }
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            default:
                break;
        }

        return false;
    }

    private boolean parkInCrater(boolean isLastPhaseSuccessful) {
        if (!isLastPhaseSuccessful || !this.opModeRef.opModeIsActive())
            return false;

        //TODO: Drive and park

        return false;
    }

    private boolean playMusicOfVictory(boolean isLastPhaseSuccessful) {
        if (!isLastPhaseSuccessful || !this.opModeRef.opModeIsActive())
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
        SharedHelper.Position mineralPosition;

        isLastPhaseSuccessful = this.lowerToGround();
        mineralPosition = this.detectSamplingMineral(isLastPhaseSuccessful);
        isLastPhaseSuccessful = this.pushSamplingMineral(mineralPosition);
        isLastPhaseSuccessful = this.placeTeamMarker(isLastPhaseSuccessful);
        isLastPhaseSuccessful = this.parkInCrater(isLastPhaseSuccessful);
        isLastPhaseSuccessful = this.playMusicOfVictory(isLastPhaseSuccessful);
        return isLastPhaseSuccessful;
    }

    private void refreshLocationBlocked() {
        while (this.opModeRef.opModeIsActive()) {
            this.facadeRef.refreshRobotPosition();
            this.lastPosition = facadeRef.getLatestRobotPosition();
            if (this.lastPosition != null) {
                break;
            }
        }
    }

    private class MusicPlayerRunnable implements Runnable {

        private static final String TAG = "MusicPlayerRunnable";

        @Override
        public void run() {
            synchronized (_lock) {
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(AUTONOMOUS_FINISH_MUSIC_PATH);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    while (true) {
                        if (opModeRef.isStopRequested() ||
                                !opModeRef.opModeIsActive()) {
                            break;
                        }

                        if (!mediaPlayer.isPlaying()) {
                            Thread.sleep(1000);
                            mediaPlayer.reset();
                            mediaPlayer.start();
                        }
                    }
                } catch (IOException ex) {
                    Log.e(TAG, "IO error occurred. Music will not be played.", ex);
                } catch (InterruptedException ex) {
                    Log.e(TAG, "Thread interrupted.", ex);
                }
                finally {
                    mediaPlayer.release();
                }
            }
        }
    }
}
