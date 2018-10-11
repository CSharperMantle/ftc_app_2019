package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="JTeleOpTest", group="Test")
@SuppressWarnings("unused")
public class JTeleOpTest extends LinearOpMode {

    @Override
    public void runOpMode() {
        DcMotor frontLeftDrive;
        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        this.waitForStart();
        while (true) {
            frontLeftDrive.setPower(1);
            if (this.isStopRequested()) break;
        }
    }
}
