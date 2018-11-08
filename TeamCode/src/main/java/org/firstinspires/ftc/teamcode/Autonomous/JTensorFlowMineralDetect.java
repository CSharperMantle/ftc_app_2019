package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@SuppressWarnings("unused")
@Autonomous(name="JTensorFlowMineralDetect", group="JTest")
public final class JTensorFlowMineralDetect extends LinearOpMode {
    @Override
    public void runOpMode() {
        VuforiaLocalizer vuforia;
        TFObjectDetector objectDetector;

        VuforiaLocalizer.Parameters param = new VuforiaLocalizer.Parameters();
        param.vuforiaLicenseKey = JAutonomousFinal_Shared.vuforiaLicenseKey;
        param.cameraDirection = JAutonomousFinal_Shared.cameraDirection;
        vuforia = ClassFactory.getInstance().createVuforia(param);

        if (!ClassFactory.getInstance().canCreateTFObjectDetector()) throw new RuntimeException("TFOD not supported");
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = JAutonomousFinal_Shared.MINIMUM_CONFIDENCE;
        objectDetector = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        objectDetector.loadModelFromAsset(JAutonomousFinal_Shared.TFOD_MODEL_ASSET,
                JAutonomousFinal_Shared.LABEL_GOLD_MINERAL,
                JAutonomousFinal_Shared.LABEL_SILVER_MINERAL);

        telemetry.addData(this.toString(), "Ready to start");
        telemetry.update();
        waitForStart();

        objectDetector.activate();
        List<Recognition> updatedRecognitions;
        while (true) {
            updatedRecognitions = objectDetector.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData(this.toString(), Integer.toString(updatedRecognitions.size()) + " Object(s) detected");
                telemetry.update();

                if (updatedRecognitions.size() == 3) {
                    int goldMineralX = -1;
                    int silverMineral1X = -1;
                    int silverMineral2X = -1;
                    for (Recognition recognition : updatedRecognitions) {
                        if (recognition.getLabel().equals(JAutonomousFinal_Shared.LABEL_GOLD_MINERAL)) {
                            goldMineralX = (int) recognition.getLeft();
                        } else if (silverMineral1X == -1) {
                            silverMineral1X = (int) recognition.getLeft();
                        } else {
                            silverMineral2X = (int) recognition.getLeft();
                        }
                    }
                    if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                        if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                            telemetry.addData("Gold Mineral Position", "Left");
                        } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                            telemetry.addData("Gold Mineral Position", "Right");
                        } else {
                            telemetry.addData("Gold Mineral Position", "Center");
                        }
                    }
                }
            }
            if (isStopRequested()) break;
        }
        objectDetector.shutdown();

    }
}
