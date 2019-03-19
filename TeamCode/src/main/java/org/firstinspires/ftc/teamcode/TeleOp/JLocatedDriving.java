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

        Thread drivingThread = new Thread(new DrivingHandlerRunnable(this, facade));
        Thread locatingThread = new Thread(new LocatorHandlerRunnable(this, facade));

        this.waitForStart();

        facade.engage();

        drivingThread.start();
        locatingThread.start();

        facade.close();
    }

    private class DrivingHandlerRunnable implements Runnable {
        private final LinearOpMode opMode;
        private final AutonomousFacade facade;

        private DrivingHandlerRunnable(LinearOpMode opMode, AutonomousFacade facade) {
            this.opMode = opMode;
            this.facade = facade;
        }

        @Override
        public void run() {
            while (this.opMode.opModeIsActive()) {
                if (this.opMode.gamepad1.dpad_up) {
                    this.facade.driveSeparated(Forward, 0.8, 0.8);
                }
                if (this.opMode.gamepad1.dpad_down) {
                    this.facade.driveSeparated(Backward, 0.8, 0.8);
                }
                if (this.opMode.gamepad1.dpad_left) {
                    this.facade.driveSeparated(TurnLeft, 0.8, 0.8);
                }
                if (this.opMode.gamepad1.dpad_right) {
                    this.facade.driveSeparated(TurnRight, 0.8, 0.8);
                }
                this.facade.stopAllDrivingMotors();
            }
        }
    }

    private class LocatorHandlerRunnable implements Runnable {
        private final LinearOpMode opMode;
        private final AutonomousFacade facade;

        private LocatorHandlerRunnable(LinearOpMode opMode, AutonomousFacade facade) {
            this.opMode = opMode;
            this.facade = facade;
        }

        @Override
        public void run() {
            while (this.opMode.opModeIsActive()) {
                if (this.opMode.gamepad1.x) {
                    while (true) {
                        if (this.facade.refreshRobotPosition() || !this.opMode.opModeIsActive()) {
                            break;
                        }
                    }
                    try {
                        AutonomousFacade.RobotPosition position = Objects.requireNonNull(facade.getLatestRobotPosition());
                        this.opMode.telemetry.addData("Pos (inch)", "{X, Y, Z} = %.1f, %.1f, %.1f",
                                position.X, position.Y, position.Z);
                        this.opMode.telemetry.addData("Rot (deg)", "{Roll, Pitch, Yaw} = %.0f, %.0f, %.0f",
                                position.Roll, position.Pitch, position.Yaw);
                        this.opMode.telemetry.update();
                    } catch (NullPointerException ex) {
                        this.opMode.telemetry.log().add("Position unknown.");
                        this.opMode.telemetry.log().add(ex.toString());
                        this.opMode.telemetry.update();
                    }
                }
            }
        }
    }
}
