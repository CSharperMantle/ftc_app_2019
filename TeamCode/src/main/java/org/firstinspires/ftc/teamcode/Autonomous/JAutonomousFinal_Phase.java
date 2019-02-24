package org.firstinspires.ftc.teamcode.Autonomous;

public abstract class JAutonomousFinal_Phase {
    public static boolean lowerToGround() {
        //TODO: Finish this phase

        return false;
    }

    public static JAutonomousFinal_Shared.Position detectSamplingMineral(boolean isLastPhaseSuccessful) {
        //TODO: Finish this phase

        if (!isLastPhaseSuccessful)
            return JAutonomousFinal_Shared.Position.Unknown;

        return JAutonomousFinal_Shared.Position.Unknown;
    }

    public static boolean pushSamplingMineral(JAutonomousFinal_Shared.Position mineralPosition) {
        //TODO: Finish this phase

        if (mineralPosition == JAutonomousFinal_Shared.Position.Unknown)
            return false;

        return false;
    }

    public static boolean placeTeamMarker(boolean isLastPhaseSuccessful) {
        //TODO: Finish this phase

        if (!isLastPhaseSuccessful)
            return false;

        return false;
    }

    public static boolean parkInCrater(boolean isLastPhaseSuccessful) {
        //TODO: Finish this phase

        if (!isLastPhaseSuccessful)
            return false;

        return false;
    }

    public static boolean playMusicOfVictory(boolean isLastPhaseSuccessful) {
        //TODO: Finish this phase

        if (!isLastPhaseSuccessful)
            return false;

        return false;
    }
}
