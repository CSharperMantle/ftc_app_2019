package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.JTeamCode_Shared;

import java.util.List;

@SuppressWarnings("unused")
public final class JAutonomousFinal_PhasePipeline {
    private final JAutonomousFinal_Facade facadeRef;
    private final LinearOpMode opModeRef;


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

        return true;
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
}
