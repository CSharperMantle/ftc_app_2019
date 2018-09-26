package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@SuppressWarnings("unused")
@TeleOp(name="JTeleOpFinal_2019", group="Final")
public final class JTeleOpFinal extends LinearOpMode {
    @Override
    public void runOpMode() {
        final JTeleOpFinal_DeviceManager deviceManager = new JTeleOpFinal_DeviceManager(this.hardwareMap, this.telemetry);

        telemetry.addData(this.toString(), "Initialized");
        telemetry.update();

        while (true) {
            if (this.gamepad1.dpad_up)
                deviceManager.drive(JTeleOpFinal_DeviceManager.Direction.FORWARD);

            if (this.gamepad1.dpad_down)
                deviceManager.drive(JTeleOpFinal_DeviceManager.Direction.BACKWARD);

            if (this.gamepad1.dpad_left)
                deviceManager.drive(JTeleOpFinal_DeviceManager.Direction.LEFTSIDE);

            if (this.gamepad1.dpad_right)
                deviceManager.drive(JTeleOpFinal_DeviceManager.Direction.RIGHTSIDE);

            if (this.gamepad1.left_bumper)
                deviceManager.drive(JTeleOpFinal_DeviceManager.Direction.SPINLEFT);

            if (this.gamepad1.right_bumper)
                deviceManager.drive(JTeleOpFinal_DeviceManager.Direction.SPINRIGHT);

            if (this.gamepad1.left_trigger > 0.0)
                deviceManager.spinWithTrigger(JTeleOpFinal_DeviceManager.Direction.SPINLEFT, this.gamepad1.left_trigger);

            if (this.gamepad1.right_trigger > 0.0)
                deviceManager.spinWithTrigger(JTeleOpFinal_DeviceManager.Direction.SPINRIGHT, this.gamepad1.right_trigger);

            if (this.gamepad1.x)
                deviceManager.drive(JTeleOpFinal_DeviceManager.Direction.FRONTLEFT);

            if (this.gamepad1.y)
                deviceManager.drive(JTeleOpFinal_DeviceManager.Direction.FRONTRIGHT);

            if (this.gamepad1.b)
                deviceManager.drive(JTeleOpFinal_DeviceManager.Direction.BACKLEFT);

            if (this.gamepad1.a)
                deviceManager.drive(JTeleOpFinal_DeviceManager.Direction.BACKRIGHT);

            deviceManager.stopAllDrives();

            telemetry.update();

            if (this.isStopRequested()) {
                deviceManager.close();
                break;
            }
        }
    }
}
