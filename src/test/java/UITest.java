import ui.FileSelection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class UITest extends JPanel implements ActionListener {
    JButton mButton = new JButton("打开文件");

    public static void main(String[] args) {
        JFrame jf = new JFrame("Config Tool");
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.setSize(1600, 900);
        jf.setResizable(false);

        FileSelection panel = new FileSelection();
        Box box = Box.createVerticalBox();
        box.add(panel);

        jf.setContentPane(box);
        jf.setLocationRelativeTo(null);
        jf.setVisible(true);

    }

    public UITest(){
        JFrame frame = new JFrame();

        mButton.setActionCommand("open");
        mButton.setBackground(Color.GRAY);//设置按钮颜色
        frame.getContentPane().add(mButton, BorderLayout.SOUTH);//建立容器使用边界布局
        //
        mButton.addActionListener(this);
        frame.setTitle("标题");
        frame.setSize(1080, 720);

        Box box = Box.createHorizontalBox();
        box.add(this);
        frame.setContentPane(box);
        //显示窗口true
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e){
        if (e.getActionCommand().equals("open")){
            JFileChooser jf = new JFileChooser();
            jf.showOpenDialog(this);//显示打开的文件对话框
            File f =  jf.getSelectedFile();//使用文件类获取选择器选择的文件
            String s = f.getAbsolutePath();//返回路径名
            //JOptionPane弹出对话框类，显示绝对路径名
            JOptionPane.showMessageDialog(this, s, "标题",JOptionPane.WARNING_MESSAGE);
        }
    }
}
