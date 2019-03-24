package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Autonomous.AutonomousFacade;

import java.util.Objects;
import java.util.Vector;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.FLOAT;
import static org.firstinspires.ftc.robotcore.external.Telemetry.Log.DisplayOrder.NEWEST_FIRST;
import static org.firstinspires.ftc.teamcode.SharedHelper.Direction.Backward;
import static org.firstinspires.ftc.teamcode.SharedHelper.Direction.Forward;
import static org.firstinspires.ftc.teamcode.SharedHelper.Direction.TurnLeft;
import static org.firstinspires.ftc.teamcode.SharedHelper.Direction.TurnRight;

@SuppressWarnings("unused")
@TeleOp(group = "Test")
public final class LocatedDriving extends LinearOpMode {
    private static final String TAG = "LocatedDriving";

    private final Vector<AutonomousFacade.RobotPosition> positionVector = new Vector<>();

    @Override
    public void runOpMode() throws InterruptedException {
        // HACK: To use a facade for Autonomous Period to display the location
        AutonomousFacade facade = new AutonomousFacade(this.hardwareMap);
        this.telemetry.log().setDisplayOrder(NEWEST_FIRST);

        facade.leftDrive.setZeroPowerBehavior(FLOAT);
        facade.rightDrive.setZeroPowerBehavior(FLOAT);

        Thread drivingThread = new Thread(new DrivingHandlerRunnable(this, facade));
        Thread locatingThread = new Thread(new LocatingRunnable(this, facade));
        Thread locationPrintingThread = new Thread(new LocationPrintingRunnable(this, facade));

        this.waitForStart();

        facade.engage();

        drivingThread.start();
        locatingThread.start();
        locationPrintingThread.start();

        drivingThread.join();
        locatingThread.join();
        locationPrintingThread.join();

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
                    this.facade.driveSeparated(Forward, 0.5, 0.5);
                }
                if (this.opMode.gamepad1.dpad_down) {
                    this.facade.driveSeparated(Backward, 0.5, 0.5);
                }
                if (this.opMode.gamepad1.dpad_left) {
                    this.facade.driveSeparated(TurnLeft, 0.5, 0.5);
                }
                if (this.opMode.gamepad1.dpad_right) {
                    this.facade.driveSeparated(TurnRight, 0.5, 0.5);
                }
                this.facade.stopAllDrivingMotors();
            }
        }
    }

    private class LocatingRunnable implements Runnable {
        private final LinearOpMode opMode;
        private final AutonomousFacade facade;

        private LocatingRunnable(LinearOpMode opMode, AutonomousFacade facade) {
            this.opMode = opMode;
            this.facade = facade;
        }

        @Override
        public void run() {
            while (this.opMode.opModeIsActive()) {
                if (this.opMode.gamepad1.x) {
                    while (this.opMode.opModeIsActive()) {
                        if (this.facade.refreshRobotPosition()) {
                            break;
                        }
                    }
                    try {
                        AutonomousFacade.RobotPosition position = Objects.requireNonNull(facade.getLatestRobotPosition());
                        positionVector.add(position);
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

                if (this.opMode.gamepad1.a) {
                    this.facade.driveSeparated(TurnLeft, 0.5, 0.5);
                    while (this.opMode.opModeIsActive()) {
                        if (this.facade.refreshRobotPosition()) {
                            break;
                        }
                    }
                    this.facade.stopAllDrivingMotors();
                    this.opMode.telemetry.addData(TAG, this.facade.getLatestRobotPosition().toString());
                    this.opMode.telemetry.update();
                }
            }
        }
    }

    private class LocationPrintingRunnable implements Runnable {
        private final LinearOpMode opMode;
        private final AutonomousFacade facade;

        private LocationPrintingRunnable(LinearOpMode opMode, AutonomousFacade facade) {
            this.opMode = opMode;
            this.facade = facade;
        }

        @Override
        public void run() {
            while (this.opMode.opModeIsActive()) {
                if (this.opMode.gamepad1.y) {
                    this.opMode.telemetry.log().add("START PRINTING POSITION");
                    for (AutonomousFacade.RobotPosition pos : positionVector) {
                        this.opMode.telemetry.log().add(pos.toString());
                    }
                    this.opMode.telemetry.log().add("END PRINTING POSITION");
                    this.opMode.telemetry.update();
                }
            }
        }
    }
}
