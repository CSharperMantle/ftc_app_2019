package org.firstinspires.ftc.robotcontroller.internal;

import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.util.Log;

import com.qualcomm.ftcrobotcontroller.R;

import org.firstinspires.ftc.ftccommon.external.SoundPlayingRobotMonitor;
import org.firstinspires.ftc.robotcore.external.function.Consumer;

public final class SoundPlayingRobotMonitor_Patched extends SoundPlayingRobotMonitor {
    private int[] _availableSound = {R.raw.nxtstartupsound, R.raw.errormessage, R.raw.warningmessage};

    @Override
    protected void playSound(Sound sound, @RawRes final int resourceId, @Nullable Consumer<Integer> runWhenStarted, @Nullable Runnable runWhenFinished) {
        boolean soundMatched = false;
        for (int s: _availableSound) {
            if (resourceId == s)
                soundMatched = true;
        }
        if (soundMatched)
            super.playSound(sound, resourceId, runWhenStarted, runWhenFinished);
        else
            Log.w("SoundPlaying_User", "Sound " + resourceId + " is not avaliable and may cause bugs.");
    }
}
