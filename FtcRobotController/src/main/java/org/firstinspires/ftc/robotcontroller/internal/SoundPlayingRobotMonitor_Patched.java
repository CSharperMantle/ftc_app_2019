package org.firstinspires.ftc.robotcontroller.internal;

import android.support.annotation.Nullable;
import android.support.annotation.RawRes;

import com.qualcomm.ftcrobotcontroller.R;

import org.firstinspires.ftc.ftccommon.external.SoundPlayingRobotMonitor;
import org.firstinspires.ftc.robotcore.external.function.Consumer;

public final class SoundPlayingRobotMonitor_Patched extends SoundPlayingRobotMonitor {
    private int[] _availiableSound = {R.raw.nxtstartupsound, R.raw.errormessage, R.raw.warningmessage};

    @Override
    protected void playSound(Sound sound, @RawRes final int resourceId, @Nullable Consumer<Integer> runWhenStarted, @Nullable Runnable runWhenFinished) {
        //Ignored.
        boolean matched = false;
        for (int s: _availiableSound) {
            if (resourceId == s)
                matched = true;
        }
        if (matched)
            super.playSound(sound, resourceId, runWhenStarted, runWhenFinished);

    }
}
