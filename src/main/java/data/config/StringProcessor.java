package data.config;

import lombok.Data;

public class StringProcessor {
    private final StringBuilder mBuilder;

    public StringProcessor(String s) {
        mBuilder = new StringBuilder(s);
    }

    public StringProcessor toParagraph() {
        mBuilder.insert(0, "<p>");
        mBuilder.append("</p>");
        return this;
    }

    public StringProcessor toTitle() {
        mBuilder.insert(0, "<font size=5>");
        mBuilder.append("</font>");
        return this;
    }

    public StringProcessor toDeleteWord() {
        mBuilder.insert(0, "<strong style= \"background:red\">");
        mBuilder.append("</strong>");
        return this;
    }

    public StringProcessor toAddWord() {
        mBuilder.insert(0, "<strong style= \"background:green\">");
        mBuilder.append("</strong>");
        return this;
    }

    public String toString() {
        return mBuilder.toString();
    }
}
