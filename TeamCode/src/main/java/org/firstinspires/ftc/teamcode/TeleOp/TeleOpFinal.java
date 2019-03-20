package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SharedHelper;

@SuppressWarnings("unused")
@TeleOp(name = "2019 TeleOp Final")
public final class TeleOpFinal extends LinearOpMode {
    /**
     * Override this method and place your code here.
     * <p>
     * Please do not swallow the InterruptedException, as it is used in cases
     * where the op mode needs to be terminated early.
     *
     */
    @Override
    public void runOpMode() {
        TeleOpFacade facade = new TeleOpFacade(this.hardwareMap);

        this.waitForStart();

        while (this.opModeIsActive()) {
            if (gamepad1.dpad_up) {
                facade.driveSeparated(SharedHelper.Direction.Forward, 0.8, 0.8);
            }
            if (gamepad1.dpad_down) {
                facade.driveSeparated(SharedHelper.Direction.Backward, 0.8, 0.8);
            }
            if (gamepad1.dpad_left) {
                facade.driveSeparated(SharedHelper.Direction.TurnLeft, 0.5, 0.5);
            }
            if (gamepad1.dpad_right) {
                facade.driveSeparated(SharedHelper.Direction.TurnRight, 0.5, 0.5);
            }

            if (gamepad1.left_trigger > 0) {
                facade.moveRoboticArmLevel1(SharedHelper.Direction.Forward, 0.5);
            }
            if (gamepad1.left_bumper) {
                facade.moveRoboticArmLevel1(SharedHelper.Direction.Backward, 0.5);
            }
            if (gamepad1.right_trigger > 0) {
                facade.moveRoboticArmLevel2(SharedHelper.Direction.Forward, 0.5);
            }
            if (gamepad1.right_bumper) {
                facade.moveRoboticArmLevel2(SharedHelper.Direction.Backward, 0.5);
            }

            facade.stopAllMotors();
        }

        facade.closeDevice();
    }
}
