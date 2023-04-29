package fr.skygames.managethediscord.utils.embeds;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.*;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PaginatedEmbed {
    private List<MessageEmbed> embeds;
    private Consumer<MessageEmbed> messageModifier;
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
            MessageEmbed modifiedBuilder = new MessageEmbed(currentPage);
            messageModifier.accept(modifiedBuilder);
            currentPage = modifiedBuilder.build();
        }
        return currentPage;
    }

    public List<Component> getActionRow() {
        ButtonStyle style = ButtonStyle.SECONDARY;
        String previousButtonId = "previous";
        String nextButtonId = "next";

        boolean isFirstPage = currentPageIndex == 0;
        boolean isLastPage = currentPageIndex == embeds.size() - 1;

        Button previousButton = Button.of(style, "◀️").withDisabled(isFirstPage).withId(previousButtonId);
        Button nextButton = Button.of(style, "▶️").withDisabled(isLastPage).withId(nextButtonId);

        return List.of(ActionRow.of(previousButton, nextButton));
    }

    public void handleButtonClick(ComponentInteraction interaction) {
        String buttonId = interaction.getComponentId();
        if (buttonId.equals("previous")) {
            currentPageIndex--;
        } else if (buttonId.equals("next")) {
            currentPageIndex++;
        }
        currentPageIndex = IntStream.range(0, embeds.size())
                .mapToObj(i -> i)
                .filter(i -> i >= 0 && i < embeds.size())
                .filter(i -> i != currentPageIndex)
                .min((i, j) -> Integer.compare(Math.abs(i - currentPageIndex), Math.abs(j - currentPageIndex)))
                .orElse(currentPageIndex);

        interaction.editOriginalComponents(getCurrentPage(), getActionRow()).queue();
    }

    public List<MessageEmbed> getEmbeds() {
        return embeds.stream()
                .map(embed -> {
                    if (messageModifier != null) {
                        EmbedBuilder modifiedBuilder = new EmbedBuilder(embed);
                        messageModifier.accept(modifiedBuilder);
                        return modifiedBuilder.build();
                    } else {
                        return embed;
                    }
                })
                .collect(Collectors.toList());
    }
}
