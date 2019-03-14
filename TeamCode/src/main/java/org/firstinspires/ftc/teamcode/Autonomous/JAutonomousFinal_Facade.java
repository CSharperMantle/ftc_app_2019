package org.firstinspires.ftc.teamcode.Autonomous;

import android.support.annotation.Nullable;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.JTeamCode_Shared;

import java.util.ArrayList;
import java.util.List;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.FRONT;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.CAMERA_DIRECTION;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.CAMERA_FORWARD_DISPLACEMENT;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.CAMERA_LEFT_DISPLACEMENT;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.CAMERA_VERTICAL_DISPLACEMENT;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.COUNTS_PER_CM;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.DRIVE_LEFT_NAME;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.DRIVE_RIGHT_NAME;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.LABEL_GOLD_MINERAL;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.LABEL_SILVER_MINERAL;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.MINIMUM_CONFIDENCE;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.MM_FTC_FIELD_WIDTH;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.MM_TARGET_HEIGHT;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.MOTOR_ESCALATOR;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.MOTOR_HAND_LEVEL_1_LEFT;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.MOTOR_HAND_LEVEL_1_RIGHT;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.MOTOR_HAND_LEVEL_2;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.NavigationTargetName.BackSpace;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.NavigationTargetName.BlueRover;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.NavigationTargetName.FrontCraters;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.NavigationTargetName.RedFootprint;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.TFOD_MODEL_ASSET;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.VUFORIA_LICENSE_KEY;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.millimeterToInch;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.navigationTargetNameToStr;

/**
 * This class contains several methods to access hardware devices quickly.
 * Users can also directly access the original hardware objects.
 * This class also contains some objects related to {@link TFObjectDetector} and
 * {@link VuforiaLocalizer}
 */
public final class JAutonomousFinal_Facade {
    // Hardware devices
    public final DcMotor leftDrive;
    public final DcMotor rightDrive;
    public final DcMotor level1LeftDrive;
    public final DcMotor level1RightDrive;
    public final DcMotor level2Drive;
    public final DcMotor escalatorDrive;

    // Software devices
    public final VuforiaLocalizer vuforia;
    public final TFObjectDetector mineralDetector;
    
    // Data structures
    public final List<VuforiaTrackable> allTrackables;
    public final VuforiaTrackables targetsRoverRuckus;

    // Target locations
    private boolean targetVisible = false;
    private OpenGLMatrix lastLocation = null;

    public final OpenGLMatrix blueRoverLocationOnField;
    public final OpenGLMatrix redFootprintLocationOnField;
    public final OpenGLMatrix frontCratersLocationOnField;
    public final OpenGLMatrix backSpaceLocationOnField;
    public final OpenGLMatrix phoneLocationOnRobot;

    // Helpers
    private final HardwareMap hardwareMapRef;

    /**
     * 'Init' the devices but not start them. The constructor just loads the devices
     * into the object, but not power, or engage them. To start all the devices to use,
     * call {@link JAutonomousFinal_Facade#engage()}
     * @param hardwareMap The hardware map to get devices from
     */
    public JAutonomousFinal_Facade(HardwareMap hardwareMap) {
        this.hardwareMapRef = hardwareMap;

        // Hardware (servos, drives, etc.) init phase...
        this.leftDrive = this.hardwareMapRef.get(DcMotor.class, DRIVE_LEFT_NAME);
        this.rightDrive = this.hardwareMapRef.get(DcMotor.class, DRIVE_RIGHT_NAME);
        this.level1LeftDrive = this.hardwareMapRef.get(DcMotor.class, MOTOR_HAND_LEVEL_1_LEFT);
        this.level1RightDrive = this.hardwareMapRef.get(DcMotor.class, MOTOR_HAND_LEVEL_1_RIGHT);
        this.level2Drive = this.hardwareMapRef.get(DcMotor.class, MOTOR_HAND_LEVEL_2);
        this.escalatorDrive = this.hardwareMapRef.get(DcMotor.class, MOTOR_ESCALATOR);
        // ...Hardware (servos, drives, etc.) init phase

        // Vuforia init phase...
        VuforiaLocalizer.Parameters vuParams = new VuforiaLocalizer.Parameters();
        vuParams.vuforiaLicenseKey = VUFORIA_LICENSE_KEY;
        vuParams.cameraDirection = CAMERA_DIRECTION;
        this.vuforia = ClassFactory.getInstance().createVuforia(vuParams);
        // ...Vuforia init phase

        // Targets setup phase...
        this.targetsRoverRuckus = this.vuforia.loadTrackablesFromAsset("RoverRuckus");
        VuforiaTrackable blueRover = this.targetsRoverRuckus.get(0);
        blueRover.setName(navigationTargetNameToStr(BlueRover));
        VuforiaTrackable redFootprint = this.targetsRoverRuckus.get(1);
        redFootprint.setName(navigationTargetNameToStr(RedFootprint));
        VuforiaTrackable frontCraters = this.targetsRoverRuckus.get(2);
        frontCraters.setName(navigationTargetNameToStr(FrontCraters));
        VuforiaTrackable backSpace = this.targetsRoverRuckus.get(3);
        backSpace.setName(navigationTargetNameToStr(BackSpace));
        // ...Targets setup phase

        this.allTrackables = new ArrayList<>();
        this.allTrackables.addAll(targetsRoverRuckus);
        
        // Targets locating phase...

        /*
         * To place the BlueRover target in the middle of the blue perimeter wall:
         * - First we rotate it 90 around the field's X axis to flip it upright.
         * - Then, we translate it along the Y axis to the blue perimeter wall.
         */
        this.blueRoverLocationOnField = OpenGLMatrix
                .translation(0, MM_FTC_FIELD_WIDTH, MM_TARGET_HEIGHT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES,
                        90, 0, 0));
        blueRover.setLocation(this.blueRoverLocationOnField);

        /*
         * To place the RedFootprint target in the middle of the red perimeter wall:
         * - First we rotate it 90 around the field's X axis to flip it upright.
         * - Second, we rotate it 180 around the field's Z axis so the image is flat against the red perimeter wall
         *   and facing inwards to the center of the field.
         * - Then, we translate it along the negative Y axis to the red perimeter wall.
         */
        this.redFootprintLocationOnField = OpenGLMatrix
                .translation(0, -MM_FTC_FIELD_WIDTH, MM_TARGET_HEIGHT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES,
                        90, 0, 180));
        redFootprint.setLocation(this.redFootprintLocationOnField);

        /*
         * To place the FrontCraters target in the middle of the front perimeter wall:
         * - First we rotate it 90 around the field's X axis to flip it upright.
         * - Second, we rotate it 90 around the field's Z axis so the image is flat against the front wall
         *   and facing inwards to the center of the field.
         * - Then, we translate it along the negative X axis to the front perimeter wall.
         */
        this.frontCratersLocationOnField = OpenGLMatrix
                .translation(-MM_FTC_FIELD_WIDTH, 0, MM_TARGET_HEIGHT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES,
                        90, 0, 90));
        frontCraters.setLocation(this.frontCratersLocationOnField);

        /*
         * To place the BackSpace target in the middle of the back perimeter wall:
         * - First we rotate it 90 around the field's X axis to flip it upright.
         * - Second, we rotate it -90 around the field's Z axis so the image is flat against the back wall
         *   and facing inwards to the center of the field.
         * - Then, we translate it along the X axis to the back perimeter wall.
         */
        this.backSpaceLocationOnField = OpenGLMatrix
                .translation(MM_FTC_FIELD_WIDTH, 0, MM_TARGET_HEIGHT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES,
                        90, 0, -90));
        backSpace.setLocation(this.backSpaceLocationOnField);

        /*
         * Create a transformation matrix describing where the phone is on the robot.
         *
         * The coordinate frame for the robot looks the same as the field.
         * The robot's "forward" direction is facing out along X axis, with the LEFT side facing out along the Y axis.
         * Z is UP on the robot.  This equates to a bearing angle of Zero degrees.
         *
         * The phone starts out lying flat, with the screen facing Up and with the physical top of the phone
         * pointing to the LEFT side of the Robot.  It's very important when you test this code that the top of the
         * camera is pointing to the left side of the  robot.  The rotation angles don't work if you flip the phone.
         *
         * If using the rear (High Res) camera:
         * We need to rotate the camera around it's long axis to bring the rear camera forward.
         * This requires a negative 90 degree rotation on the Y axis
         *
         * If using the Front (Low Res) camera
         * We need to rotate the camera around it's long axis to bring the FRONT camera forward.
         * This requires a Positive 90 degree rotation on the Y axis
         *
         * Next, translate the camera lens to where it is on the robot.
         * In this example, it is centered (left to right), but 110 mm forward of the middle of the robot, and 200 mm above ground level.
         */
        this.phoneLocationOnRobot = OpenGLMatrix
                .translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES,
                        CAMERA_DIRECTION == FRONT ? 90 : -90, 0, 0));

        for (VuforiaTrackable trackable : allTrackables)
        {
            ((VuforiaTrackableDefaultListener)trackable.getListener())
                    .setPhoneInformation(phoneLocationOnRobot, vuParams.cameraDirection);
        }
        // ...Targets locating phase

        // TFObjectDetector check phase...
        if (!ClassFactory.getInstance().canCreateTFObjectDetector()) {
            throw new RuntimeException("TFObjectDetector not supported");
        }
        // ...TFObjectDetector check phase

        // TFObjectDetector init phase...
        TFObjectDetector.Parameters tfParams = new TFObjectDetector.Parameters();
        tfParams.minimumConfidence = MINIMUM_CONFIDENCE;
        this.mineralDetector = ClassFactory.getInstance().createTFObjectDetector(tfParams,
                this.vuforia);
        this.mineralDetector.loadModelFromAsset(TFOD_MODEL_ASSET,
                LABEL_GOLD_MINERAL,
                LABEL_SILVER_MINERAL);
        // ...TFObjectDetector init phase

        // THE END!
    }

    /**
     * Start up all devices. Should be called after the game started,
     * or after the constructor
     */
    public void engage() {
        // Engaging hardware...
        this.leftDrive.setZeroPowerBehavior(BRAKE);
        this.rightDrive.setZeroPowerBehavior(BRAKE);
        this.level1LeftDrive.setZeroPowerBehavior(BRAKE);
        this.level1RightDrive.setZeroPowerBehavior(BRAKE);
        this.level2Drive.setZeroPowerBehavior(BRAKE);
        this.escalatorDrive.setZeroPowerBehavior(BRAKE);
        stopAllDrivingMotors();
        this.level1LeftDrive.setPower(0);
        this.level1RightDrive.setPower(0);
        this.level2Drive.setPower(0);
        this.escalatorDrive.setPower(0);
        // ...Engaging hardware

        // Engaging targets...
        targetsRoverRuckus.activate();
        // ...Engaging targets
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

        this.leftDrive.setPower(Math.abs(leftPower));
        this.rightDrive.setPower(Math.abs(rightPower));
    }

    /**
     * Stop all motors of driving system
     */
    public void stopAllDrivingMotors() {
        this.leftDrive.setPower(0);
        this.rightDrive.setPower(0);
    }

    /**
     * Drive the robot with encoders
     * @param speed An double from 0 to 1. Indicates speed.
     * @param leftCMs The inches which the left wheel should go.
     * @param rightCMs The inches which the right wheel should go.
     * @param timeoutS The timeout in seconds.
     * @param linearOpModeForRef LinearOpMode for reference.
     */
    public void driveWithEncoder(double speed,
                                double leftCMs, double rightCMs,
                                double timeoutS,
                                JTeamCode_Shared.Direction direction,
                                LinearOpMode linearOpModeForRef) {
        int newLeftTarget;
        int newRightTarget;
        ElapsedTime runtime = new ElapsedTime();

        this.leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Determine new target position, and pass to motor controller
        newLeftTarget = this.leftDrive.getCurrentPosition() + (int) (leftCMs * COUNTS_PER_CM);
        newRightTarget = this.rightDrive.getCurrentPosition() + (int) (rightCMs * COUNTS_PER_CM);
        this.leftDrive.setTargetPosition(newLeftTarget);
        this.rightDrive.setTargetPosition(newRightTarget);

        // Turn On RUN_TO_POSITION
        this.leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        runtime.reset();
        driveSeparated(direction, speed, speed);

        while (true) {

            if (!linearOpModeForRef.opModeIsActive() ||
                    (runtime.seconds() >= timeoutS) ||
                    !(this.leftDrive.isBusy() || this.rightDrive.isBusy())) {
                break;
            }
        }

        // Stop all motion;
        this.stopAllDrivingMotors();

        // Turn off RUN_TO_POSITION
        this.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /**
     * Refresh the robot's location using {@link VuforiaLocalizer}.
     * @return 'true' if it was successful, otherwise 'false'
     */
    public boolean refreshRobotLocation() {
        this.targetVisible = false;
        for (VuforiaTrackable trackable : allTrackables) {
            if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()) {
                this.targetVisible = true;

                // getUpdatedRobotLocation() will return null if no new information is available since
                // the last time that call was made, or if the trackable is not currently visible.
                OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener)trackable.getListener())
                        .getUpdatedRobotLocation();
                if (robotLocationTransform != null) {
                    this.lastLocation = robotLocationTransform;
                }
                return targetVisible;
            }
        }
        return targetVisible;
    }

    /**
     * Get an {@link RobotLocation} object which contains the latest position and orientation data
     * @return The {@link RobotLocation} object
     */
    @Nullable
    public RobotLocation getLatestRobotLocation() {
        if (this.targetVisible) {
            return new RobotLocation(this.lastLocation);
        }
        return null;
    }

    /**
     * Perform all the actions required to safely stop the activities and release resources in use
     */
    public void close() {
        this.leftDrive.close();
        this.rightDrive.close();

        this.level1LeftDrive.close();
        this.level1RightDrive.close();

        this.targetsRoverRuckus.deactivate();
        this.mineralDetector.deactivate();
        this.mineralDetector.shutdown();
    }

    public class RobotLocation {
        public final float X;
        public final float Y;
        public final float Z;
        public final float Roll;
        public final float Pitch;
        public final float Yaw;

        private RobotLocation(OpenGLMatrix lastLocation) {
            VectorF trans = lastLocation.getTranslation();
            this.X = (float)millimeterToInch(trans.get(0));
            this.Y = (float)millimeterToInch(trans.get(1));
            this.Z = (float)millimeterToInch(trans.get(2));

            Orientation orientation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);
            this.Roll = orientation.firstAngle;
            this.Pitch = orientation.secondAngle;
            this.Yaw = orientation.thirdAngle;
        }

        public int getDimensionNumber() {
            if (this.X > 0 && this.Y > 0) {
                return 1;
            } else if (this.X < 0 && this.Y > 0) {
                return 2;
            } else if (this.X < 0 && this.Y < 0) {
                return 3;
            } else { // this.X > 0 && this.Y < 0
                return 4;
            }
        }
    }
}
