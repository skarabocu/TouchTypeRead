//Sinan Karabocuoglu 
//10 July 2018
//Frame Components

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class FrameComponents extends JComponent {
    //attitudes
    public String sentence;
    public String text;
    public BufferedReader reader;
    public ArrayList<String> lines;
    public int page;
    public int lineCount;//count of the lines
    public int letterOrder;//order of the letter
    public int sentenceOrder;//order of the sentence
    public int loopCount;//count for the loop that separates the parts
    public int leftMargin;
    public int topMargin;
    public double accuracy;//percentage of correctly pressed letters 
    public int totalTries;//the number of key presses
    public int correctTries;//the number of correct key presses
    public long startTime = 0;
    public long stopTime = 0;
    public double wordsCount = 0;
    public static double wordPerMin;
    public Graphics2D g2;
    public List<Double> wordPerMinList = new ArrayList<Double>();

    //constructer
    public FrameComponents() {
        // Initialize variables
        sentence = "";
        text = "";
        page = 0;
        lineCount = 0;
        letterOrder = 0;
        sentenceOrder = 0;
        leftMargin = 20;
        topMargin = 20;
        totalTries = 0;
        correctTries = 0;
        accuracy = 0;
        wordPerMin = 0;
        lines = new ArrayList<>();
        g2 = (Graphics2D) this.getGraphics();

        //analyzes the passage
        try {
            JFileChooser chooser = new JFileChooser();
            //choosing file from any directory(PUTTING .txt FILE TO DESKTOP BEFORE RUNNING TESTS WILL EASE THE PROCESS)
            chooser.setCurrentDirectory(new File(System.getProperty("user.home") +
                    "/Dropbox/School/L12/AP Programming/Output/production/AP Programming"));
            int result = chooser.showOpenDialog(this);

            if(result == JFileChooser.APPROVE_OPTION) {
                reader = new BufferedReader(new FileReader(chooser.getSelectedFile().getAbsolutePath()));
                //gets lines of the monologue
                while (true) {
                    sentence = reader.readLine();
                    if (!(sentence == null)) {
                        //sets each line a maximum of 50 characters so that a variety of articles can be read
                        for(int i = 0; i < sentence.length(); i += 50) {
                            if(i + 50 > sentence.length()-1){
                                lines.add(sentence.substring(i,sentence.length()));
                            }
                            else{
                                lines.add(sentence.substring(i,i + 50));
                            }
                            lineCount++;
                        }
                    } else {
                        break;
                    }
                }
            }else{
                System.exit(1);
            }
        } catch (IOException e) {
            System.out.println(e);
        }

        //*********key listener*********
        class KeyListener1 implements KeyListener {
            public void keyTyped(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(totalTries == 0){
                    //starts the time when the first letter is pressed
                    startTime = System.nanoTime();
                }
                //increment the totalTries
                totalTries++;
                String character = Character.toString(e.getKeyChar());
                if (character.equals(lines.get(sentenceOrder).substring(letterOrder, letterOrder + 1))) {
                  if(lines.get(sentenceOrder).substring(letterOrder, letterOrder + 1).equals(" ")){
                      //a space increases the word count
                      wordsCount++;
                      //stops the time to calculate the time passed for this word
                      stopTime = System.nanoTime();
                      //calculates the time
                      double time = (double)((((stopTime-startTime)/(double)(1000000000)))/(double)(60));
                      //updates the word per minute count
                      TouchTypeRead.label2.setText("Words per Minute: "+ (double)(wordsCount/time));
                      //gathers data for the graph
                      wordPerMinList.add((double)(wordsCount/time));
                      //always the most recent 20 points will be displayed
                      if(wordPerMinList.size() > 20){
                          wordPerMinList.remove(0);
                      }
                      //repaints the graph
                      TouchTypeRead.graph.repaint();
                  }
                    //increments the correctTries
                    correctTries++;
                    //goes to the next character
                    letterOrder++;
                    //adjusts the values when the line ends
                    if(letterOrder == lines.get(sentenceOrder).length())
                    {   //if it was the last sentence on the page, page changes
                        if(sentenceOrder == 4) {
                            page += 5;
                        }
                        //turns back to the beginning of the sentence
                        letterOrder = 0;
                        //goes to the next sentence IN THE FILE for checking the accuracy
                        sentenceOrder++;
                    }
                    //finds the accuracy
                    accuracy = ((double)correctTries /(double) totalTries)*100;
                    //display the accuracy
                    TouchTypeRead.accuracyBar.setValue((int)accuracy);
                    TouchTypeRead.label.setText("The Accuracy of the Answers: %" + accuracy);
                    repaint();
                }
            }
        }
        //******************************
        addKeyListener(new KeyListener1());
        setFocusable(true);
    }

    public void paintComponent(Graphics g) {
        g2 = (Graphics2D) g;

        int order = 0;
        //sets the parts of the monologue as 5 lines each
        while (true) {
            JTextArea label = new JTextArea();
            lineCount = lineCount - 5;
            if (lineCount >= 5) {
                loopCount = 5;
            } else if (5 > lineCount && lineCount > 0) {
                loopCount = lineCount;
            } else {
                break;
            }
            //creates the display text
            for (int a = loopCount; a > 0; a--) {
                text = text + "\n" + "        " + lines.get(order);
                order++;
            }
            //sets the text of the label
            label.setText(text);
            text = "";
        }

        //*******************

        Font textFont = new Font("Courier", Font.PLAIN, 16);
        g2.setFont(textFont);
        g2.setColor(Color.GREEN);
        // Integer that will determine the position of the marker rect
        int rectX = leftMargin;
        // For loop that will loop through the five lines
        for(int pageLine = 0; pageLine < 5; pageLine++){
            // For loop that will draw each letter. The integer 'i' denotes which letter the loop is on and the integer
            // 'xPos' keeps track of which x position the next letter will be drawn on
            for (int i = 0, xPos = leftMargin; i < lines.get(page + pageLine).length(); i++) {
                // Change to color to black if the marker is on a letter that hasn't been typed
                if(sentenceOrder % 5 == pageLine && i == letterOrder) g2.setColor(Color.BLACK);
                // Get the character
                String c = lines.get(page + pageLine).substring(i, i + 1);
                // Draw the character according to the xPos and pageLine
                g2.drawString(c, xPos, topMargin + 20 + 20 * pageLine);
                // Increase xPos by the width of the character
                xPos += g2.getFontMetrics().stringWidth(c) + 1;
                // If the loop is on the line that the marker should be on, calculate the x position of the marker
                if(sentenceOrder % 5 == pageLine && i < letterOrder) rectX += g2.getFontMetrics().stringWidth(c) + 1;
            }
        }

        g2.setColor(Color.MAGENTA);
        g2.drawRect(rectX - 1, topMargin + 4 + (sentenceOrder%5) * 20, g2.getFontMetrics().stringWidth(lines.get(sentenceOrder).substring(letterOrder, letterOrder+1)) + 2, 20);
    }

    public void prevPage(){
        if(page > 4) {
            page -= 5;
            sentenceOrder = 0;//adjust the size of the checking mechanism
            letterOrder = 0;//adjust the size of the checking mechanism
            repaint();//repaints the frame
            requestFocus();//keeps the focus on the panel
        }
    }

    public void nextPage(){
        page += 5;
        sentenceOrder = 0;//adjust the size of the checking mechanism
        letterOrder = 0;//adjust the size of the checking mechanism
        repaint();//repaints the frame
        requestFocus();//keeps the focus on the panel
    }
}