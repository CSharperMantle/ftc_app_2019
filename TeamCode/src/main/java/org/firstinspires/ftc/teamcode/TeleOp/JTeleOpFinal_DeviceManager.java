package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.io.Closeable;

public final class JTeleOpFinal_DeviceManager implements Closeable{

    public DcMotor frontLeftDrive;
    public DcMotor frontRightDrive;
    public DcMotor backLeftDrive;
    public DcMotor backRightDrive;

    public void init(HardwareMap hm) {
        frontLeftDrive = hm.get(DcMotor.class, "frontLeftDrive");
        frontRightDrive = hm.get(DcMotor.class, "frontRightDrive");
        backLeftDrive = hm.get(DcMotor.class, "backLeftDrive");
        backRightDrive = hm.get(DcMotor.class, "backRightDrive");
    }

    public void stopAllDrives() {
        frontRightDrive.setPower(0);
        frontLeftDrive.setPower(0);
        backRightDrive.setPower(0);
        backLeftDrive.setPower(0);
    }

    @Override
    public void close() {
        frontLeftDrive.close();
        frontRightDrive.close();
        backLeftDrive.close();
        backRightDrive.close();
    }
}
