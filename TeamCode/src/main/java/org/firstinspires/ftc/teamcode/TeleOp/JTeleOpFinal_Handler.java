package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.SharedHelper;

public abstract class JTeleOpFinal_Handler {
    public static void gamepadsKeyHandler(TeleOpFacade facade, Gamepad gamepad1, Gamepad gamepad2) {
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
            facade.moveRoboticArmLevel1(SharedHelper.Direction.Forward, 0.3);
        }
        if (gamepad1.left_bumper) {
            facade.moveRoboticArmLevel1(SharedHelper.Direction.Backward, 0.3);
        }

        if (gamepad1.right_trigger > 0) {
            facade.moveRoboticArmLevel2(SharedHelper.Direction.Forward, 0.3);
        }
        if (gamepad1.right_bumper) {
            facade.moveRoboticArmLevel2(SharedHelper.Direction.Backward, 0.3);
        }
        facade.stopAllDrives();
    }
}
