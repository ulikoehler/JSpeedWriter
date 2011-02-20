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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.text.html.HTMLEditorKit;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

/**
 *
 * @author uli
 */
public class JSpeedWriterFrame extends javax.swing.JFrame {

    private static final File programDirectory = new File(new File(System.getProperty("user.home")),".jspeedwriter");
    private static final File configFile = new File(programDirectory, "config");
    private static final char SPACE = ' ';
    private List<String> templateWords = null;
    private List<String> coloredTemplateWords = null;
    private int currentWordIndex = 0;
    //Configuration options (values initialized in the constructor)
    private boolean caseSensitive = true;
    private boolean countWrongWords = true;
    private int timespan = 60;
    private int displayed_sentences = 5;
    //Timer
    private Timer countdownTimer = null;
    private int countdown = timespan;
    private boolean timerStarted = false;
    private boolean readyToStart = true;
    //Statistics
    private List<String> writtenWords = null;
    private int correctWords = 0;
    private int wrongWords = 0;
    private int writtenCharacters = 0;
    private boolean lastWasEmpty = false; //Prevents that multiple spaces are counted as written characters
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
        countdown = timespan;
    }

    /**
     * Starts the timer and initializes the statistics counter
     */
    private void startWriting() {
        newTextButton.setEnabled(false);
        templateComboBox.setEnabled(false);
        resetButton.setText("Stop");
        startStatistics();
        startTimer();
    }

    private void stopWriting() {
        stopTimer();
        readyToStart = false;
        showStatisticsDialog();
        newTextButton.setEnabled(true);
        templateComboBox.setEnabled(true);
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
        String msg = String.format("<html>%d Zeichen pro Minute - <span color=\"#00FF00\">%d</span> korrekte und <span color=\"#FF0000\">%d</span> falsche Wörter", writtenCharacters / (timespan / 60), correctWords, wrongWords);
        JOptionPane.showMessageDialog(this, msg, "Resultat", JOptionPane.INFORMATION_MESSAGE);
    }

    /** Creates new form JSpeedWriterFrame */
    public JSpeedWriterFrame() throws IOException, ConfigurationException {
        initComponents();
        //Init the config directory
        if (!programDirectory.exists()) {
            programDirectory.mkdir();
            configFile.createNewFile();
        }
        //Load the configuration
        ConfigurationFactory factory = new ConfigurationFactory("config.xml");
        Configuration config = factory.getConfiguration();
        timespan = config.getInt("options.timespan");
        displayed_sentences = config.getInt("options.displayed_sentences");
        caseSensitive = config.getBoolean("options.case_sensitive");
        countWrongWords = config.getBoolean("options.count_wrong_words");
        //Load the templates
        String[] templateDirs = config.getStringArray("templates.default");
        for(String templateDirName : templateDirs) {
            File templateDir = new File(templateDirName);
            for(File f : FileUtils.listFiles(templateDir, new SuffixFileFilter(".txt"), TrueFileFilter.INSTANCE)) {
                String name = templateDir.getAbsoluteFile().getParentFile().toURI().relativize(f.toURI()).getPath();
                templateComboBox.addItem(name);
            }
        }
        templateComboBox.setSelectedIndex(0);
        //Init other UI stuff
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
        templateComboBox = new javax.swing.JComboBox();
        newTextButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JSpeedWriter");

        countdownLabel.setFont(new java.awt.Font("Ubuntu", 0, 24));
        countdownLabel.setText("60");

        templateScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        templateScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        templateField.setEditorKit(new HTMLEditorKit());
        templateField.setFont(new java.awt.Font("Ubuntu", 0, 18));
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

        templateComboBox.setModel(new DefaultComboBoxModel());

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
                        .addComponent(templateComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(templateComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                if (countWrongWords) {
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
        List<String> textLines = IOUtils.readLines(new FileInputStream(templateComboBox.getSelectedItem().toString()));
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
        int maxSentenceIndex = minSentenceIndex + displayed_sentences;
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
                    } catch (ConfigurationException ex) {
                        Logger.getLogger(JSpeedWriterFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel countdownLabel;
    private javax.swing.JButton newTextButton;
    private javax.swing.JButton resetButton;
    private javax.swing.JComboBox templateComboBox;
    private javax.swing.JTextPane templateField;
    private javax.swing.JScrollPane templateScrollPane;
    private javax.swing.JTextField writerField;
    // End of variables declaration//GEN-END:variables
}
