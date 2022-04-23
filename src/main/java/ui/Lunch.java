package ui;

import data.DTO.Different;
import lombok.extern.log4j.Log4j2;
import utils.Callback;

import javax.swing.*;

@Log4j2
public class Lunch extends JFrame{

    private final Box mBox = Box.createVerticalBox();
    private final DifferentShower mDifferentShower = new DifferentShower();;
    private Callback mCallback = message -> {
        switch (message.getMType()) {
            case DIFFERENT_ALREADY:
                mDifferentShower.show((Different) message.getMObject());
                break;
        }
    };

    public Lunch() {

        log.info("lunch");

        this.setTitle("Config Tool");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(1600, 900);
        this.setResizable(false);

        FileSelection selection = new FileSelection(mCallback);
        selection.setSize(1600, 100);
        JScrollPane jScrollPane = new JScrollPane(mDifferentShower.getMBox());

        mBox.add(selection);
        mBox.add(jScrollPane);

        this.setContentPane(mBox);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new Lunch();
    }
}
