package me.yejun.famabot.data.forms;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FormFilingsManager {
    public Map<Map<String, String>, String> fetch13FForms() {
        String[] urls = {
                "https://13f.info/manager/0001649339-scion-asset-management-llc",
                "https://13f.info/manager/0001037389-renaissance-technologies-llc",
                "https://13f.info/manager/0001067983-berkshire-hathaway-inc",
                "https://13f.info/manager/0001423053-citadel-advisors-llc"
        };

        Map<Map<String, String>, String> returnInfo = new HashMap<>();

        for (String url : urls) {
            try {
                Document doc = Jsoup.connect(url).get();
                String managerName = doc.selectFirst("h1").text();

                // Extract latest filing details
                Element latestFilingTable = doc.selectFirst("table");
                if (latestFilingTable != null) {
                    Elements rows = latestFilingTable.select("tr");
                    if (rows.size() > 1) {
                        Element latestFilingRow = rows.get(1);
                        Elements cols = latestFilingRow.select("td");

                        String period = cols.get(0).text();
                        String holdings = cols.get(3).text();

                        Map<String, String> firmInfo = new HashMap<>();
                        firmInfo.put("Manager", managerName);
                        firmInfo.put("Filing Date", period);
                        returnInfo.put(firmInfo, holdings);
                    }
                }

            } catch (IOException e) {
                System.err.println("Error fetching data from " + url);
                e.printStackTrace();
            }
        }
        return returnInfo;
    }


}




