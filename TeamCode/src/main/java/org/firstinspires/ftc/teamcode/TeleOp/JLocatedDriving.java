package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Autonomous.AutonomousFacade;

import java.util.Objects;

import static org.firstinspires.ftc.robotcore.external.Telemetry.Log.DisplayOrder.NEWEST_FIRST;
import static org.firstinspires.ftc.teamcode.SharedHelper.Direction.Backward;
import static org.firstinspires.ftc.teamcode.SharedHelper.Direction.Forward;
import static org.firstinspires.ftc.teamcode.SharedHelper.Direction.TurnLeft;
import static org.firstinspires.ftc.teamcode.SharedHelper.Direction.TurnRight;

@SuppressWarnings("unused")
@TeleOp
public final class JLocatedDriving extends LinearOpMode {
    @Override
    public void runOpMode() {
        // HACK: To use a facade for Autonomous Period to display the location
        AutonomousFacade facade = new AutonomousFacade(this.hardwareMap);
        this.telemetry.log().setDisplayOrder(NEWEST_FIRST);

        this.waitForStart();

        facade.engage();
        while (this.opModeIsActive()) {
            if (this.gamepad1.dpad_up) {
                facade.driveSeparated(Forward, 0.8, 0.8);
            }
            if (this.gamepad1.dpad_down) {
                facade.driveSeparated(Backward, 0.8, 0.8);
            }
            if (this.gamepad1.dpad_left) {
                facade.driveSeparated(TurnLeft, 0.8, 0.8);
            }
            if (this.gamepad1.dpad_right) {
                facade.driveSeparated(TurnRight, 0.8, 0.8);
            }
            if (this.gamepad1.x) {
                while (true) {
                    if (facade.refreshRobotPosition() || !this.opModeIsActive()) {
                        break;
                    }
                }
                try {
                    AutonomousFacade.RobotPosition position = Objects.requireNonNull(facade.getLatestRobotPosition());
                    this.telemetry.addData("Pos (inch)", "{X, Y, Z} = %.1f, %.1f, %.1f",
                            position.X, position.Y, position.Z);
                    this.telemetry.addData("Rot (deg)", "{Roll, Pitch, Yaw} = %.0f, %.0f, %.0f",
                            position.Roll, position.Pitch, position.Yaw);
                    this.telemetry.update();
                } catch (NullPointerException ex) {
                    this.telemetry.log().add("Position unknown.");
                    this.telemetry.log().add(ex.toString());
                    this.telemetry.update();
                }
            }
            facade.stopAllDrivingMotors();
        }

        facade.close();
    }
}
