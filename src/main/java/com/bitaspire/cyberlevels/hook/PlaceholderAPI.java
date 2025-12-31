package com.bitaspire.cyberlevels.hook;

import com.bitaspire.cyberlevels.CyberLevels;
import com.bitaspire.cyberlevels.cache.Lang;
import com.bitaspire.cyberlevels.level.LevelSystem;
import com.bitaspire.cyberlevels.user.LevelUser;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

final class PlaceholderAPI implements Hook {

    private final PlaceholderExpansion expansion;

    PlaceholderAPI(CyberLevels main) {
        expansion = new PlaceholderExpansion() {

            @Override
            public @NotNull String getAuthor() {
                return main.getAuthors();
            }

            @Override
            public @NotNull String getIdentifier() {
                return "slv";
            }

            @Override
            public @NotNull String getVersion() {
                return main.getDescription().getVersion();
            }

            @Override
            public boolean persist() {
                return true;
            }

            private String getLeaderboard(OfflinePlayer player, String type, String positionStr) {
                if (!main.cache().config().isLeaderboardEnabled())
                    return "enable in config.yml";

                int position;
                try {
                    position = Integer.parseInt(positionStr);
                } catch (NumberFormatException e) {
                    return "invalid number";
                }

                if (position < 1 || position > 10)
                    return "out of bounds";

                LevelSystem<?> system = main.levelSystem();
                LevelUser<?> user = system.getLeaderboard().getTopPlayer(position);

                Lang.LeaderboardKeys keys = main.cache().lang().leaderboardKeys();
                String value = user == null ? keys.getLoadingName() : keys.getNoPlayerName();

                if (user != null) {
                    value = switch (type.toLowerCase()) {
                        case "name" -> user.getName();
                        case "displayname" -> user.getPlayer().getDisplayName();
                        case "level" -> user.getLevel() + "";
                        case "exp" -> system.formatNumber(user.getExp());
                        default -> value;
                    };
                }

                return main.core().textSettings()
                        .colorize(player instanceof Player ? (Player) player : null, value);
            }

            @Override
            public String onRequest(OfflinePlayer player, @NotNull String identifier) {
                if (!player.isOnline()) return null;

                LevelSystem<?> system = main.levelSystem();

                switch (identifier.toLowerCase()) {
//                    case "level_maximum": return system.getMaxLevel() + "";
                    case "exp_minimum": return system.getStartExp() + "";
                    case "level_minimum":  return system.getStartLevel() + "";
                }

                if (identifier.startsWith("leaderboard_")) {
                    String[] parts = identifier.split("_", 3);
                    if (parts.length == 3) {
                        String type = parts[1];
                        String pos  = parts[2];
                        return getLeaderboard(player, type, pos);
                    }
                }

                LevelUser<?> user = main.userManager().getUser((Player) player);
                if (user == null) return "0";

                return switch (identifier.toLowerCase()) {
                    case "player_level" -> String.valueOf(user.getLevel());
                    case "player_prestige" -> String.valueOf(user.getPrestige());
                    case "player_level_next" -> (Math.min(user.getLevel() + 1, system.getMaxLevel(user))) + "";
                    case "player_exp" -> system.formatNumber(user.getExp());
                    case "player_exp_required" -> system.formatNumber(user.getRequiredExp());
                    case "player_exp_remaining" -> system.formatNumber(user.getRemainingExp());
                    case "player_exp_progress_bar" -> main.core().textSettings().colorize(user.getProgressBar());
                    case "player_exp_percent" -> user.getPercent();
                    default -> null;
                };

            }
        };
    }

    @Override
    public void register() {
        expansion.register();
    }

    @Override
    public void unregister() {
        expansion.unregister();
    }
}
