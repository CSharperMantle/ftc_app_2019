package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.FRONT;
import static org.firstinspires.ftc.teamcode.Autonomous.JAutonomousFinal_Shared.writeMessageRefresh;

@Autonomous(name="JAutonomousFinal_2019", group="Final")
@SuppressWarnings("unused")
public final class JAutonomousFinal_2019 extends LinearOpMode {

    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    private JAutonomousFinal_DeviceManager deviceManager = new JAutonomousFinal_DeviceManager();

    private boolean isTargetVisible = false;
    private OpenGLMatrix lastLocation = null;
    private JAutonomousFinal_Shared.NavigationTargetName targetName = null;
    private VuforiaTrackables targetsRoverRuckus = null;
    private List<VuforiaTrackable> allTrackables = null;

    private JAutonomousFinal_Shared.Position goldenMineralPosition = null;

    @Override
    public void runOpMode() {
        writeMessageRefresh(this.toString(), "Init...", this.telemetry);

        initHardware();
        initVuforia();
        initTensorFlow();

        writeMessageRefresh(this.toString(), "...Init", this.telemetry);
        this.waitForStart();
        //Start requested
        targetsRoverRuckus.activate();

        //Loop until stop requested
        while (this.opModeIsActive()) {
            isTargetVisible = false;
            for (VuforiaTrackable trackable : allTrackables) {
                if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()) {
                    String trackableName = trackable.getName();
                    telemetry.addData("Visible Target", trackableName);
                    isTargetVisible = true;
                    targetName = JAutonomousFinal_Shared.parseNavigationTargetName(trackableName);
                    OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener)trackable.getListener()).getUpdatedRobotLocation();
                    if (robotLocationTransform != null) {
                        lastLocation = robotLocationTransform;
                    }
                    break;
                }
            }

            if (isStopRequested()) break;

            if (isTargetVisible) {
                //TODO: Got target, go for it!
                while (true) {
                    //TODO: Don't forget to drive!
                    goldenMineralPosition = detectMineral();
                    if (goldenMineralPosition != null) break;
                }
                //TODO: Got position of gold mineral, push it away!

                switch (goldenMineralPosition) {
                    case LEFT:
                        //TODO: Goto LEFT
                        break;
                    case RIGHT:
                        //TODO: Goto RIGHT
                        break;
                    case CENTER:
                        //TODO: Goto CENTER
                        break;
                }
            }

        }

        cleanUp();
    }

    private void initVuforia() {
        /* initVuforia()... */
        int cameraMonitorViewId = hardwareMap.appContext
                .getResources()
                .getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = JAutonomousFinal_Shared.vuforiaLicenseKey;
        parameters.cameraDirection = JAutonomousFinal_Shared.cameraDirection;

        deviceManager.vuforia = ClassFactory.getInstance().createVuforia(parameters);

        targetsRoverRuckus = deviceManager.vuforia.loadTrackablesFromAsset(JAutonomousFinal_Shared.MODEL_ASSET_NAME);

        VuforiaTrackable blueRover = targetsRoverRuckus.get(0);
        blueRover.setName("Blue-Rover");
        VuforiaTrackable redFootprint = targetsRoverRuckus.get(1);
        redFootprint.setName("Red-Footprint");
        VuforiaTrackable frontCraters = targetsRoverRuckus.get(2);
        frontCraters.setName("Front-Craters");
        VuforiaTrackable backSpace = targetsRoverRuckus.get(3);
        backSpace.setName("Back-Space");

        OpenGLMatrix blueRoverLocationOnField = OpenGLMatrix
                .translation(0, JAutonomousFinal_Shared.mmFTCFieldWidth, JAutonomousFinal_Shared.mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0));
        blueRover.setLocation(blueRoverLocationOnField);

        OpenGLMatrix redFootprintLocationOnField = OpenGLMatrix
                .translation(0, -JAutonomousFinal_Shared.mmFTCFieldWidth, JAutonomousFinal_Shared.mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180));
        redFootprint.setLocation(redFootprintLocationOnField);

        OpenGLMatrix frontCratersLocationOnField = OpenGLMatrix
                .translation(-JAutonomousFinal_Shared.mmFTCFieldWidth, 0, JAutonomousFinal_Shared.mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0 , 90));
        frontCraters.setLocation(frontCratersLocationOnField);

        OpenGLMatrix backSpaceLocationOnField = OpenGLMatrix
                .translation(JAutonomousFinal_Shared.mmFTCFieldWidth, 0, JAutonomousFinal_Shared.mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90));
        backSpace.setLocation(backSpaceLocationOnField);

        OpenGLMatrix phoneLocationOnRobot = OpenGLMatrix
                .translation(JAutonomousFinal_Shared.cameraForwardDisplacement, JAutonomousFinal_Shared.cameraLeftDisplacement, JAutonomousFinal_Shared.cameraVerticalDisplacement)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES,
                        JAutonomousFinal_Shared.cameraDirection == FRONT ? 90 : -90, 0, 0));

        allTrackables = new ArrayList<>(targetsRoverRuckus);
        for (VuforiaTrackable trackable : allTrackables)
        {
            ((VuforiaTrackableDefaultListener)trackable.getListener()).setPhoneInformation(phoneLocationOnRobot, parameters.cameraDirection);
        }
        /* ...initVuforia() */
    }

    private void initHardware() {
        /* initHardware()... */
        deviceManager.init(this.hardwareMap);
        /* ...initHardware() */
    }

    private void initTensorFlow() {
        /* initTensorFlow()... */
        if (!ClassFactory.getInstance().canCreateTFObjectDetector()) { throw new RuntimeException("TensorFlow not supported"); }
        JAutonomousFinal_Shared.Position mineralPosition;

        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        deviceManager.objectDetector = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, deviceManager.vuforia);
        deviceManager.objectDetector.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
        /* ...initTensorFlow() */
    }

    private JAutonomousFinal_Shared.Position detectMineral() {
        List<Recognition> updatedRecognitions = deviceManager.objectDetector.getUpdatedRecognitions();
        if (updatedRecognitions != null) {
            if (updatedRecognitions.size() == 3) {
                int goldMineralX = -1;
                int silverMineral1X = -1;
                int silverMineral2X = -1;
                for (Recognition recognition : updatedRecognitions) {
                    if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                        goldMineralX = (int) recognition.getLeft();
                    } else if (silverMineral1X == -1) {
                        silverMineral1X = (int) recognition.getLeft();
                    } else {
                        silverMineral2X = (int) recognition.getLeft();
                    }
                }
                if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                    if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                        return JAutonomousFinal_Shared.Position.LEFT;
                    } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                        return JAutonomousFinal_Shared.Position.RIGHT;
                    } else {
                        return JAutonomousFinal_Shared.Position.CENTER;
                    }
                }
            }
        }
        return null;
    }

    private void cleanUp() {
        writeMessageRefresh(this.toString(), "Closing", this.telemetry);
        deviceManager.close();
        writeMessageRefresh(this.toString(), "Closed devices", this.telemetry);
    }

}
