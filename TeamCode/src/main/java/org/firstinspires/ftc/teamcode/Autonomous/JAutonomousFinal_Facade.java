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

import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.CAMERA_DIRECTION;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.COUNTS_PER_CM;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.DRIVE_HAND_LEVEL_1_LEFT;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.DRIVE_HAND_LEVEL_1_RIGHT;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.DRIVE_LEFT_NAME;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.DRIVE_RIGHT_NAME;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.LABEL_GOLD_MINERAL;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.LABEL_SILVER_MINERAL;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.MINIMUM_CONFIDENCE;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.SERVO_HAND_LEVEL_2_LEFT_NAME;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.SERVO_HAND_LEVEL_2_RIGHT_NAME;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.SERVO_HAND_LEVEL_3_LEFT_NAME;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.SERVO_HAND_LEVEL_3_RIGHT_NAME;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.TFOD_MODEL_ASSET;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.VUFORIA_LICENSE_KEY;

public final class JAutonomousFinal_Facade {
    public final DcMotor leftDrive;
    public final DcMotor rightDrive;
    public final DcMotor level1LeftDrive;
    public final DcMotor level1RightDrive;

    public final Servo level2LeftServo;
    public final Servo level2RightServo;
    public final Servo level3LeftServo;
    public final Servo level3RightServo;

    public final VuforiaLocalizer vuforia;
    public final TFObjectDetector mineralDetector;

    private final HardwareMap hardwareMapRef;

    public JAutonomousFinal_Facade(HardwareMap hardwareMap) {
        this.hardwareMapRef = hardwareMap;

        // Hardware (servos, drives, etc.) init phase
        this.leftDrive = this.hardwareMapRef.get(DcMotor.class, DRIVE_LEFT_NAME);
        this.rightDrive = this.hardwareMapRef.get(DcMotor.class, DRIVE_RIGHT_NAME);
        this.leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.level1LeftDrive = this.hardwareMapRef.get(DcMotor.class, DRIVE_HAND_LEVEL_1_LEFT);
        this.level1RightDrive = this.hardwareMapRef.get(DcMotor.class, DRIVE_HAND_LEVEL_1_RIGHT);

        this.level2LeftServo = this.hardwareMapRef.get(Servo.class, SERVO_HAND_LEVEL_2_LEFT_NAME);
        this.level2RightServo = this.hardwareMapRef.get(Servo.class, SERVO_HAND_LEVEL_2_RIGHT_NAME);
        this.level3LeftServo = this.hardwareMapRef.get(Servo.class, SERVO_HAND_LEVEL_3_LEFT_NAME);
        this.level3RightServo = this.hardwareMapRef.get(Servo.class, SERVO_HAND_LEVEL_3_RIGHT_NAME);

        // Vuforia init phase
        VuforiaLocalizer.Parameters vuParams = new VuforiaLocalizer.Parameters();
        vuParams.vuforiaLicenseKey = VUFORIA_LICENSE_KEY;
        vuParams.cameraDirection = CAMERA_DIRECTION;
        this.vuforia = ClassFactory.getInstance().createVuforia(vuParams);

        // TFObjectDetector check phase
        if (!ClassFactory.getInstance().canCreateTFObjectDetector()) {
            throw new RuntimeException("TFObjectDetector not supported");
        }

        // TFObjectDetector init phase
        TFObjectDetector.Parameters tfParams = new TFObjectDetector.Parameters();
        tfParams.minimumConfidence = MINIMUM_CONFIDENCE;
        this.mineralDetector = ClassFactory.getInstance().createTFObjectDetector(tfParams,
                this.vuforia);
        this.mineralDetector.loadModelFromAsset(TFOD_MODEL_ASSET,
                LABEL_GOLD_MINERAL,
                LABEL_SILVER_MINERAL);
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
        newLeftTarget = this.leftDrive.getCurrentPosition() + (int) (leftCMs * COUNTS_PER_CM);
        newRightTarget = this.rightDrive.getCurrentPosition() + (int) (rightCMs * COUNTS_PER_CM);
        this.leftDrive.setTargetPosition(newLeftTarget);
        this.rightDrive.setTargetPosition(newRightTarget);

        // Turn On RUN_TO_POSITION
        this.leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        runtime.reset();
        this.leftDrive.setPower(Math.abs(speed));
        this.rightDrive.setPower(Math.abs(speed));

        while (true) {

            if (!linearOpModeForRef.opModeIsActive() ||
                    (runtime.seconds() >= timeoutS) ||
                    !(this.leftDrive.isBusy() || this.rightDrive.isBusy())) {
                break;
            }
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

        this.level1LeftDrive.close();
        this.level1RightDrive.close();
        this.level2LeftServo.close();
        this.level2RightServo.close();
        this.level3LeftServo.close();
        this.level3RightServo.close();

        this.mineralDetector.deactivate();
        this.mineralDetector.shutdown();
    }
}
