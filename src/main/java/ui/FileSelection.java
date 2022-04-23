package ui;

import data.DTO.Different;
import data.config.ConfigData;
import data.config.Message;
import fileOperation.FileComparator;
import lombok.extern.log4j.Log4j2;
import utils.Callback;
import utils.ConfigOperator;
import utils.MyThreadPoolExecutor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Callable;

import static utils.ConfigUtils.*;
import static utils.Text.COMPARE_TEXT;

@Log4j2
public class FileSelection extends JPanel implements ActionListener {

    private final JButton mOldDirButton;
    private final JButton mNewDirButton;
    private final JButton mCompareButton;
    private final JTextField mOldDirText;
    private final JTextField mNewDirText;
    private final MyThreadPoolExecutor mPool;
    private final ConfigOperator mConfigOperator;
    private final Callback mCallback;

    public FileSelection(Callback callback) {

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

        mPool = MyThreadPoolExecutor.getPool("FileSelection");
        mConfigOperator = new ConfigOperator();
        mCallback = callback;

        initComponent();
    }

    private void initComponent() {

        ConfigData data = null;
        try {
            data = mPool.submit(new Callable<ConfigData>() {
                @Override
                public ConfigData call() {
                    String name = Thread.currentThread().getName();
                    Thread.currentThread().setName(name + "-readConfig");
                    return mConfigOperator.readConfig();
                }
            }).get();
        } catch (Exception e) {

        }
        if (data != null) {
            mOldDirText.setText(data.getMOldPath());
            mNewDirText.setText(data.getMNewPath());
        }

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
        if (command.equals(COMMAND_SELECT_OLD)) {
            mOldDirText.setText(chooseDir());
        } else if (command.equals(COMMAND_SELECT_NEW)) {
            mNewDirText.setText(chooseDir());
        } else if (command.equals(COMMAND_COMPARE)) {
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
        mPool.submit(new Runnable() {
            @Override
            public void run() {
                String name = Thread.currentThread().getName();
                Thread.currentThread().setName(name + "-writeConfig");
                mConfigOperator.writeConfig(new ConfigData(oldDir, newDir));
            }
        });

        if (oldDir.isEmpty() || oldDir.isBlank()
                || newDir.isEmpty() || newDir.isBlank()) {
            return;
        }

        log.info("on poll submit");
        MessageDailog messageDailog = new MessageDailog();
        messageDailog.messageDailog(COMPARE_TEXT);

        try {
            var different = mPool.submit(new Callable<Different>() {
                @Override
                public Different call() throws Exception {
                    String name = Thread.currentThread().getName();
                    Thread.currentThread().setName(name + "-compare");
                    return new FileComparator(oldDir, newDir).compare();
                }
            }).get();
            log.warn(different);
            mCallback.callback(new Message(MessageType.DIFFERENT_ALREADY, different));
        } catch (Exception e) {
            e.printStackTrace();
        }

        messageDailog.dispose();
    }
}
