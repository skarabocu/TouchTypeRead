//Sinan Karabocuoglu 
//10 July 2018
//TouchTypeRead

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

public class TouchTypeRead {
    //attitutes
    static JProgressBar accuracyBar = new JProgressBar();
    static FrameComponents components = new FrameComponents();
    static JLabel label = new JLabel("The Accuracy of the Answers: %" + components.accuracy);
    static JLabel label2 = new JLabel("Words per Minute: " + components.wordPerMin);
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("Touch-Type-Read");
        frame.setSize(650, 400);
        //closes on exit
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //sets the position
        frame.setLocationRelativeTo(null);
        //sets nonresizable
        frame.setResizable(false);
        JButton next = new JButton("Next Page");
        JButton previous = new JButton("Previous Page");
        //********ButtonListeners******
        class ButtonListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == next) {
                    components.nextPage();
                } else {
                    components.prevPage();
                }
            }
        }
        //********************************
        //*****buttons for navigating*******
        JPanel panel = new JPanel(new GridLayout(1, 2));
        ButtonListener listener = new ButtonListener();
        next.addActionListener(listener);
        previous.addActionListener(listener);
        panel.add(previous);
        panel.add(next);
        frame.add(panel, BorderLayout.SOUTH);
        JTabbedPane tabs = new JTabbedPane();
        //**********************************
        JPanel statsPanel = new JPanel(new GridLayout(3,1));
        statsPanel.add(label);
        accuracyBar.setValue((int)components.accuracy);
        statsPanel.add(accuracyBar);
        statsPanel.add(label2);
        //****panel for progress and statistics***
        tabs.addTab("Text",components);
        tabs.add("Statistics",statsPanel);
        frame.add(tabs, BorderLayout.CENTER);
        //sets the frame visible
        frame.setVisible(true);
    }
}