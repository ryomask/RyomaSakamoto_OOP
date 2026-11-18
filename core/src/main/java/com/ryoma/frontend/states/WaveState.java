package com.ryoma.frontend.states;

import com.ryoma.frontend.systems.LevelWaveManager;

public interface WaveState {
    void onEnter(LevelWaveManager manager);

    void update(LevelWaveManager manager, float delta);
}
