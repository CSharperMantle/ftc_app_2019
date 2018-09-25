package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@Autonomous(name = "JTestVuMarkNavigation", group = "JTest")
public final class JTestVuMarkNavigation extends LinearOpMode {

    public static final String TAG = "JTestVuMarkNavigation";

    private final String vuforiaLicenseKey =JAutonomousFinal_Shared.vuforiaLicenseKey;

    private OpenGLMatrix lastLocation = null;
    private VuforiaLocalizer vuforia = null;

    private float mmPerInch = 25.4f;
    private float mmBotWidth = 18 * mmPerInch;
    private float mmFTCFieldWidth = (12*12 - 2) * mmPerInch;

    @Override
    public void runOpMode() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId",
                "id",
                hardwareMap.appContext.getPackageName());

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = vuforiaLicenseKey;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        VuforiaTrackables roverRuckusTrackables = vuforia.loadTrackablesFromAsset("RoverRuckus");
        VuforiaTrackables stonesAndChipsTrackables = vuforia.loadTrackablesFromAsset("StonesAndChips");

        VuforiaTrackable stone = stonesAndChipsTrackables.get(0);
        stone.setName("stone");

        VuforiaTrackable chips = stonesAndChipsTrackables.get(1);
        chips.setName("chips");

        List<VuforiaTrackable> allTrackables = new ArrayList<>();
        allTrackables.addAll(stonesAndChipsTrackables);

        telemetry.addData("Info", "Press START to start checking.");
        telemetry.update();
        waitForStart();

        stonesAndChipsTrackables.activate();
        roverRuckusTrackables.activate();

        while (opModeIsActive()) {
            for (VuforiaTrackable trackable : allTrackables) {

                telemetry.addData(trackable.getName(), ((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible() ? "Visible" : "Not Visible");    //

                OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener)trackable.getListener()).getUpdatedRobotLocation();
                if (robotLocationTransform != null) {
                    lastLocation = robotLocationTransform;
                }
            }
            /**
             * Provide feedback as to where the robot was last located (if we know).
             */
            if (lastLocation != null) {
                //  RobotLog.vv(TAG, "robot=%s", format(lastLocation));
                telemetry.addData("Status", format(lastLocation));
            } else {
                telemetry.addData("Status", "Unknown");
            }
            telemetry.update();
        }
    }

    private String format(OpenGLMatrix transformationMatrix) {
        return transformationMatrix.formatAsTransform();
    }
}
