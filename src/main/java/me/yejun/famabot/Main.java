package me.yejun.famabot;

/*
    Written by Yejun Kang on 3/14/25
 */



import me.yejun.famabot.listeners.MessageListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Main {

    private static final String BOT_TOKEN = System.getenv("BOT_TOKEN");

    // Uses the Finnhub API
    public static final String API_TOKEN = System.getenv("API_TOKEN");
    public static final double VERSION = 1.0;

    public static void main(String[] args) throws InterruptedException {
        start();
    }

    public static void start() throws InterruptedException {
        JDA api = JDABuilder.createDefault(BOT_TOKEN, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(new MessageListener())
                .build();
        api.awaitReady();
    }
}