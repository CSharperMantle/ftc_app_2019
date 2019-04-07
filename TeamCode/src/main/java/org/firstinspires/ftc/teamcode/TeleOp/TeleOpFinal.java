package org.firstinspires.ftc.teamcode.TeleOp;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.SharedHelper;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.firstinspires.ftc.teamcode.SharedHelper.Direction.Unknown;

@SuppressWarnings("unused")
@TeleOp(name = "2019 TeleOp Final")
public final class TeleOpFinal extends LinearOpMode {
    private static final String TAG = "TeleOpFinal";

    @Override
    public void runOpMode() throws InterruptedException {
        TeleOpFacade facade = new TeleOpFacade(this.hardwareMap);

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        Future<?> drivingTaskResult = executorService.submit(new DrivingHandlerRunnable(this, facade));
        Future<?> armTaskResult = executorService.submit(new ArmMovingHandlerRunnable(this, facade));
        Future<?> hookTaskResult = executorService.submit(new HookMovingHandlerRunnable(this, facade));

        this.waitForStart();

        try {
            drivingTaskResult.get();
            armTaskResult.get();
            hookTaskResult.get();
        } catch (ExecutionException ex) {
            Log.w(TAG, ex);
        }

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
                    double turn = gamepad1.right_stick_x;
                    double leftPower = Range.clip(drive + turn, -1.0, 1.0) ;
                    double rightPower = Range.clip(drive - turn, -1.0, 1.0) ;

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
                if (this.opMode.gamepad1.x) {
                    this.facade.moveHangingHook(true);
                }
                if (this.opMode.gamepad1.y) {
                    this.facade.moveHangingHook(false);
                }

                if (this.opMode.gamepad1.a) {
                    this.facade.moveEscalatorMotor(SharedHelper.Direction.Forward, 0.5);
                }
                if (this.opMode.gamepad1.b) {
                    this.facade.moveEscalatorMotor(SharedHelper.Direction.Backward, 0.5);
                }
                this.facade.moveEscalatorMotor(SharedHelper.Direction.Forward, 0);
            }
        }
    }
}
