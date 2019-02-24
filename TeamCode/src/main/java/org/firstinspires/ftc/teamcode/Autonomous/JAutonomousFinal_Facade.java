package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

public class JAutonomousFinal_Facade {
    public final DcMotor leftDrive;
    public final DcMotor rightDrive;

    public final Servo level1LeftServo;
    public final Servo level1RightServo;
    public final Servo level2LeftServo;
    public final Servo level2RightServo;
    public final Servo level3LeftServo;
    public final Servo level3RightServo;

    public final VuforiaLocalizer vuforia;
    public final TFObjectDetector mineralDetector;

    public JAutonomousFinal_Facade(HardwareMap hardwareMap) {
        // Hardware (inc. servos, drives) init phase
        this.leftDrive = hardwareMap.get(DcMotor.class, JAutonomousFinal_Shared.LEFT_DRIVE_NAME);
        this.rightDrive = hardwareMap.get(DcMotor.class, JAutonomousFinal_Shared.RIGHT_DRIVE_NAME);

        this.level1LeftServo = hardwareMap.get(Servo.class,
                JAutonomousFinal_Shared.LEVEL_1_LEFT_SERVO_NAME);
        this.level1RightServo = hardwareMap.get(Servo.class,
                JAutonomousFinal_Shared.LEVEL_1_RIGHT_SERVO_NAME);
        this.level2LeftServo = hardwareMap.get(Servo.class,
                JAutonomousFinal_Shared.LEVEL_2_LEFT_SERVO_NAME);
        this.level2RightServo = hardwareMap.get(Servo.class,
                JAutonomousFinal_Shared.LEVEL_2_RIGHT_SERVO_NAME);
        this.level3LeftServo = hardwareMap.get(Servo.class,
                JAutonomousFinal_Shared.LEVEL_3_LEFT_SERVO_NAME);
        this.level3RightServo = hardwareMap.get(Servo.class,
                JAutonomousFinal_Shared.LEVEL_3_RIGHT_SERVO_NAME);

        // Vuforia init phase
        VuforiaLocalizer.Parameters vuParams = new VuforiaLocalizer.Parameters();
        vuParams.vuforiaLicenseKey = JAutonomousFinal_Shared.VUFORIA_LICENSE_KEY;
        vuParams.cameraDirection = JAutonomousFinal_Shared.CAMERA_DIRECTION;
        this.vuforia = ClassFactory.getInstance().createVuforia(vuParams);

        // TFObjectDetector check phase
        if (!ClassFactory.getInstance().canCreateTFObjectDetector()) {
            throw new RuntimeException("TFObjectDetector not supported");
        }

        // TFObjectDetector init phase
        TFObjectDetector.Parameters tfParams = new TFObjectDetector.Parameters();
        tfParams.minimumConfidence = JAutonomousFinal_Shared.MINIMUM_CONFIDENCE;
        this.mineralDetector = ClassFactory.getInstance().createTFObjectDetector(tfParams,
                this.vuforia);
        this.mineralDetector.loadModelFromAsset(JAutonomousFinal_Shared.TFOD_MODEL_ASSET,
                JAutonomousFinal_Shared.LABEL_GOLD_MINERAL,
                JAutonomousFinal_Shared.LABEL_SILVER_MINERAL);
    }

    public void close() {
        this.leftDrive.close();
        this.rightDrive.close();

        this.level1LeftServo.close();
        this.level1RightServo.close();
        this.level2LeftServo.close();
        this.level2RightServo.close();
        this.level3LeftServo.close();
        this.level3RightServo.close();

        this.mineralDetector.deactivate();
        this.mineralDetector.shutdown();
    }
}
