package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.JTeamCode_Shared;

public abstract class JTeleOpFinal_Handler {
    public static void gamepadsKeyHandler(JTeleOpFinal_Facade facade, Gamepad gamepad1, Gamepad gamepad2) {
        if (gamepad1.dpad_up) {
            facade.driveSeparated(JTeamCode_Shared.Direction.Forward, 0.8, 0.8);
        }
        if (gamepad1.dpad_down) {
            facade.driveSeparated(JTeamCode_Shared.Direction.Backward, 0.8, 0.8);
        }
        if (gamepad1.dpad_left) {
            facade.driveSeparated(JTeamCode_Shared.Direction.TurnLeft, 0.5, 0.5);
        }
        if (gamepad1.dpad_right) {
            facade.driveSeparated(JTeamCode_Shared.Direction.TurnRight, 0.5, 0.5);
        }

        if (gamepad1.a) {
            facade.moveRoboticArmLevel1(JTeamCode_Shared.Direction.Forward, 0.3);
        }
        if (gamepad1.y) {
            facade.moveRoboticArmLevel1(JTeamCode_Shared.Direction.Backward, 0.3);
        }

        if (gamepad1.b) {
            facade.moveRoboticArmLevel2(JTeamCode_Shared.Direction.Forward, 0.3);
        }
        if (gamepad1.x) {
        if (gamepad1.x) {
            facade.moveRoboticArmLevel2(JTeamCode_Shared.Direction.Backward, 0.3);
        }
        facade.stopAllDrives();
    }
}
