package org.firstinspires.ftc.teamcode.Autonomous;

import android.media.AudioManager;
import android.media.SoundPool;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.R;

import java.util.List;

@Autonomous(name="JMineralDetectionSoundPlayer", group="Test")
@SuppressWarnings("unused")
public final class JMineralDetectionSoundPlayer extends LinearOpMode {

    @Override
    public void runOpMode() {
        VuforiaLocalizer vuforia;
        TFObjectDetector objectDetector;
        SoundPool soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

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

        int goldSoundId = soundPool.load(this.hardwareMap.appContext, R.raw.gold, 1);
        int silverSoundId = soundPool.load(this.hardwareMap.appContext, R.raw.silver, 1);

        while (opModeIsActive()) {
            updatedRecognitions = objectDetector.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                for (Recognition recognition : updatedRecognitions) {
                    if (recognition.getLabel().equals(JAutonomousFinal_Shared.LABEL_GOLD_MINERAL)) {
                        soundPool.play(goldSoundId, 0.8f, 0.8f, 1, -1, 1.0f);
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            System.exit(0);
                        }
                    } else if (recognition.getLabel().equals(JAutonomousFinal_Shared.LABEL_SILVER_MINERAL)) {
                        soundPool.play(silverSoundId, 0.8f, 0.8f, 1, -1, 1.0f);
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            System.exit(0);
                        }
                    }
                }
            }
            if (isStopRequested()) break;
        }

        objectDetector.shutdown();
    }
}
