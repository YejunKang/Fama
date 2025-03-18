package me.yejun.famabot.formatter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.yejun.famabot.data.StockDataApiHandler;
import me.yejun.famabot.data.news.NewsData;
import me.yejun.famabot.data.quote.QuoteData;
import me.yejun.famabot.util.TimeUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

import java.awt.*;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class StockOverview {
    public TimeUtil timeUtil = new TimeUtil();





    public void createStockOverview(String companyName, String symbol, MessageChannel channel) throws IOException {
        String[] days = timeUtil.getDatesInCST();
        if (symbol != null) {
            channel.sendMessage("Fetching stock quote for " + companyName + "...").queue();
            QuoteData quoteData = StockDataApiHandler.fetchQuote(symbol);
            String newsData = StockDataApiHandler.fetchCompanyNews(symbol, days[1], days[0]);
            if (quoteData != null && newsData != null) {
                EmbedBuilder stockEmbed = new EmbedBuilder()
                        .setTitle("Fama - Daily quote for " + symbol)
                        .setColor(Color.GREEN)
                        .addField("Daily Stock Data for $" + symbol + ":", "", false)
                        .addField("Current Price:", String.format("%.2f", quoteData.c), true)
                        .addField("Change:", String.format("%.2f", quoteData.d), true)
                        .addField("Percent Change:", String.format("%.4f%%", quoteData.dp), true)
                        .addField("High Price:", String.format("%.2f", quoteData.h), true)
                        .addField("Low Price:", String.format("%.2f", quoteData.l), true)
                        .addField("Open Price:", String.format("%.2f", quoteData.o), true)
                        .addField("Previous Close:", String.format("%.2f", quoteData.pc), true)
                        .setFooter("Data as of March 17, 2025");
                channel.sendMessageEmbeds(stockEmbed.build()).queue();
                channel.sendMessage(parseAndPrintJson(newsData)).queue();
            } else {
                channel.sendMessage("Failed to fetch stock overview for " + companyName + ".").queue();
            }
        } else {
            channel.sendMessage("Company not found in S&P 500 list.").queue();
        }
    }

    private static StringBuilder parseAndPrintJson(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<NewsData> articles = objectMapper.readValue(json, new TypeReference<List<NewsData>>() {});
        StringBuilder stringBuilder = new StringBuilder();
        //System.out.println(articles.size());
        for (int i=0; i<2; i++) {
            stringBuilder.append("**Headline: ").append(articles.get(i).headline).append("**\n");
            stringBuilder.append("Summary: ").append(articles.get(i).summary).append("\n");
            stringBuilder.append("URL: ").append(articles.get(i).url).append("\n");
            stringBuilder.append("--------------------------------------------------+\n");
        }
        return stringBuilder;
    }

}