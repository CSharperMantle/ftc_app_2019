package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.SharedHelper;

@SuppressWarnings("unused")
@Autonomous(name = "Drive by encoders")
public final class DriveByEncoder extends LinearOpMode {
    /**
     * Override this method and place your code here.
     * <p>
     * Please do not swallow the InterruptedException, as it is used in cases
     * where the op mode needs to be terminated early.
     *
     */
    @Override
    public void runOpMode() {
        AutonomousFacade facade = new AutonomousFacade(this.hardwareMap);

        this.waitForStart();

        while (this.opModeIsActive()) {
            if (this.gamepad1.x) {
                facade.driveWithEncoder(0.8, 1000, 1000, 15,
                        SharedHelper.Direction.Forward,
                        this);
            }
        }

        facade.close();
    }
}
