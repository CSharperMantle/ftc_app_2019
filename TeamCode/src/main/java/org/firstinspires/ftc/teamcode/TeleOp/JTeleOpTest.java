package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@SuppressWarnings("unused")
@TeleOp(name="JTeleOpTest", group="Test")
public final class JTeleOpTest extends LinearOpMode {

    private JTeleOpFinal_DeviceManager _deviceManager;

    @Override
    public void runOpMode() {
        _deviceManager = new JTeleOpFinal_DeviceManager();
        _deviceManager.init(hardwareMap);

        while (true) {
            if (this.gamepad1.a) {
                _deviceManager.frontRightDrive.setDirection(DcMotorSimple.Direction.FORWARD);
                _deviceManager.frontRightDrive.setPower(1);
            }
            if (this.gamepad1.x) {
                _deviceManager.frontLeftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
                _deviceManager.frontLeftDrive.setPower(1);
            }
            if (this.gamepad1.b) {
                _deviceManager.backRightDrive.setDirection(DcMotorSimple.Direction.FORWARD);
                _deviceManager.backRightDrive.setPower(1);
            }
            if (this.gamepad1.y) {
                _deviceManager.backLeftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
                _deviceManager.backLeftDrive.setPower(1);
            }
            if (this.gamepad1.left_bumper) {
                _deviceManager.frontRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
                _deviceManager.frontRightDrive.setPower(1);
            }
            if (this.gamepad1.right_bumper) {
                _deviceManager.frontLeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
                _deviceManager.frontLeftDrive.setPower(1);
            }
            if (this.gamepad1.left_trigger > 0.5) {
                _deviceManager.backRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
                _deviceManager.backRightDrive.setPower(1);
            }
            if (this.gamepad1.right_trigger > 0.5) {
                _deviceManager.backLeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
                _deviceManager.backLeftDrive.setPower(1);
            }
            _deviceManager.stopAllDrives();

            if (isStopRequested()) break;
        }

        _deviceManager.close();
    }
}
