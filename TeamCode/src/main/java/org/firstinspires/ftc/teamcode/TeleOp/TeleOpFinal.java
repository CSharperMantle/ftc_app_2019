package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.SharedHelper;

import static org.firstinspires.ftc.teamcode.SharedHelper.Direction.Unknown;

@SuppressWarnings("unused")
@TeleOp(name = "2019 TeleOp Final")
public final class TeleOpFinal extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        TeleOpFacade facade = new TeleOpFacade(this.hardwareMap);
        
        Thread drivingThread = new Thread(new DrivingHandlerRunnable(this, facade));
        Thread armMovingThread = new Thread(new ArmMovingHandlerRunnable(this, facade));
        Thread hookMovingThread = new Thread(new HookMovingHandlerRunnable(this, facade));

        this.waitForStart();

        drivingThread.start();
        armMovingThread.start();
        hookMovingThread.start();

        drivingThread.join();
        armMovingThread.join();
        hookMovingThread.join();

        facade.closeDevice();
    }
    
    private class DrivingHandlerRunnable implements Runnable {
        private final LinearOpMode opMode;
        private final TeleOpFacade facade;
        
        private DrivingHandlerRunnable(LinearOpMode opMode, TeleOpFacade facade) {
            this.opMode = opMode;
            this.facade = facade;
        }
        
        @Override
        public void run() {
            while (this.opMode.opModeIsActive()) {
                if (this.opMode.gamepad1.dpad_up) {
                    this.facade.driveSeparated(SharedHelper.Direction.Forward, 0.8, 0.8);
                }
                if (this.opMode.gamepad1.dpad_down) {
                    this.facade.driveSeparated(SharedHelper.Direction.Backward, 0.8, 0.8);
                }
                if (this.opMode.gamepad1.dpad_left) {
                    this.facade.driveSeparated(SharedHelper.Direction.TurnLeft, 0.5, 0.5);
                }
                if (this.opMode.gamepad1.dpad_right) {
                    this.facade.driveSeparated(SharedHelper.Direction.TurnRight, 0.5, 0.5);
                }
                this.facade.stopAllDrivingMotors();
            }
        }
    }

    private class ArmMovingHandlerRunnable implements Runnable {
        private final LinearOpMode opMode;
        private final TeleOpFacade facade;

        private ArmMovingHandlerRunnable(LinearOpMode opMode, TeleOpFacade facade) {
            this.opMode = opMode;
            this.facade = facade;
        }

        @Override
        public void run() {
            while (this.opMode.opModeIsActive()) {
                if (this.opMode.gamepad1.left_trigger > 0) {
                    facade.moveRoboticArmLevel1(SharedHelper.Direction.Forward, 0.5);
                }
                if (this.opMode.gamepad1.left_bumper) {
                    facade.moveRoboticArmLevel1(SharedHelper.Direction.Backward, 0.5);
                }
                if (this.opMode.gamepad1.right_trigger > 0) {
                    facade.moveRoboticArmLevel2(SharedHelper.Direction.Forward, 0.5);
                }
                if (this.opMode.gamepad1.right_bumper) {
                    facade.moveRoboticArmLevel2(SharedHelper.Direction.Backward, 0.5);
                }
                facade.stopAllArmMotors();

                if (this.opMode.gamepad1.left_stick_y != 0
                        || this.opMode.gamepad1.right_stick_x != 0) {
                    double drive = -gamepad1.left_stick_y;
                    double turn  =  gamepad1.right_stick_x;
                    double leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
                    double rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;

                    this.facade.driveSeparated(Unknown, leftPower, rightPower);
                }
            }
        }
    }

    private class HookMovingHandlerRunnable implements Runnable {
        private final LinearOpMode opMode;
        private final TeleOpFacade facade;

        private HookMovingHandlerRunnable(LinearOpMode opMode, TeleOpFacade facade) {
            this.opMode = opMode;
            this.facade = facade;
        }

        @Override
        public void run() {
            while (this.opMode.opModeIsActive()) {
                if (gamepad1.x) {
                    facade.moveHangingHook(true);
                }
                if (gamepad1.y) {
                    facade.moveHangingHook(false);
                }
            }
        }
    }
}
