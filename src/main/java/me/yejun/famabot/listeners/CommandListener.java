package me.yejun.famabot.listeners;

import me.yejun.famabot.data.forms.FormFilingsManager;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandListener extends ListenerAdapter {
    private static final List<String> COMPANIES = Arrays.asList(
            "RENAISSANCE", "CITADEL", "SCION ASSET MANAGEMENT", "BERKSHIRE"
    );
    private static final Map<String, String> COMPANIESMAP = new HashMap<>();

    private final FormFilingsManager formFilingsManager = new FormFilingsManager();

    static {
        COMPANIESMAP.put("RENAISSANCE", "RENAISSANCE TECHNOLOGIES LLC");
        COMPANIESMAP.put("CITADEL", "CITADEL ADVISORS LLC");
        COMPANIESMAP.put("BERKSHIRE", "Berkshire Hathaway Inc");
        COMPANIESMAP.put("SCION ASSET MANAGEMENT", "Scion Asset Management, LLC");
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("holdings")) {
            OptionMapping companyOption = event.getOption("company");
            if (companyOption == null) {
                event.reply("Please provide a company name!").setEphemeral(true).queue();
                return;
            }

            String company = companyOption.getAsString().toUpperCase();
            if (COMPANIESMAP.containsKey(company)) {

                Map<Map<String, String>, String> data = formFilingsManager.fetch13FForms();
                for (Map.Entry<Map<String, String>, String> entry : data.entrySet()) {
                    Map<String, String> firmInfo = entry.getKey();
                    String holdings = entry.getValue();

                    if (COMPANIESMAP.get(company).equals(firmInfo.get("Manager"))) {

                        event.reply("Manager: " + firmInfo.get("Manager") + "\n" +
                                "Filing Date: " + firmInfo.get("Filing Date") + "\n" +
                                "Holdings: " + holdings).queue();
                    }
                }

                String companyName = COMPANIESMAP.get(company);
                event.reply("**" + companyName + "** Holdings:\n[Assume Holdings Data Here]").queue();
            } else {
                event.reply("Invalid company! Choose from: RENAISSANCE, CITADEL, SCION ASSET MANAGEMENT, BERKSHIRE")
                        .setEphemeral(true).queue();
            }
        }
    }

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        if (event.getName().equals("holdings") && event.getFocusedOption().getName().equals("company")) {
            String input = event.getFocusedOption().getValue().toUpperCase();

            List<Command.Choice> suggestions = COMPANIES.stream()
                    .filter(name -> name.startsWith(input))
                    .map(name -> new Command.Choice(name, name))
                    .toList();

            event.replyChoices(suggestions).queue();
        }
    }
}
