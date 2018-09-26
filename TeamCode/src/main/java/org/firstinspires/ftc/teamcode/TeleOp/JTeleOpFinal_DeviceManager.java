package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.io.Closeable;

public final class JTeleOpFinal_DeviceManager implements Closeable {

    public DcMotor frontLeftDrive;
    public DcMotor frontRightDrive;
    public DcMotor backLeftDrive;
    public DcMotor backRightDrive;

    public JTeleOpFinal_DeviceManager(HardwareMap hm) {
        init(hm);
    }

    public void init(HardwareMap hm) {
        frontLeftDrive = hm.get(DcMotor.class, "frontLeftDrive");
        frontRightDrive = hm.get(DcMotor.class, "frontRightDrive");
        backLeftDrive = hm.get(DcMotor.class, "backLeftDrive");
        backRightDrive = hm.get(DcMotor.class, "backRightDrive");
        setAllZeroPowerBehaviorDrives(DcMotor.ZeroPowerBehavior.FLOAT);
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

    @Override
    public void close() {
        stopAllDrives();
        frontLeftDrive.close();
        frontRightDrive.close();
        backLeftDrive.close();
        backRightDrive.close();
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
}
