
import java.util.Scanner;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
import javax.swing.*;


public class Main extends JPanel implements ActionListener, MouseListener, KeyListener {
    //constructor
    public Main(){
        setPreferredSize (new Dimension (600, 600));
        setBackground (new Color (0, 255, 255));

    }

    public void paintComponent (Graphics g){
        super.paintComponent(g);
    }

    public void actionPerformed(ActionEvent event) {

    }

    // MouseListener methods
    public void mouseClicked(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {


    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    // KeyListener methods
    public void keyPressed(KeyEvent kp) {

    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Window");
        Main panel = new Main();
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);


    }
}