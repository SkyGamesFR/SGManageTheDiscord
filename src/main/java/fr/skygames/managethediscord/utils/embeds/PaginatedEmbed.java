package fr.skygames.managethediscord.utils.embeds;

import fr.skygames.managethediscord.utils.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.*;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PaginatedEmbed {
    private final List<MessageEmbed> embeds;
    private final Consumer<MessageEmbed> messageModifier;
    private int currentPageIndex;

    public PaginatedEmbed(List<MessageEmbed> embeds) {
        this(embeds, null);
    }

    public PaginatedEmbed(List<MessageEmbed> embeds, Consumer<MessageEmbed> messageModifier) {
        this.embeds = embeds;
        this.messageModifier = messageModifier;
        this.currentPageIndex = 0;
    }

    public MessageEmbed getCurrentPage() {
        MessageEmbed currentPage = embeds.get(currentPageIndex);
        if (messageModifier != null) {
            EmbedBuilder modifiedBuilder = new EmbedBuilder(currentPage);
            messageModifier.accept(modifiedBuilder.build());
            currentPage = modifiedBuilder.build();
        }
        return currentPage;
    }

    public List<ItemComponent> getActionRow() {
        ButtonStyle style = ButtonStyle.SECONDARY;
        String previousButtonId = "previous";
        String nextButtonId = "next";

        boolean isFirstPage = currentPageIndex == 0;
        boolean isLastPage = currentPageIndex == embeds.size() - 1;

        Button previousButton = Button.of(style, previousButtonId, Emoji.fromUnicode("\u2B05")).withDisabled(isFirstPage);
        Button nextButton = Button.of(style, nextButtonId, Emoji.fromUnicode("\u27A1")).withDisabled(isLastPage);

       List<ItemComponent> components = new ArrayList<>();
        components.add(previousButton);
        components.add(nextButton);
        return components;
    }

    public void handleButtonClick(ComponentInteraction interaction) {
        String buttonId = interaction.getComponentId();
        if (buttonId.equals("previous")) {
            currentPageIndex--;
        } else if (buttonId.equals("next")) {
            currentPageIndex++;
        }
        currentPageIndex = IntStream.range(0, embeds.size())
                .boxed()
                .filter(i -> i >= 0 && i < embeds.size())
                .filter(i -> i != currentPageIndex)
                .min((i, j) -> Integer.compare(Math.abs(i - currentPageIndex), Math.abs(j - currentPageIndex)))
                .orElse(currentPageIndex);

        interaction.editComponents(ActionRow.of(getActionRow())).queue();
    }

    public EmbedBuilder createEmbed(String title, String description, String footer) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(title);
        builder.setColor(Color.GREEN);
        builder.setFooter(Constants.SERVER_NAME + " | " + footer);
        return builder;
    }

    public List<MessageEmbed> getEmbeds() {
        return embeds.stream()
                .map(embed -> {
                    if (messageModifier != null) {
                        EmbedBuilder modifiedBuilder = new EmbedBuilder(embed);
                        messageModifier.accept(modifiedBuilder.build());
                        return modifiedBuilder.build();
                    } else {
                        return embed;
                    }
                })
                .collect(Collectors.toList());
    }
}
