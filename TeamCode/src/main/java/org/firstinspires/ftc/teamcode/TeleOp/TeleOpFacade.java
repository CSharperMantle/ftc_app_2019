package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Facade.DrivingSimpleFacade;
import org.firstinspires.ftc.teamcode.SharedHelper;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
import static org.firstinspires.ftc.teamcode.SharedHelper.DRIVE_LEFT_NAME;
import static org.firstinspires.ftc.teamcode.SharedHelper.DRIVE_RIGHT_NAME;
import static org.firstinspires.ftc.teamcode.SharedHelper.Direction;
import static org.firstinspires.ftc.teamcode.SharedHelper.MOTOR_ESCALATOR;
import static org.firstinspires.ftc.teamcode.SharedHelper.MOTOR_HAND_LEVEL_1_LEFT;
import static org.firstinspires.ftc.teamcode.SharedHelper.MOTOR_HAND_LEVEL_1_RIGHT;
import static org.firstinspires.ftc.teamcode.SharedHelper.MOTOR_HAND_LEVEL_2;
import static org.firstinspires.ftc.teamcode.SharedHelper.SERVO_HANGING_HOOK;

/**
 * This class contains several methods to access hardware devices quickly.
 * Users can also directly access the original hardware objects.
 */
public final class TeleOpFacade implements DrivingSimpleFacade {
    private final DcMotor leftDrive;
    private final DcMotor rightDrive;
    private final DcMotor level1LeftMotor;
    private final DcMotor level1RightMotor;
    private final DcMotor level2Motor;
    private final DcMotor escalatorMotor;
    private final Servo hangingHookServo;

    TeleOpFacade(HardwareMap hardwareMap) {
        this.leftDrive = hardwareMap.get(DcMotor.class, DRIVE_LEFT_NAME);
        this.rightDrive = hardwareMap.get(DcMotor.class, DRIVE_RIGHT_NAME);
        this.level1LeftMotor = hardwareMap.get(DcMotor.class, MOTOR_HAND_LEVEL_1_LEFT);
        this.level1RightMotor = hardwareMap.get(DcMotor.class, MOTOR_HAND_LEVEL_1_RIGHT);
        this.level2Motor = hardwareMap.get(DcMotor.class, MOTOR_HAND_LEVEL_2);
        this.escalatorMotor = hardwareMap.get(DcMotor.class, MOTOR_ESCALATOR);
        level1LeftMotor.setZeroPowerBehavior(BRAKE);
        level1RightMotor.setZeroPowerBehavior(BRAKE);
        level2Motor.setZeroPowerBehavior(BRAKE);
        this.escalatorMotor.setZeroPowerBehavior(BRAKE);

        this.hangingHookServo = hardwareMap.get(Servo.class, SERVO_HANGING_HOOK);
    }

    @Deprecated
    public void driveTogether(SharedHelper.Direction direction,
                                     double power) {
        driveSeparated(direction, power, power);
    }

    @Override
    public void driveSeparated(SharedHelper.Direction direction,
                                      double leftPower, double rightPower) {
        DcMotorSimple.Direction leftDriveDirection;
        DcMotorSimple.Direction rightDriveDirection;

        switch (direction) {
            case Unknown:
            case Forward:
                leftDriveDirection = DcMotorSimple.Direction.FORWARD;
                rightDriveDirection = DcMotorSimple.Direction.REVERSE;
                break;
            case Backward:
                leftDriveDirection = DcMotorSimple.Direction.REVERSE;
                rightDriveDirection = DcMotorSimple.Direction.FORWARD;
                break;
            case TurnLeft:
                leftDriveDirection = DcMotorSimple.Direction.FORWARD;
                rightDriveDirection = DcMotorSimple.Direction.FORWARD;
                break;
            case TurnRight:
                leftDriveDirection = DcMotorSimple.Direction.REVERSE;
                rightDriveDirection = DcMotorSimple.Direction.REVERSE;
                break;
            default:
                throw new IllegalArgumentException("direction not acceptable");
        }

        this.leftDrive.setDirection(leftDriveDirection);
        this.rightDrive.setDirection(rightDriveDirection);

        this.leftDrive.setPower(leftPower);
        this.rightDrive.setPower(rightPower);
    }

    public void stopAllMotors() {
        this.stopAllDrivingMotors();
        this.stopAllArmMotors();
        this.moveEscalatorMotor(Direction.Forward, 0);
    }

    @Override
    public void stopAllDrivingMotors() {
        this.leftDrive.setPower(0);
        this.rightDrive.setPower(0);
    }

    public void stopAllArmMotors() {
        this.level1LeftMotor.setPower(0);
        this.level1RightMotor.setPower(0);
        this.level2Motor.setPower(0);
    }

    public void moveEscalatorMotor(Direction direction, double power) {
        switch (direction) {
            case Forward:
                this.escalatorMotor.setDirection(DcMotorSimple.Direction.FORWARD);
                break;
            case Backward:
                this.escalatorMotor.setDirection(DcMotorSimple.Direction.REVERSE);
                break;
            default:
                break;
        }

        this.escalatorMotor.setPower(power);
    }

    public void moveRoboticArmLevel1(Direction direction, double power) {
        DcMotorSimple.Direction leftArmDirection;
        DcMotorSimple.Direction rightArmDirection;

        switch (direction) {
            case Forward:
                leftArmDirection = DcMotorSimple.Direction.FORWARD;
                rightArmDirection = DcMotorSimple.Direction.REVERSE;
                break;
            case Backward:
                leftArmDirection = DcMotorSimple.Direction.REVERSE;
                rightArmDirection = DcMotorSimple.Direction.FORWARD;
                break;
            default:
                // Don't have this operation
                throw new IllegalArgumentException("direction not acceptable");
        }

        this.level1LeftMotor.setDirection(leftArmDirection);
        this.level1RightMotor.setDirection(rightArmDirection);

        this.level1LeftMotor.setPower(power);
        this.level1RightMotor.setPower(power);
    }

    public void moveRoboticArmLevel2(Direction direction, double power) {
        DcMotorSimple.Direction armDirection;

        switch (direction) {
            case Forward:
                armDirection = DcMotorSimple.Direction.FORWARD;
                break;
            case Backward:
                armDirection = DcMotorSimple.Direction.REVERSE;
                break;
            default:
                // Don't have this operation
                throw new IllegalArgumentException("direction not acceptable");
        }

        this.level2Motor.setDirection(armDirection);

        this.level2Motor.setPower(power);
    }

    public void moveHangingHook(boolean isOpen) {
        //TODO: Is 180 deg open? Or 0 deg open
        if (isOpen) {
            this.hangingHookServo.setPosition(180);
        } else {
            this.hangingHookServo.setPosition(0);
        }
    }

    public void closeDevice() {
        this.leftDrive.close();
        this.rightDrive.close();
        this.level1LeftMotor.close();
        this.level1RightMotor.close();
        this.level2Motor.close();

        this.hangingHookServo.close();
    }

    @Override
    public void close() {
        this.stopAllMotors();
        this.closeDevice();
    }
}
