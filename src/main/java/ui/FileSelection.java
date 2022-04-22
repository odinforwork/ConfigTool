package ui;

import controller.MainController;
import data.DTO.Different;
import data.DTO.Message;
import fileOperation.FileComparator;
import lombok.extern.log4j.Log4j2;
import utils.ThreadPoolUtil;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import static utils.ConfigUtils.*;

@Log4j2
public class FileSelection extends JPanel implements ActionListener {

    private final JButton mOldDirButton;
    private final JButton mNewDirButton;
    private final JButton mCompareButton;
    private final JTextField mOldDirText;
    private final JTextField mNewDirText;

    public FileSelection() {

        log.info("FileSelection");

        mOldDirButton = new JButton(COMMAND_SELECT_OLD);
        mOldDirButton.setActionCommand(COMMAND_SELECT_OLD);
        mOldDirButton.addActionListener(this);

        mNewDirButton = new JButton(COMMAND_SELECT_NEW);
        mNewDirButton.setActionCommand(COMMAND_SELECT_NEW);
        mNewDirButton.addActionListener(this);

        mCompareButton = new JButton(COMMAND_COMPARE);
        mCompareButton.setActionCommand(COMMAND_COMPARE);
        mCompareButton.addActionListener(this);

        mOldDirText = new JTextField(50);
        mOldDirText.setEditable(false);
        mNewDirText = new JTextField(50);
        mNewDirText.setEditable(false);

        JPanel panel01 = new JPanel();
        panel01.add(mOldDirButton);
        panel01.add(mOldDirText);

        JPanel panel02 = new JPanel();
        panel02.add(mNewDirButton);
        panel02.add(mNewDirText);

        JPanel panel03 = new JPanel();
        panel03.add(mCompareButton);

        this.add(panel01);
        this.add(panel02);
        this.add(panel03);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        log.info(command);
        if (command.equals(COMMAND_SELECT_OLD)) {
            mOldDirText.setText(chooseDir());
        } else if (command.equals(COMMAND_SELECT_NEW)) {
            mNewDirText.setText(chooseDir());
        } else if (command.equals(COMMAND_COMPARE)) {
            log.info(COMMAND_COMPARE);
            onCompareCommand();
        } else {

        }
    }

    private String chooseDir() {
        JFileChooser jf = new JFileChooser();
        jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jf.showOpenDialog(this);
        return jf.getSelectedFile().getAbsolutePath();
    }

    private void onCompareCommand() {
        log.info("onCompareCommand");

        String oldDir = mOldDirText.getText();
        String newDir = mNewDirText.getText();
        if (oldDir.isEmpty() || oldDir.isBlank()
                || newDir.isEmpty() || newDir.isBlank()) {
            return;
        }

        log.info("on poll");
        var pool = ThreadPoolUtil.getInstance().getMPool();
        Future<Different> future = pool.submit(new Callable<Different>() {
            @Override
            public Different call() throws Exception {
                return new FileComparator(oldDir, newDir).compare();
            }
        });

        log.info("on dailog");
        String titleString = "comparing...";
        MessageDailog messageDailog = new MessageDailog();
        messageDailog.messageDailog(titleString);
        try {
            var different = future.get();
            log.warn(different);
        } catch (Exception e) {
            e.printStackTrace();
        }
        messageDailog.dispose();
    }
}
