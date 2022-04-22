package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class MessageDailog extends JWindow implements ActionListener {
    private JLabel mLabel;

    public void messageDailog(String str) {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        Dimension frameSize = this.getSize();
        int x = (screenWidth - frameSize.width) / 2;
        int y = (screenHeight - frameSize.height) / 2;

        mLabel = new JLabel();
        mLabel.setText("<html><font style=font:20pt>" + str + "</font>");

        Container contentPane = getContentPane();
        contentPane.setLayout(null);
        contentPane.setSize(getMaximumSize());
        setSize((250 + (str.length() * 4)), 100);

        setLocation(x - ((250 + (str.length() * 4)) / 2), (y - 130 / 2));
        toFront();
        mLabel.setBounds(50, 25, (250 + (str.length() * 4)) - 10, 50);
        setVisible(true);
        contentPane.add(mLabel);

    }

    public void actionPerformed(ActionEvent e) {}

    public static void main(String[] args) {
        String warningString="comparing...";
        MessageDailog messageDailog=new MessageDailog();
        //时刻在屏幕的最前面显示弹框
//        messageDailog.setAlwaysOnTop(true);
        messageDailog.messageDailog(warningString);
        try{
            Thread.sleep(20 * 1000);
        } catch (Exception e) {

        }
        messageDailog.dispose();
    }

}
