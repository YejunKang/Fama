package me.yejun.famabot.formatter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.yejun.famabot.data.StockDataApiHandler;
import me.yejun.famabot.data.news.NewsData;
import me.yejun.famabot.data.quote.QuoteData;
import me.yejun.famabot.util.TimeUtil;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

import java.io.IOException;
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
                channel.sendMessage("> **Daily Stock Data for " + companyName + ":**\n" +
                        "Current Price: " + quoteData.c + "\n" +
                        "Change: " + quoteData.d + "\n" +
                        "Percent Change: " + quoteData.dp + "\n" +
                        "High Price: " + quoteData.h + "\n" +
                        "Low Price: " + quoteData.l + "\n" +
                        "Open Price: " + quoteData.o + "\n" +
                        "Previous Close: " + quoteData.pc + "\n" +
                        "---------------------------------\n").queue();
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
        System.out.println(articles.size());
        for (int i=0; i<2; i++) {
            stringBuilder.append("**Headline: ").append(articles.get(i).headline).append("**\n");
            stringBuilder.append("Summary: ").append(articles.get(i).summary).append("\n");
            stringBuilder.append("URL: ").append(articles.get(i).url).append("\n");
            stringBuilder.append("--------------------------------------------------+\n");
        }
        return stringBuilder;
    }
}
