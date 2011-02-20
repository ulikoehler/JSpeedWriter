/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JSpeedWriterFrame.java
 *
 * Created on 20.02.2011, 19:17:07
 */
package jspeedwriter;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.text.html.HTMLEditorKit;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author uli
 */
public class JSpeedWriterFrame extends javax.swing.JFrame {

    private static final char SPACE = ' ';
    private List<String> templateWords = null;
    private List<String> coloredTemplateWords = null;
    private int currentWordIndex = 0;
    //Timer
    private Timer countdownTimer = null;
    private int countdown = TIMESPAN;
    private boolean timerStarted = false;
    private boolean readyToStart = true;
    //Statistics
    private List<String> writtenWords = null;
    private int correctWords = 0;
    private int wrongWords = 0;
    private int writtenCharacters = 0;
    private boolean lastWasEmpty = false; //Prevents that multiple spaces are counted as written characters
    //Options
    private static final boolean caseSensitive = true;
    private static final boolean addCharsOnWrongWords = true;
    private static final int TIMESPAN = 60;
    private static final int NUM_DISPLAYED_SENTENCES = 3;
    //Sentence shift variables.
    private List<Integer> sentenceIndices = null;

    public int getCurrentSentenceId() {
        for (int i = 0; i < sentenceIndices.size(); i++) {
            if (currentWordIndex < sentenceIndices.get(i)) {
                return i - 1;
            }
        }
        return sentenceIndices.size();
    }

    private void startTimer() {
        timerStarted = true;
        countdownTimer = new Timer();
        countdownTimer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                if (countdown == 1) {
                    stopWriting();
                }
                countdown -= 1;
                countdownLabel.setText(Integer.toString(countdown));
            }
        }, 1000, 1000);
    }

    private void stopTimer() {
        countdownTimer.cancel();
        timerStarted = false;
        countdown = TIMESPAN;
    }

    /**
     * Starts the timer and initializes the statistics counter
     */
    private void startWriting() {
        newTextButton.setEnabled(false);
        resetButton.setText("Stop");
        startStatistics();
        startTimer();
    }

    private void stopWriting() {
        stopTimer();
        readyToStart = false;
        showStatisticsDialog();
        newTextButton.setEnabled(false);
        resetButton.setText("Neustarten");
    }

    /**
     * Resets the statistics variables
     */
    private void startStatistics() {
        writtenWords = Lists.newLinkedList();
        correctWords = 0;
        wrongWords = 0;
        writtenCharacters = 0;
    }

    private void showStatisticsDialog() {
        String msg = String.format("<html>%d Zeichen pro Minute - <span color=\"#00FF00\">%d</span> korrekte und <span color=\"#FF0000\">%d</span> falsche Wörter", writtenCharacters / (TIMESPAN / 60), correctWords, wrongWords);
        JOptionPane.showMessageDialog(this, msg, "Resultat", JOptionPane.INFORMATION_MESSAGE);
    }

    /** Creates new form JSpeedWriterFrame */
    public JSpeedWriterFrame() throws IOException {
        initComponents();
        loadTemplate();
        writerField.requestFocus();
        updateTemplateField();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        countdownLabel = new javax.swing.JLabel();
        templateScrollPane = new javax.swing.JScrollPane();
        templateField = new javax.swing.JTextPane();
        writerField = new javax.swing.JTextField();
        resetButton = new javax.swing.JButton();
        topicComboBox = new javax.swing.JComboBox();
        newTextButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JSpeedWriter");

        countdownLabel.setFont(new java.awt.Font("Ubuntu", 0, 24)); // NOI18N
        countdownLabel.setText("60");

        templateScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        templateScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        templateField.setEditorKit(new HTMLEditorKit());
        templateField.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        templateScrollPane.setViewportView(templateField);

        writerField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                writerFieldKeyTyped(evt);
            }
        });

        resetButton.setText("Neustart");
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        topicComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Politik", "Roman" }));

        newTextButton.setText("Neuer Text");
        newTextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newTextButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(templateScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 857, Short.MAX_VALUE)
                    .addComponent(writerField, javax.swing.GroupLayout.DEFAULT_SIZE, 857, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(countdownLabel)
                        .addGap(30, 30, 30)
                        .addComponent(resetButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(topicComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(newTextButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(countdownLabel)
                    .addComponent(resetButton)
                    .addComponent(topicComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newTextButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(templateScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(writerField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void writerFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_writerFieldKeyTyped
        if (!timerStarted && readyToStart) {
            startWriting();
        }
        if (evt.getKeyChar() == SPACE) {
            String writtenWord = writerField.getText().trim();
            if (writtenWord.isEmpty()) {
                if (!lastWasEmpty) {
                    writtenCharacters += 1;
                }
                lastWasEmpty = true;
            } else {
                lastWasEmpty = false;
            }
            String correctWord = templateWords.get(currentWordIndex);
            if (writtenWord.equals(correctWord) || (!caseSensitive && writtenWord.toLowerCase().equals(correctWord.toLowerCase()))) {
                setGreen(currentWordIndex);
                correctWords++;
                writtenCharacters += writtenWord.length() + 1;
            } else {
                setRed(currentWordIndex);
                wrongWords++;
                if (addCharsOnWrongWords) {
                    writtenCharacters += writtenWord.length() + 1;
                }
            }
            //Prepare the UI for the new word
            currentWordIndex++;
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    writerField.setText("");
                }
            });
            updateTemplateField();
        }
    }//GEN-LAST:event_writerFieldKeyTyped

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        coloredTemplateWords = Lists.newArrayList(templateWords);
        readyToStart = true;
        writerField.requestFocus();
    }//GEN-LAST:event_resetButtonActionPerformed

    private void newTextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newTextButtonActionPerformed
        try {
            loadTemplate();
            updateTemplateField();
        } catch (IOException ex) {
            Logger.getLogger(JSpeedWriterFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_newTextButtonActionPerformed

    private void loadTemplate() throws IOException {
        List<String> textLines = IOUtils.readLines(JSpeedWriterFrame.class.getResourceAsStream("/templates/de/" + topicComboBox.getSelectedItem().toString().toLowerCase() + "/1.txt"));
        String text = Joiner.on("").join(textLines);
        templateWords = Lists.newArrayList(Splitter.on(SPACE).omitEmptyStrings().split(text));
        coloredTemplateWords = Lists.newArrayList(templateWords);
        //Find the sentece indices
        sentenceIndices = Lists.newArrayListWithCapacity(100);
        sentenceIndices.add(0);
        for (int i = 0; i < templateWords.size(); i++) {
            String word = templateWords.get(i);
            if (word.contains(".") || word.contains(":") || word.contains(";")) {
                sentenceIndices.add(i + 1);
            }
        }
    }

    private void setGreen(int n) {
        coloredTemplateWords.set(n, "<span color=\"#00FF00\">" + templateWords.get(n) + "</span>");
    }

    private void setRed(int n) {
        coloredTemplateWords.set(n, "<span color=\"#FF0000\">" + templateWords.get(n) + "</span>");
    }

    private void setGrayBackground(int n) {
        coloredTemplateWords.set(n, "<span bgcolor=\"#BBBBBB\">" + templateWords.get(n) + "</span>");
    }

    /**
     * Adds a gray background to the current word and updates the template area content
     */
    private void updateTemplateField() {
        setGrayBackground(currentWordIndex);
        int minSentenceIndex = getCurrentSentenceId();
        int maxSentenceIndex = minSentenceIndex + NUM_DISPLAYED_SENTENCES;
        templateField.setText("<html>" + Joiner.on(SPACE).join(coloredTemplateWords.subList(sentenceIndices.get(minSentenceIndex), sentenceIndices.get(maxSentenceIndex))));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    new JSpeedWriterFrame().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(JSpeedWriterFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel countdownLabel;
    private javax.swing.JButton newTextButton;
    private javax.swing.JButton resetButton;
    private javax.swing.JTextPane templateField;
    private javax.swing.JScrollPane templateScrollPane;
    private javax.swing.JComboBox topicComboBox;
    private javax.swing.JTextField writerField;
    // End of variables declaration//GEN-END:variables
}