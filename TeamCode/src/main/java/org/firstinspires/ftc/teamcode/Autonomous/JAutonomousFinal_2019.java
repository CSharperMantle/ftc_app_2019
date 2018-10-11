package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

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
    @Override
    public void runOpMode() {
        writeMessageRefresh("JAutonomousFinal_2019", "Init", this.telemetry);
        DcMotor frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        DcMotor frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");
        DcMotor backLeftDrive = hardwareMap.get(DcMotor.class, "backLeftDrive");
        DcMotor backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = JAutonomousFinal_Shared.vuforiaLicenseKey ;
        parameters.cameraDirection = JAutonomousFinal_Shared.cameraDirection;

        VuforiaLocalizer vuforia = ClassFactory.getInstance().createVuforia(parameters);

        VuforiaTrackables targetsRoverRuckus = vuforia.loadTrackablesFromAsset("RoverRuckus");
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

        List<VuforiaTrackable> allTrackables = new ArrayList<>(targetsRoverRuckus);
        for (VuforiaTrackable trackable : allTrackables)
        {
            ((VuforiaTrackableDefaultListener)trackable.getListener()).setPhoneInformation(phoneLocationOnRobot, parameters.cameraDirection);
        }

        writeMessageRefresh("JAutonomousFinal_2019", "Init done.", this.telemetry);
        this.waitForStart();
        while (true) {

            if (this.isStopRequested()) break;
        }

        writeMessageRefresh("JAutonomousFinal_2019", "Closing", this.telemetry);
        frontLeftDrive.close();
        frontRightDrive.close();
        backLeftDrive.close();
        backRightDrive.close();
        writeMessageRefresh("JAutonomousFinal_2019", "Closed devices", this.telemetry);
    }
}
