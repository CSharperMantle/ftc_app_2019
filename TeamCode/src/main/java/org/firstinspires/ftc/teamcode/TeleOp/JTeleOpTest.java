package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@SuppressWarnings("unused")
@TeleOp(name="JTeleOpTest", group="test")
public final class JTeleOpTest extends LinearOpMode {

    private JTeleOpFinal_DeviceManager _deviceManager;

    @Override
    public void runOpMode() {
        _deviceManager = new JTeleOpFinal_DeviceManager();
        _deviceManager.init(hardwareMap);

        while (true) {
            if (this.gamepad1.dpad_down) {
                _deviceManager.frontRightDrive.setPower(1);
            }
            if (this.gamepad1.dpad_up) {
                _deviceManager.frontLeftDrive.setPower(1);
            }
            if (this.gamepad1.dpad_left) {
                _deviceManager.backRightDrive.setPower(1);
            }
            if (this.gamepad1.dpad_right) {
                _deviceManager.backLeftDrive.setPower(1);
            }
            _deviceManager.stopAllDrives();

            if (isStopRequested()) break;
        }

        _deviceManager.close();
    }
}
