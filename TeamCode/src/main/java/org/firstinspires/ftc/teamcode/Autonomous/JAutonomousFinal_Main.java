package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "2019 Autonomous Final")
public final class JAutonomousFinal_Main extends LinearOpMode {
    /**
     * Override this method and place your code here.
     * <p>
     * Please do not swallow the InterruptedException, as it is used in cases
     * where the op mode needs to be terminated early.
     *
     * @throws InterruptedException
     */
    @Override
    public void runOpMode() throws InterruptedException {
        JAutonomousFinal_Facade facade = new JAutonomousFinal_Facade(this.hardwareMap);

        this.waitForStart();

        while (this.opModeIsActive()) {

        }

        facade.close();
    }
}
