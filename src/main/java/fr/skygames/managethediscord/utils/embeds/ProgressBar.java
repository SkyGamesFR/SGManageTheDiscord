package fr.skygames.managethediscord.utils.embeds;

public class ProgressBar {
    private static final int MAX_PROGRESS = 100;
    private static final String BAR_CHARACTER = "█";
    private static final int BAR_LENGTH = 20;

    public String update(long pingTime) {
        int progress = (int) Math.min(MAX_PROGRESS, pingTime);
        int numBars = (int) ((double) progress / MAX_PROGRESS * BAR_LENGTH);

        StringBuilder progressBar = new StringBuilder();
        progressBar.append("[");
        for (int i = 0; i < BAR_LENGTH; i++) {
            if (i < numBars) {
                progressBar.append(BAR_CHARACTER);
            } else {
                progressBar.append(" ");
            }
        }
        progressBar.append("]");
        return progressBar.toString();
    }

    public String updateMusic(long max, long playtime) {
        int progress = (int) Math.min(max, playtime);
        int numBars = (int) ((double) progress / playtime * BAR_LENGTH);

        StringBuilder progressBar = new StringBuilder();
        progressBar.append("[");
        for (int i = 0; i < 20; i++) {
            if (i < numBars) {
                progressBar.append(BAR_CHARACTER);
            } else if(i == 0) {
                progressBar.append(BAR_CHARACTER);
            }else {
                progressBar.append("ㅤ");
            }
        }
        progressBar.append("]");

        System.out.println(progressBar.toString());

        return progressBar.toString();
    }
}
