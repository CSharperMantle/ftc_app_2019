package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.io.Closeable;

public final class JAutonomousFinal_DeviceManager implements Closeable {

    public DcMotor frontLeftDrive = null;
    public DcMotor frontRightDrive = null;
    public DcMotor backLeftDrive = null;
    public DcMotor backRightDrive = null;
    public DcMotor escalatorDrive = null;
    public TFObjectDetector objectDetector = null;
    public VuforiaLocalizer vuforia = null;
    public VoltageSensor hubVoltageSensorOne = null;

    public void init(HardwareMap hm) {
        init(hm, null);
    }

    public void init(HardwareMap hm, Telemetry telem) {
        frontLeftDrive = hm.get(DcMotor.class, "frontLeftDrive");
        frontRightDrive = hm.get(DcMotor.class, "frontRightDrive");
        backLeftDrive = hm.get(DcMotor.class, "backLeftDrive");
        backRightDrive = hm.get(DcMotor.class, "backRightDrive");
        escalatorDrive = hm.get(DcMotor.class, "escalatorDrive");
        hubVoltageSensorOne = hm.get(VoltageSensor.class, "drivingEngineHub");

        setAllZeroPowerBehaviorDrives(DcMotor.ZeroPowerBehavior.FLOAT);
        if (telem != null) {
            telem.addData(this.toString(), "Init finished.");
            if (hubVoltageSensorOne.getVoltage() < 10.0)
                telem.addData(this.toString(), "Voltage too low, is " + hubVoltageSensorOne.getVoltage());
            telem.update();
        }
    }

    public void stopAllDrives() {
        frontRightDrive.setPower(0);
        frontLeftDrive.setPower(0);
        backRightDrive.setPower(0);
        backLeftDrive.setPower(0);
    }

    public void setAllPowerDrives(float power) {
        frontRightDrive.setPower(power);
        frontLeftDrive.setPower(power);
        backRightDrive.setPower(power);
        backLeftDrive.setPower(power);
    }

    public void setAllZeroPowerBehaviorDrives(DcMotor.ZeroPowerBehavior behavior) {
        frontRightDrive.setZeroPowerBehavior(behavior);
        frontLeftDrive.setZeroPowerBehavior(behavior);
        backRightDrive.setZeroPowerBehavior(behavior);
        backLeftDrive.setZeroPowerBehavior(behavior);
    }

    public void spinWithTrigger(Direction direction, float power) {
        switch (direction) {
            case SPINLEFT:
                frontRightDrive.setDirection(DcMotorSimple.Direction.FORWARD);
                frontLeftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
                backRightDrive.setDirection(DcMotorSimple.Direction.FORWARD);
                backLeftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
                setAllPowerDrives(power);
                break;
            case SPINRIGHT:
                frontRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
                frontLeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
                backRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
                backLeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
                setAllPowerDrives(power);
                break;
            default:
                throw new IllegalArgumentException(direction.name());
        }
    }

    public void drive(Direction direction) {
        switch (direction) {
            case FORWARD:
                frontRightDrive.setDirection(DcMotorSimple.Direction.FORWARD);
                frontLeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
                backRightDrive.setDirection(DcMotorSimple.Direction.FORWARD);
                backLeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
                setAllPowerDrives(1);
                break;
            case BACKWARD:
                frontRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
                frontLeftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
                backRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
                backLeftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
                setAllPowerDrives(1);
                break;
            case LEFTSIDE:
                frontRightDrive.setDirection(DcMotorSimple.Direction.FORWARD);
                frontLeftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
                backRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
                backLeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
                setAllPowerDrives(1);
                break;
            case RIGHTSIDE:
                frontRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
                frontLeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
                backRightDrive.setDirection(DcMotorSimple.Direction.FORWARD);
                backLeftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
                setAllPowerDrives(1);
                break;
            case SPINLEFT:
                frontRightDrive.setDirection(DcMotorSimple.Direction.FORWARD);
                frontLeftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
                backRightDrive.setDirection(DcMotorSimple.Direction.FORWARD);
                backLeftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
                setAllPowerDrives(1);
                break;
            case SPINRIGHT:
                frontRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
                frontLeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
                backRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
                backLeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
                setAllPowerDrives(1);
                break;
            case FRONTLEFT:
                frontRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
                backLeftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
                frontRightDrive.setPower(1);
                backLeftDrive.setPower(1);
                break;
            case FRONTRIGHT:
                frontLeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
                backRightDrive.setDirection(DcMotorSimple.Direction.FORWARD);
                frontLeftDrive.setPower(1);
                backRightDrive.setPower(1);
                break;
            case BACKLEFT:
                frontRightDrive.setDirection(DcMotorSimple.Direction.FORWARD);
                backLeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
                frontRightDrive.setPower(1);
                backLeftDrive.setPower(1);
                break;
            case BACKRIGHT:
                frontLeftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
                backRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
                frontLeftDrive.setPower(1);
                backRightDrive.setPower(1);
                break;
            case STOP:
                stopAllDrives();
                break;
            default:
                throw new IllegalArgumentException(direction.name());
        }
    }

    public void setAllDriveMode(DcMotor.RunMode runMode) {
        this.backLeftDrive.setMode(runMode);
        this.backRightDrive.setMode(runMode);
        this.frontLeftDrive.setMode(runMode);
        this.frontRightDrive.setMode(runMode);
    }

    public void setAllDriveTargetPosition(int targetPosition) {
        this.backLeftDrive.setTargetPosition(this.backLeftDrive.getCurrentPosition() + targetPosition);
        this.backRightDrive.setTargetPosition(this.backRightDrive.getCurrentPosition() + targetPosition);
        this.frontLeftDrive.setTargetPosition(this.frontLeftDrive.getCurrentPosition() + targetPosition);
        this.frontRightDrive.setTargetPosition(this.frontRightDrive.getCurrentPosition() + targetPosition);
    }
    @Override
    public void close() {
        stopAllDrives();
        frontLeftDrive.close();
        frontRightDrive.close();
        backLeftDrive.close();
        backRightDrive.close();
        escalatorDrive.close();
        if (objectDetector != null) objectDetector.shutdown();
    }

    public enum Direction {
        FORWARD,
        BACKWARD,
        LEFTSIDE,
        RIGHTSIDE,
        SPINLEFT,
        SPINRIGHT,
        FRONTLEFT,
        FRONTRIGHT,
        BACKLEFT,
        BACKRIGHT,
        STOP
    }

    private static final double COUNTS_PER_MOTOR_REV = 757;    // eg: TETRIX Motor Encoder
    private static final double DRIVE_GEAR_REDUCTION = 1.0;     // This is < 1.0 if geared UP
    private static final double WHEEL_DIAMETER_INCHES = 3.0;     // For figuring circumference
    public static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    public static final double COUNTS_PER_CM = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            ((WHEEL_DIAMETER_INCHES * 3.1415) / 2.54);
    private static final double DRIVE_SPEED = 0.6;
    private static final double TURN_SPEED = 0.5;
}
