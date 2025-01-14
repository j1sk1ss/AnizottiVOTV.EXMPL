package org.cordell.anizotti.anizottiVOTV.managment;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.cordell.anizotti.anizottiVOTV.AnizottiVOTV;

import java.io.IOException;


public class DaysManager {
    public static int day = 0;
    private static int failedQuota = 0;

    public static void startDayTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                newDay();
            }
        }.runTaskTimer(AnizottiVOTV.getPlugin(AnizottiVOTV.class), 20L * 60 * 2, 20L * 60 * 20);
    }

    public static void newDay() {
        if (!QuotaManager.isCompleteQuota()) {
            failedQuota++;
            for (var player : Bukkit.getOnlinePlayers()) {
                if (TeamManager.isKittie(player)) continue;
                player.sendMessage("Quota not complete. Fine is 250$");
                try {
                    MoneyManager.removeMoney(Math.min(MoneyManager.getMoney(player), 250), player);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        day++;
        QuotaManager.stopQuota();
        QuotaManager.startQuota(day * 3);

        if (day >= 12) {
            for (var player : Bukkit.getOnlinePlayers()) {
                if (failedQuota < 5) player.sendMessage("Players win!");
                else player.sendMessage("Kitties win!");
            }
        }
    }
}
