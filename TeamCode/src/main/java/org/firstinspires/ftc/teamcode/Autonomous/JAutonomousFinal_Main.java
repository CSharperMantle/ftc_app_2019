package org.firstinspires.ftc.teamcode.Autonomous;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@SuppressWarnings("unused")
@Autonomous(name = "2019 Autonomous Final")
public final class JAutonomousFinal_Main extends LinearOpMode {

    public static final String TAG = "2019 Autonomous Final";

    /**
     * Override this method and place your code here.
     * <p>
     * Please do not swallow the InterruptedException, as it is used in cases
     * where the op mode needs to be terminated early.
     *
     */
    @Override
    public void runOpMode() {
        JAutonomousFinal_Facade facade = new JAutonomousFinal_Facade(this.hardwareMap);
        JAutonomousFinal_PhasePipeline phasePipeline = new JAutonomousFinal_PhasePipeline(facade,
                this);

        this.waitForStart();

        if (phasePipeline.pipelinePhasesAndExecute()) {
            this.telemetry.addData(TAG, "Finished and completed!");
            Log.i(TAG, "Finished and completed!");
        } else {
            this.telemetry.addData(TAG, "Finished but failed.");
            Log.w(TAG, "Finished but failed.");
        }

        facade.close();
    }
}
