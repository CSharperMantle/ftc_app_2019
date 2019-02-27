package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.JTeamCode_Shared;


public class JTeleOpFinal_Facade {
    public final DcMotor leftDrive;
    public final DcMotor rightDrive;

    public final Servo level1LeftServo;
    public final Servo level1RightServo;
    public final Servo level2LeftServo;
    public final Servo level2RightServo;
    public final Servo level3LeftServo;
    public final Servo level3RightServo;

    public JTeleOpFinal_Facade(HardwareMap hardwareMap) {
        this.leftDrive = hardwareMap.get(DcMotor.class, JTeamCode_Shared.LEFT_DRIVE_NAME);
        this.rightDrive = hardwareMap.get(DcMotor.class, JTeamCode_Shared.RIGHT_DRIVE_NAME);

        this.level1LeftServo = hardwareMap.get(Servo.class,
                JTeamCode_Shared.LEVEL_1_LEFT_SERVO_NAME);
        this.level1RightServo = hardwareMap.get(Servo.class,
                JTeamCode_Shared.LEVEL_1_RIGHT_SERVO_NAME);
        this.level2LeftServo = hardwareMap.get(Servo.class,
                JTeamCode_Shared.LEVEL_2_LEFT_SERVO_NAME);
        this.level2RightServo = hardwareMap.get(Servo.class,
                JTeamCode_Shared.LEVEL_2_RIGHT_SERVO_NAME);
        this.level3LeftServo = hardwareMap.get(Servo.class,
                JTeamCode_Shared.LEVEL_3_LEFT_SERVO_NAME);
        this.level3RightServo = hardwareMap.get(Servo.class,
                JTeamCode_Shared.LEVEL_3_RIGHT_SERVO_NAME);
    }

    @Deprecated
    public void driveTogether(JTeamCode_Shared.Direction direction,
                                     double power) {
        driveSeparated(direction, power, power);
    }

    public void driveSeparated(JTeamCode_Shared.Direction direction,
                                      double leftPower, double rightPower) {
        JTeamCode_Shared.Direction leftDriveDirection;
        JTeamCode_Shared.Direction rightDriveDirection;

        switch (direction) {
            case Forward:
                leftDriveDirection = JTeamCode_Shared.Direction.Forward;
                rightDriveDirection = JTeamCode_Shared.Direction.Backward;
                break;
            case Backward:
                leftDriveDirection = JTeamCode_Shared.Direction.Backward;
                rightDriveDirection = JTeamCode_Shared.Direction.Forward;
                break;
            case TurnLeft:
                leftDriveDirection = JTeamCode_Shared.Direction.Forward;
                rightDriveDirection = JTeamCode_Shared.Direction.Forward;
                break;
            case TurnRight:
                leftDriveDirection = JTeamCode_Shared.Direction.Backward;
                rightDriveDirection = JTeamCode_Shared.Direction.Backward;
                break;
            default:
                throw new IllegalArgumentException("direction not acceptable");
        }

        this.leftDrive.setDirection(leftDriveDirection == JTeamCode_Shared.Direction.Forward ?
                DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);
        this.rightDrive.setDirection(rightDriveDirection == JTeamCode_Shared.Direction.Forward ?
                DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);

        this.leftDrive.setPower(leftPower);
        this.rightDrive.setPower(rightPower);
    }

    public void stopAllDrives() {
        this.leftDrive.setPower(0);
        this.rightDrive.setPower(0);
    }

    public void closeDevice() {
        this.leftDrive.close();
        this.rightDrive.close();

        this.level1LeftServo.close();
        this.level1RightServo.close();
        this.level2LeftServo.close();
        this.level2RightServo.close();
        this.level3LeftServo.close();
        this.level3RightServo.close();
    }
}
