package ui;

import data.DTO.Different;
import data.config.StringProcessor;
import lombok.Getter;

import javax.swing.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static utils.ConfigUtils.*;

public class DifferentShower {
    @Getter
    private final Box mBox;
    @Getter
    private final JTextPane mOldShower;
    @Getter
    private final JTextPane mNewShower;

    private final StringBuilder mOldBuilder;
    private final StringBuilder mNewBuilder;

    public DifferentShower() {

        mOldBuilder = new StringBuilder();
        mNewBuilder = new StringBuilder();

        for (int i = 0; i < 100; i++) {
            mOldBuilder.append("<p><strong style= \"background:red\"><font size=\"5\">this </font></strong>is a test. </p>");
            mNewBuilder.append("<p><font size=\"5\" color=\"red\">that </font>is a test. </p>");
        }

        mOldShower = new JTextPane();
        mOldShower.setContentType("text/html");
        mOldShower.setEditable(false);

        mNewShower = new JTextPane();
        mNewShower.setContentType("text/html");
        mNewShower.setEditable(false);

        mBox = Box.createHorizontalBox();
        mBox.add(mOldShower);
        mBox.add(mNewShower);

        refresh();
    }

    private void refresh() {
        mOldShower.setText(mOldBuilder.toString());
        mNewShower.setText(mNewBuilder.toString());
    }

    private void addBlank(int witch) {
        if (witch % 2 == 1) {
            mOldBuilder.append("<p></p>");
        } else if ((witch >> 1) % 2 == 1) {
            mNewBuilder.append("<p></p>");
        }
    }

    public void show(Different different) {
        mOldBuilder.delete(0, mOldBuilder.length());
        mNewBuilder.delete(0, mNewBuilder.length());

        different.getMDeleteFiles()
                .forEach(s -> {
                    mOldBuilder.append(new StringProcessor(s).toDeleteWord().toTitle().toParagraph());
                    mNewBuilder.append(new StringProcessor("").toParagraph());
                });
        addBlank(ALL);

        different.getMNewFiles()
                .forEach(s -> {
                    mOldBuilder.append(new StringProcessor("").toParagraph());
                    mNewBuilder.append(new StringProcessor(s).toAddWord().toTitle().toParagraph());
                });
        addBlank(ALL);

        different.getMDifferentInFiles()
                .forEach(this::showFile);

        refresh();
    }

    private void showFile(Different.DifferentInFile different) {
        String name = new StringProcessor(different.getMFileName()).toTitle().toParagraph().toString();
        mOldBuilder.append(name);
        mNewBuilder.append(name);
        addBlank(ALL);

        different.getMDeleteBlocks()
                .forEach(blockInfo -> {
                    mOldBuilder.append(
                            new StringProcessor(blockInfo.getMBlockName()).toDeleteWord().toTitle().toParagraph());
                    addBlank(NEW);
                    blockInfo.getMInfos().forEach((t, s) -> {
                        mOldBuilder.append(
                                new StringProcessor(s).toDeleteWord().toParagraph());
                        addBlank(NEW);
                    });
                });
        addBlank(ALL);

        different.getMNewBlocks()
                .forEach(blockInfo -> {
                    mNewBuilder.append(
                            new StringProcessor(blockInfo.getMBlockName()).toDeleteWord().toTitle().toParagraph());
                    addBlank(OLD);
                    blockInfo.getMInfos().forEach((t, s) -> {
                        mNewBuilder.append(
                                new StringProcessor(s).toDeleteWord().toParagraph());
                        addBlank(OLD);
                    });
                });
        addBlank(ALL);

        different.getMDifferentInBlocks()
                .forEach(this::showBlock);
    }

    private void showBlock(Different.DifferentInBlock different) {
        String name = new StringProcessor(different.getMBlockName()).toParagraph().toString();
        mOldBuilder.append(name);
        mNewBuilder.append(name);

        var oBlock = different.getMOldBlockInfo().getMInfos();
        var nBlock = different.getMNewBlockInfo().getMInfos();
        var dils = different.getMDifferentInLines();

        Set<String> titles = dils.stream()
                .map(Different.DifferentInLine::getMTitle)
                .collect(Collectors.toSet());

        for (String title : titles) {
            String o = oBlock.getOrDefault(title, "");
            String n = nBlock.getOrDefault(title, "");

            List<Integer> dList = new ArrayList<>();
            List<Integer> iList = new ArrayList<>();
            dList.add(0);
            iList.add(0);
            //TODO !!!!!!!!!!!!!!!!!!!!! ==-1!!!!!!!!!!!!!!!!!!!!
            dils.stream()
                    .filter(d -> d.getMTitle().equals(title))
                    .forEach(d -> {
                        dList.add(d.getMDeleteStartAt());
                        dList.add(d.getMDeleteEndAt());
                        iList.add(d.getMInsertStartAt());
                        iList.add(d.getMInsertEndAt());
                    });
            int length = dList.size();
            String[] os = new String[length];
            String[] ns = new String[length];

            for (int i = 0; i < length; i++) {

            }
        }
    }

}