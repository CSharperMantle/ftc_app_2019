package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.SharedHelper;
import org.firstinspires.ftc.teamcode.R;

import java.util.List;

@Disabled
@Autonomous(name="JMineralDetectionSoundPlayer", group="Test")
@SuppressWarnings("unused")
public final class JMineralDetectionSoundPlayer extends LinearOpMode {

    @Override
    public void runOpMode() {
        VuforiaLocalizer vuforia;
        TFObjectDetector objectDetector;

        VuforiaLocalizer.Parameters param = new VuforiaLocalizer.Parameters();
        param.vuforiaLicenseKey = SharedHelper.VUFORIA_LICENSE_KEY;
        param.cameraDirection = SharedHelper.CAMERA_DIRECTION;
        vuforia = ClassFactory.getInstance().createVuforia(param);

        if (!ClassFactory.getInstance().canCreateTFObjectDetector()) throw new RuntimeException("TFOD not supported");
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = SharedHelper.MINIMUM_CONFIDENCE;
        objectDetector = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        objectDetector.loadModelFromAsset(SharedHelper.TFOD_MODEL_ASSET,
                SharedHelper.LABEL_GOLD_MINERAL,
                SharedHelper.LABEL_SILVER_MINERAL);

        telemetry.addData(this.toString(), "Ready to start");
        telemetry.update();
        waitForStart();

        objectDetector.activate();
        List<Recognition> updatedRecognitions;

        while (opModeIsActive()) {
            updatedRecognitions = objectDetector.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                for (Recognition recognition : updatedRecognitions) {
                    if (recognition.getLabel().equals(SharedHelper.LABEL_GOLD_MINERAL)) {
                        SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, R.raw.gold);
                        while (recognition.getLabel().equals(SharedHelper.LABEL_GOLD_MINERAL)) { idle(); }
                    } else if (recognition.getLabel().equals(SharedHelper.LABEL_SILVER_MINERAL)) {
                        SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, R.raw.silver);
                        while (recognition.getLabel().equals(SharedHelper.LABEL_SILVER_MINERAL)) { idle(); }
                    }
                }
            }
            if (isStopRequested()) break;
        }

        objectDetector.shutdown();
    }
}
