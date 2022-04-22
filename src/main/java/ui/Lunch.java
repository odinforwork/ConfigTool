package ui;

import lombok.extern.log4j.Log4j2;

import javax.swing.*;
import java.awt.*;

@Log4j2
public class Lunch extends JFrame{

    public Lunch() {

        log.info("lunch");

        this.setTitle("Config Tool");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(1600, 900);
        this.setResizable(false);

        FileSelection panel = new FileSelection();
        Box box = Box.createVerticalBox();
        box.add(panel);

        this.setContentPane(box);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
