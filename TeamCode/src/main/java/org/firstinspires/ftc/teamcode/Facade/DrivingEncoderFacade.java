package org.firstinspires.ftc.teamcode.Facade;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.SharedHelper;

public interface DrivingEncoderFacade extends Facade {
    void driveWithEncoder(double speed,
                                 double leftCMs, double rightCMs,
                                 double timeoutS,
                                 SharedHelper.Direction direction,
                                 LinearOpMode linearOpModeForRef);
}
