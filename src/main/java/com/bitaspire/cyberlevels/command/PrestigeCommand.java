package com.bitaspire.cyberlevels.command;

import com.bitaspire.cyberlevels.CyberLevels;
import com.bitaspire.cyberlevels.cache.Lang;
import com.bitaspire.cyberlevels.level.LevelSystem;
import com.bitaspire.cyberlevels.user.LevelUser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PrestigeCommand implements CommandExecutor {
    private final CyberLevels main;

    public PrestigeCommand(CyberLevels main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        Player player = (sender instanceof Player) ? (Player) sender : null;

        if (player == null) {
            main.logger("&cConsole cannot use this command!");
            return true;
        }

        LevelUser<?> user = main.userManager().getUser(player);
        LevelSystem<?> system = main.levelSystem();

        if (user.getLevel() >= system.getMaxLevel(user)) {
            user.setLevel(1, false);
            user.setExp("0", false, false);

            user.setPrestige(user.getPrestige() + 1);
            main.cache().lang().sendMessage(player, Lang::getGainedPrestige,
                    new String[]{"player","level","prestige","maxLevel","playerEXP","requiredEXP","percent","progressBar"},
                    user.getName(),  user.getLevel(),  user.getPrestige(),  system.getMaxLevel(user),  system.formatNumber(user.getExp()),
                    system.formatNumber(user.getRequiredExp()),  user.getPercent(),  user.getProgressBar());
        } else {
            main.cache().lang().sendMessage(player, Lang::getNeedLevels,
                    new String[]{"player","level","prestige","maxLevel","playerEXP","requiredEXP","percent","progressBar", "needed"},
                    user.getName(),  user.getLevel(),  user.getPrestige(),  system.getMaxLevel(user),  system.formatNumber(user.getExp()),
                    system.formatNumber(user.getRequiredExp()),  user.getPercent(),  user.getProgressBar(), (system.getMaxLevel(user) - user.getLevel()) );
        }
        return true;
    }
}
