package com.randomappsinc.simpleflashcards.quiz.constants;

import androidx.annotation.IntDef;

@IntDef({
        TimerState.NEEDS_ACTIVATION,
        TimerState.RUNNING,
        TimerState.PAUSED,
        TimerState.FINISHED,
})
public @interface TimerState {
    int NEEDS_ACTIVATION = 0;
    int RUNNING = 1;
    int PAUSED = 2;
    int FINISHED = 3;
}
