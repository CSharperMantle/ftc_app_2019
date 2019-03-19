package org.firstinspires.ftc.teamcode.Facade;

import org.firstinspires.ftc.teamcode.SharedHelper;

public interface DrivingSimpleFacade extends Facade {
    void driveSeparated(SharedHelper.Direction direction,
                               double leftPower, double rightPower);
}
