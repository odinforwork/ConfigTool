import ui.FileSelection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class UITest {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append("<p>this is a test. </p>");
            sb2.append("<p><font size=\"10\" color=\"red\">that </font>is a test. </p>");
        }


        JTextPane newsTextPane = new JTextPane();
        newsTextPane.setContentType("text/html");
        newsTextPane.setEditable(false);
        newsTextPane.setText(sb.toString());

        JEditorPane textPane = new JEditorPane();
        textPane.setContentType("text/html");
        textPane.setEditable(false);
        textPane.setText(sb2.toString());

        Box box = Box.createHorizontalBox();
        box.add(newsTextPane);
        box.add(textPane);

        JScrollPane scrollPane = new JScrollPane(box);
        scrollPane.setVerticalScrollBarPolicy(
                javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        Box box1 = Box.createVerticalBox();
        box1.add(new JButton("000"));
        box1.add(scrollPane);
        frame.add(box1);
        frame.setSize(300, 200);
        frame.setVisible(true);

    }
}
