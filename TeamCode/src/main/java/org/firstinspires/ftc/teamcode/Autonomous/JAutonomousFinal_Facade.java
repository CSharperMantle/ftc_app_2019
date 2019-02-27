package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.JTeamCode_Shared;

public class JAutonomousFinal_Facade {
    public final DcMotor leftDrive;
    public final DcMotor rightDrive;

    public final Servo level1LeftServo;
    public final Servo level1RightServo;
    public final Servo level2LeftServo;
    public final Servo level2RightServo;
    public final Servo level3LeftServo;
    public final Servo level3RightServo;

    public final VuforiaLocalizer vuforia;
    public final TFObjectDetector mineralDetector;

    public final HardwareMap hardwareMapRef;

    public JAutonomousFinal_Facade(HardwareMap hardwareMap) {
        this.hardwareMapRef = hardwareMap;

        // Hardware (servos, drives, etc.) init phase
        this.leftDrive = hardwareMap.get(DcMotor.class, JTeamCode_Shared.LEFT_DRIVE_NAME);
        this.rightDrive = hardwareMap.get(DcMotor.class, JTeamCode_Shared.RIGHT_DRIVE_NAME);
        this.leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.level1LeftServo = hardwareMap.get(Servo.class,
                JTeamCode_Shared.LEVEL_1_LEFT_SERVO_NAME);
        this.level1RightServo = hardwareMap.get(Servo.class,
                JTeamCode_Shared.LEVEL_1_RIGHT_SERVO_NAME);
        this.level2LeftServo = hardwareMap.get(Servo.class,
                JTeamCode_Shared.LEVEL_2_LEFT_SERVO_NAME);
        this.level2RightServo = hardwareMap.get(Servo.class,
                JTeamCode_Shared.LEVEL_2_RIGHT_SERVO_NAME);
        this.level3LeftServo = hardwareMap.get(Servo.class,
                JTeamCode_Shared.LEVEL_3_LEFT_SERVO_NAME);
        this.level3RightServo = hardwareMap.get(Servo.class,
                JTeamCode_Shared.LEVEL_3_RIGHT_SERVO_NAME);

        // Vuforia init phase
        VuforiaLocalizer.Parameters vuParams = new VuforiaLocalizer.Parameters();
        vuParams.vuforiaLicenseKey = JTeamCode_Shared.VUFORIA_LICENSE_KEY;
        vuParams.cameraDirection = JTeamCode_Shared.CAMERA_DIRECTION;
        this.vuforia = ClassFactory.getInstance().createVuforia(vuParams);

        // TFObjectDetector check phase
        if (!ClassFactory.getInstance().canCreateTFObjectDetector()) {
            throw new RuntimeException("TFObjectDetector not supported");
        }

        // TFObjectDetector init phase
        TFObjectDetector.Parameters tfParams = new TFObjectDetector.Parameters();
        tfParams.minimumConfidence = JTeamCode_Shared.MINIMUM_CONFIDENCE;
        this.mineralDetector = ClassFactory.getInstance().createTFObjectDetector(tfParams,
                this.vuforia);
        this.mineralDetector.loadModelFromAsset(JTeamCode_Shared.TFOD_MODEL_ASSET,
                JTeamCode_Shared.LABEL_GOLD_MINERAL,
                JTeamCode_Shared.LABEL_SILVER_MINERAL);
    }

    @Deprecated
    public void driveTogether(JTeamCode_Shared.Direction direction,
                              double power) {
        driveSeparated(direction, power, power);
    }

    public void driveSeparated(JTeamCode_Shared.Direction direction,
                               double leftPower, double rightPower) {
        JTeamCode_Shared.Direction leftDriveDirection;
        JTeamCode_Shared.Direction rightDriveDirection;

        switch (direction) {
            case Forward:
                leftDriveDirection = JTeamCode_Shared.Direction.Forward;
                rightDriveDirection = JTeamCode_Shared.Direction.Backward;
                break;
            case Backward:
                leftDriveDirection = JTeamCode_Shared.Direction.Backward;
                rightDriveDirection = JTeamCode_Shared.Direction.Forward;
                break;
            case TurnLeft:
                leftDriveDirection = JTeamCode_Shared.Direction.Forward;
                rightDriveDirection = JTeamCode_Shared.Direction.Forward;
                break;
            case TurnRight:
                leftDriveDirection = JTeamCode_Shared.Direction.Backward;
                rightDriveDirection = JTeamCode_Shared.Direction.Backward;
                break;
            default:
                throw new IllegalArgumentException("direction not acceptable");
        }

        this.leftDrive.setDirection(leftDriveDirection == JTeamCode_Shared.Direction.Forward ?
                DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);
        this.rightDrive.setDirection(rightDriveDirection == JTeamCode_Shared.Direction.Forward ?
                DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);

        this.leftDrive.setPower(Math.abs(leftPower));
        this.rightDrive.setPower(Math.abs(rightPower));
    }

    /**
     * Stop all drives.
     */
    public void stopAllDrives() {
        this.leftDrive.setPower(0);
        this.rightDrive.setPower(0);
    }

    /**
     * Drive the robot with encoders
     * @param speed An double from 0 to 1. Indicates speed.
     * @param leftCMs The inches which the left wheel should go.
     * @param rightCMs The inches which the right wheel should go.
     * @param timeoutS The timeout in seconds.
     * @param linearOpModeForRef LinearOpMode for reference.
     */
    public void driveWithEncoder(double speed,
                                double leftCMs, double rightCMs,
                                double timeoutS,
                                JTeamCode_Shared.Direction direction,
                                LinearOpMode linearOpModeForRef) {
        int newLeftTarget;
        int newRightTarget;
        ElapsedTime runtime = new ElapsedTime();

        // Determine new target position, and pass to motor controller
        newLeftTarget = this.leftDrive.getCurrentPosition() + (int) (leftCMs * JTeamCode_Shared.COUNTS_PER_CM);
        newRightTarget = this.rightDrive.getCurrentPosition() + (int) (rightCMs * JTeamCode_Shared.COUNTS_PER_CM);
        this.leftDrive.setTargetPosition(newLeftTarget);
        this.rightDrive.setTargetPosition(newRightTarget);

        // Turn On RUN_TO_POSITION
        this.leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        runtime.reset();
        this.leftDrive.setPower(Math.abs(speed));
        this.rightDrive.setPower(Math.abs(speed));

        while (linearOpModeForRef.opModeIsActive() &&
                (runtime.seconds() < timeoutS) &&
                (this.leftDrive.isBusy() || this.rightDrive.isBusy())) {

            // Blank while. Do nothing. Wait until finish or stop requested

            /*
            // Display it for the driver.
            linearOpModeForRef.telemetry.addData("Path1", "Running to %7d :%7d", newLeftTarget, newRightTarget);
            linearOpModeForRef.telemetry.addData("Path2", "Running at %7d :%7d",
                    this.leftDrive.getCurrentPosition(),
                    this.rightDrive.getCurrentPosition());
            linearOpModeForRef.telemetry.update();
            */
        }

        // Stop all motion;
        this.stopAllDrives();

        // Turn off RUN_TO_POSITION
        this.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void close() {
        this.leftDrive.close();
        this.rightDrive.close();

        this.level1LeftServo.close();
        this.level1RightServo.close();
        this.level2LeftServo.close();
        this.level2RightServo.close();
        this.level3LeftServo.close();
        this.level3RightServo.close();

        this.mineralDetector.deactivate();
        this.mineralDetector.shutdown();
    }
}
