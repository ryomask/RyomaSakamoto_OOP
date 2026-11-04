package com.ryoma.frontend.commands;

import com.ryoma.frontend.objects.Player;
import com.ryoma.frontend.systems.BulletManager;

public interface Command {
    void execute(Player player, BulletManager bulletManager);
}
