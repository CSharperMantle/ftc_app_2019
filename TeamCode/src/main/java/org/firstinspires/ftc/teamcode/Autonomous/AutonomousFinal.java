package org.firstinspires.ftc.teamcode.Autonomous;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@SuppressWarnings("unused")
@Autonomous(name = "2019 Autonomous Final")
public final class AutonomousFinal extends LinearOpMode {

    private static final String TAG = "2019 Autonomous Final";

    @Override
    public void runOpMode() {
        AutonomousFacade facade = new AutonomousFacade(this.hardwareMap);
        facade.engage();

        AutonomousFinal_PhasePipeline phasePipeline = new AutonomousFinal_PhasePipeline(facade,
                this);

        this.waitForStart();

        if (phasePipeline.pipelinePhasesAndExecute()) {
            this.telemetry.addData(TAG, "Finished and completed!");
            this.telemetry.update();
            Log.i(TAG, "Finished and completed!");
        } else {
            this.telemetry.addData(TAG, "Finished but failed.");
            this.telemetry.update();
            Log.e(TAG, "Finished but failed.");
        }

        facade.close();
    }
}
