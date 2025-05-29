
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
import java.util.ArrayList;

public class Main extends JPanel implements ActionListener, MouseListener, KeyListener {

    int [][] board = new int [4][4];


    //need to create constructor
    public Main(){
        int a = 4;
    }

    public static void main(String[] args) {



        Scanner scan = new Scanner(System.in);
        System.out.println("please enter a number: ");
        int x = Integer.parseInt(scan.nextLine());
        int y = 90;



        System.out.println(x);
        System.out.println("hello");
        System.out.println(5);
        System.out.println(4);

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
}