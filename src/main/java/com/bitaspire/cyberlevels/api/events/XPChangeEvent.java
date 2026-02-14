package com.bitaspire.cyberlevels.api.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Getter
public class XPChangeEvent extends Event {
    @Getter
    private static final HandlerList handlerList = new HandlerList();
    private final Player player;
    private final double oldXP;
    private final double newXP;
    @Setter
    private double amount;

    public XPChangeEvent(@NotNull Player player, double oldXP, double newXP, double amount) {
        super(!Bukkit.isPrimaryThread());

        this.player = player;
        this.oldXP = oldXP;
        this.newXP = newXP;
        this.amount = amount;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}