package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "2019 TeleOp Final")
public final class JTeleOpFinal_Main extends LinearOpMode {
    /**
     * Override this method and place your code here.
     * <p>
     * Please do not swallow the InterruptedException, as it is used in cases
     * where the op mode needs to be terminated early.
     *
     */
    @Override
    public void runOpMode() {
        JTeleOpFinal_Facade facade = new JTeleOpFinal_Facade(this.hardwareMap);

        this.waitForStart();

        while (this.opModeIsActive()) {
            JTeleOpFinal_Handler.gamepadsKeyHandler(facade, this.gamepad1, this.gamepad2);
        }

        facade.closeDevice();
    }
}
