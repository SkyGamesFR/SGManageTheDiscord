package fr.skygames.managethediscord;

import fr.skygames.managethediscord.managers.Manager;
import fr.skygames.managethediscord.utils.embeds.ProgressBar;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;

import java.util.Scanner;

public class Main {

    private static final Manager manager = new Manager();


    public static void main(String[] args) {
        onEnable();
        onDisable();
    }

    static void onEnable() {

        ProgressBar progressBar = new ProgressBar();
        progressBar.updateMusic(195000, 100000);

        manager.initSQL();
        manager.initConfig();
        manager.init();
    }

    static void onDisable() {
        Scanner scanner = new Scanner(System.in);
        if (scanner.nextLine().equals("exit")) {


            manager.jda.getPresence().setStatus(OnlineStatus.OFFLINE);
            System.out.println("Bot offline");
            manager.jda.shutdown();
            System.exit(0);
        }
    }
}