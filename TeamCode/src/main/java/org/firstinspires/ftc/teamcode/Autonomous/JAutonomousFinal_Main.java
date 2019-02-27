package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@SuppressWarnings("unused")
@Autonomous(name = "2019 Autonomous Final")
public final class JAutonomousFinal_Main extends LinearOpMode {
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

        phasePipeline.pipelinePhasesAndExecute();

        facade.close();
    }
}
