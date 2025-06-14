

import java.awt.image.BufferedImage;
import java.util.Scanner;
import java.awt.*;
import javax.sound.sampled.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.imageio.ImageIO;










public class Main extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener {
    Image setting_icon;
    BufferedImage chongqing;
    BufferedImage newYork;
    BufferedImage seoul;
    static Clip music;
    Clip mergeEffect;




    //Buttons
    Rectangle playButton = new Rectangle(200, 290, 200, 50);
    Rectangle tutorialButton = new Rectangle(200, 350, 200, 50);
    Rectangle creditsButton = new Rectangle(200, 410, 200, 50);
    Rectangle exitButton = new Rectangle(200, 470, 200, 50);
    Rectangle settingsButton = new Rectangle(3, 560, 40, 40);


    Rectangle backButton = new Rectangle(5, 5, 65, 30);
    Rectangle tutorialNext = new Rectangle(530, 5, 65, 30);
    Rectangle tutorialHome = new Rectangle(250, 540, 100, 40);


    Rectangle musicOn = new Rectangle(300, 165, 80, 50);
    Rectangle SFXOn = new Rectangle(300, 295, 80, 50);


    Rectangle backgroundButton = new Rectangle(140,410,300,80);


    Rectangle chongqingButton = new Rectangle(30, 220, 160, 200);
    Rectangle newYorkButton = new Rectangle(220, 220, 160, 200);
    Rectangle seoulButton = new Rectangle(410, 220, 160, 200);
    Rectangle resetButton = new Rectangle (260, 515, 80, 40);


    int musicButton = 1;
    int SFXButton = 1;
    int backgroundChange = 1;




    static JFrame game;


    //1: Home, 2: Game, 3-11: Tutorial, 12: Credits, 13: Settings, 14: Custom Backgrounds
    int pageSwitch = 1;




    //constructor
    public Main() throws IOException{
        setPreferredSize(new Dimension(600, 600));
        setBackground(new Color(222, 207, 189));


        setting_icon = Toolkit.getDefaultToolkit().getImage("Setting_Icon.png");
        chongqing = ImageIO.read(new File("chongqingBackground.png"));
        newYork = ImageIO.read(new File("newYorkBackground.png"));
        seoul = ImageIO.read(new File("seoulBackground.png"));


        addMouseListener(this); //detects button clicks
        addMouseMotionListener(this);
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();


    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        drawnPages(g2D);
    }


    public void drawnPages(Graphics2D g2D) {
        System.out.println("drawnpages " + pageSwitch);
        System.out.println("music button " + musicButton);
        System.out.println("SFX button " + SFXButton);




        if (pageSwitch == 1) {
            setBackground(new Color(222, 207, 189));
            //Title
            g2D.setColor(new Color(236, 89, 89));
            g2D.fillRoundRect(167, 110, 270, 110, 80, 80);
            g2D.setColor(Color.BLACK);
            g2D.setFont(new Font("Monospaced", Font.BOLD, 70));
            g2D.drawString("19683", 195, 187);
            g2D.setStroke(new BasicStroke(6));
            g2D.drawRoundRect(167, 110, 270, 110, 80, 80);


            //Play Button
            g2D.setStroke(new BasicStroke(4));
            g2D.setColor(Color.BLACK);
            g2D.drawRoundRect(playButton.x, playButton.y, playButton.width, playButton.height, 30, 30);


            g2D.setColor(new Color(238, 228, 218));
            g2D.fillRoundRect(playButton.x, playButton.y, playButton.width, playButton.height, 30, 30);


            //Tutorial Button
            g2D.setColor(Color.BLACK);
            g2D.drawRoundRect(tutorialButton.x, tutorialButton.y, tutorialButton.width, tutorialButton.height, 30, 30);


            g2D.setColor(new Color(237, 224, 200));
            g2D.fillRoundRect(tutorialButton.x, tutorialButton.y, tutorialButton.width, tutorialButton.height, 30, 30);


            //Credits Button
            g2D.setColor(Color.BLACK);
            g2D.drawRoundRect(creditsButton.x, creditsButton.y, creditsButton.width, creditsButton.height, 30, 30);


            g2D.setColor(new Color(227, 197, 148));
            g2D.fillRoundRect(creditsButton.x, creditsButton.y, creditsButton.width, creditsButton.height, 30, 30);


            //Exit Button
            g2D.setColor(Color.BLACK);
            g2D.drawRoundRect(exitButton.x, exitButton.y, exitButton.width, exitButton.height, 30, 30);


            g2D.setColor(new Color(236, 188, 109));
            g2D.fillRoundRect(exitButton.x, exitButton.y, exitButton.width, exitButton.height, 30, 30);


            //Play Button Text
            g2D.setColor(Color.BLACK);
            g2D.setFont(new Font("Monospaced", Font.BOLD, 24));
            g2D.drawString("PLAY", 273, 322);


            //Tutorial Button Text
            g2D.setColor(Color.BLACK);
            g2D.setFont(new Font("Monospaced", Font.BOLD, 24));
            g2D.drawString("TUTORIAL", 248, 382);


            //Credits Button Text
            g2D.setColor(Color.BLACK);
            g2D.setFont(new Font("Monospaced", Font.BOLD, 24));
            g2D.drawString("CREDITS", 253, 442);


            //Exit Button Text
            g2D.setColor(Color.BLACK);
            g2D.setFont(new Font("Monospaced", Font.BOLD, 24));
            g2D.drawString("EXIT", 273, 502);


            //Settings Icon
            g2D.drawImage(setting_icon, settingsButton.x, settingsButton.y, settingsButton.width, settingsButton.height, this);
        }
        else if (pageSwitch == 2){


            //changes the background of game page
            if (backgroundChange == 1) {
                setBackground(new Color(237, 225, 200));
            }
            else if (backgroundChange == 2){
                g2D.drawImage(chongqing, 0, 0, getWidth(), getHeight(), this); //sets image as background and scales it
            }
            else if (backgroundChange == 3){
                g2D.drawImage(newYork, 0, 0, getWidth(), getHeight(), this);
            }
            else if (backgroundChange == 4){
                g2D.drawImage(seoul, 0, 0, getWidth(), getHeight(), this);
            }


            //Back Button
            g2D.setFont(new Font("Plain", Font.BOLD, 18));
            g2D.setColor(new Color(136, 55, 23));
            g2D.fillRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(2));
            g2D.drawRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);


            g2D.setColor(new Color(239, 211, 204));
            g2D.drawString("BACK", 10, 27);


        }


        else if (pageSwitch == 3) {
            setBackground(new Color(237, 225, 200));


            Image tutorialPicOne = Toolkit.getDefaultToolkit().getImage("tutorialPicOne.png");
            g2D.drawImage(tutorialPicOne, 100, 100, 400, 400, this);


            g2D.setStroke(new BasicStroke(4));
            g2D.setColor(Color.BLACK);
            g2D.drawRoundRect(100, 50, 400, 50, 30, 30);
            g2D.drawRoundRect(35, 518, 530, 50, 30, 30);
            g2D.setColor(new Color(230, 184, 144));
            g2D.fillRoundRect(100, 50, 400, 50, 30, 30);
            g2D.fillRoundRect(35, 518, 530, 50, 30, 30);


            g2D.setColor(Color.BLACK);
            g2D.setFont(new Font("Plain", Font.BOLD, 18));
            g2D.drawString("Every game begins with three blocks of 3", 118, 80);
            g2D.drawString("Let's see what happens when the left key is pressed...", 60, 550);


            //Next Button
            g2D.setColor(new Color(136, 55, 23));
            g2D.fillRoundRect(tutorialNext.x, tutorialNext.y, tutorialNext.width, tutorialNext.height, 25, 25);
            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(2));
            g2D.drawRoundRect(tutorialNext.x, tutorialNext.y, tutorialNext.width, tutorialNext.height, 25, 25);


            g2D.setColor(new Color(239, 211, 204));
            g2D.drawString("NEXT", 535, 27);


            //Back Button
            g2D.setColor(new Color(136, 55, 23));
            g2D.fillRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(2));
            g2D.drawRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);


            g2D.setColor(new Color(239, 211, 204));
            g2D.drawString("BACK", 10, 27);
        }


        else if (pageSwitch == 4) {
            setBackground(new Color(214, 207, 187));
            Image tutorialPicOne = Toolkit.getDefaultToolkit().getImage("tutorialPicTwo.png");
            g2D.drawImage(tutorialPicOne, 100, 100, 400, 400, this);


            g2D.setColor(new Color(255, 224, 198));
            g2D.fillRoundRect(100, 80, 400, 100, 30, 30);


            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(2));
            g2D.drawRoundRect(100, 80, 400, 100, 30, 30);


            g2D.setFont(new Font("Plain", Font.BOLD, 18));
            g2D.drawString("Pressing the left key shifts every block", 118, 120);
            g2D.drawString("to the left as far as possible:", 118, 150);


            //Next Button
            g2D.setColor(new Color(136, 55, 23));
            g2D.fillRoundRect(tutorialNext.x, tutorialNext.y, tutorialNext.width, tutorialNext.height, 25, 25);
            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(2));
            g2D.drawRoundRect(tutorialNext.x, tutorialNext.y, tutorialNext.width, tutorialNext.height, 25, 25);


            g2D.setColor(new Color(239, 211, 204));
            g2D.drawString("NEXT", 535, 27);


            //Back Button
            g2D.setColor(new Color(136, 55, 23));
            g2D.fillRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(2));
            g2D.drawRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);


            g2D.setColor(new Color(239, 211, 204));
            g2D.drawString("BACK", 10, 27);


        }


        else if (pageSwitch == 5) {
            Image tutorialPicOne = Toolkit.getDefaultToolkit().getImage("tutorialPicTwo.png");
            g2D.drawImage(tutorialPicOne, 100, 100, 400, 400, this);


            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(4));
            g2D.drawRoundRect(300, 350, 280, 100, 30, 30);
            g2D.setColor(new Color(255, 224, 198));
            g2D.fillRoundRect(300, 350, 280, 100, 30, 30);


            g2D.setFont(new Font("Plain", Font.BOLD, 16));
            g2D.setColor(Color.BLACK);
            g2D.drawString("Notice how a new block of 3", 318, 390);
            g2D.drawString("is generated in a random spot!", 318, 420);


            //Next Button
            g2D.setFont(new Font("Plain", Font.BOLD, 18));
            g2D.setColor(new Color(136, 55, 23));
            g2D.fillRoundRect(tutorialNext.x, tutorialNext.y, tutorialNext.width, tutorialNext.height, 25, 25);
            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(2));
            g2D.drawRoundRect(tutorialNext.x, tutorialNext.y, tutorialNext.width, tutorialNext.height, 25, 25);


            g2D.setColor(new Color(239, 211, 204));
            g2D.drawString("NEXT", 535, 27);


            //Back Button
            g2D.setColor(new Color(136, 55, 23));
            g2D.fillRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(2));
            g2D.drawRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);


            g2D.setColor(new Color(239, 211, 204));
            g2D.drawString("BACK", 10, 27);
        }


        else if (pageSwitch == 6) {
            Image tutorialPicOne = Toolkit.getDefaultToolkit().getImage("tutorialPicThree.png");
            g2D.drawImage(tutorialPicOne, 100, 100, 400, 400, this);


            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(4));
            g2D.drawRoundRect(80, 350, 350, 100, 30, 30);
            g2D.setColor(new Color(255, 194, 144));
            g2D.fillRoundRect(80, 350, 350, 100, 30, 30);


            g2D.setFont(new Font("Plain", Font.BOLD, 16));
            g2D.setColor(Color.BLACK);
            g2D.drawString("After pressing the up key", 98, 390);
            g2D.drawString("every block moves up as far as possible!", 98, 420);


            //Next Button
            g2D.setFont(new Font("Plain", Font.BOLD, 18));
            g2D.setColor(new Color(136, 55, 23));
            g2D.fillRoundRect(tutorialNext.x, tutorialNext.y, tutorialNext.width, tutorialNext.height, 25, 25);
            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(2));
            g2D.drawRoundRect(tutorialNext.x, tutorialNext.y, tutorialNext.width, tutorialNext.height, 25, 25);


            g2D.setColor(new Color(239, 211, 204));
            g2D.drawString("NEXT", 535, 27);


            //Back Button
            g2D.setColor(new Color(136, 55, 23));
            g2D.fillRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(2));
            g2D.drawRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);


            g2D.setColor(new Color(239, 211, 204));
            g2D.drawString("BACK", 10, 27);
        }


        else if (pageSwitch == 7) {
            Image tutorialPicOne = Toolkit.getDefaultToolkit().getImage("tutorialPicThree.png");
            g2D.drawImage(tutorialPicOne, 100, 100, 400, 400, this);


            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(4));
            g2D.drawRoundRect(240, 230, 200, 70, 30, 30);
            g2D.setColor(new Color(255, 194, 144));
            g2D.fillRoundRect(240, 230, 200, 70, 30, 30);


            g2D.setFont(new Font("Plain", Font.BOLD, 16));
            g2D.setColor(Color.BLACK);
            g2D.drawString("Notice how another", 258, 257);
            g2D.drawString("'3' block spawned!", 258, 287);


            //Next Button
            g2D.setFont(new Font("Plain", Font.BOLD, 18));
            g2D.setColor(new Color(136, 55, 23));
            g2D.fillRoundRect(tutorialNext.x, tutorialNext.y, tutorialNext.width, tutorialNext.height, 25, 25);
            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(2));
            g2D.drawRoundRect(tutorialNext.x, tutorialNext.y, tutorialNext.width, tutorialNext.height, 25, 25);


            g2D.setColor(new Color(239, 211, 204));
            g2D.drawString("NEXT", 535, 27);


            //Back Button
            g2D.setColor(new Color(136, 55, 23));
            g2D.fillRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(2));
            g2D.drawRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);


            g2D.setColor(new Color(239, 211, 204));
            g2D.drawString("BACK", 10, 27);
        }


        else if (pageSwitch == 8) {
            Image tutorialPicOne = Toolkit.getDefaultToolkit().getImage("tutorialPicFour.png");
            g2D.drawImage(tutorialPicOne, 100, 100, 400, 400, this);


            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(4));
            g2D.drawRoundRect(106, 80, 390, 40, 30, 30);
            g2D.setColor(new Color(250, 190, 134));
            g2D.fillRoundRect(106, 80, 390, 40, 30, 30);


            g2D.setFont(new Font("Plain", Font.BOLD, 16));
            g2D.setColor(Color.BLACK);
            g2D.drawString("Look what happened when we pressed left again", 115, 107);


            g2D.drawRoundRect(150, 490, 300, 40, 30, 30);
            g2D.setColor(new Color(227, 143, 72));
            g2D.fillRoundRect(150, 490, 300, 40, 30, 30);


            g2D.setFont(new Font("Plain", Font.BOLD, 16));
            g2D.setColor(Color.BLACK);
            g2D.drawString("Have you been noticing a pattern?", 168, 517);


            //Next Button
            g2D.setFont(new Font("Plain", Font.BOLD, 18));
            g2D.setColor(new Color(136, 55, 23));
            g2D.fillRoundRect(tutorialNext.x, tutorialNext.y, tutorialNext.width, tutorialNext.height, 25, 25);
            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(2));
            g2D.drawRoundRect(tutorialNext.x, tutorialNext.y, tutorialNext.width, tutorialNext.height, 25, 25);


            g2D.setColor(new Color(239, 211, 204));
            g2D.drawString("NEXT", 535, 27);


            //Back Button
            g2D.setColor(new Color(136, 55, 23));
            g2D.fillRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(2));
            g2D.drawRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);


            g2D.setColor(new Color(239, 211, 204));
            g2D.drawString("BACK", 10, 27);
        }


        else if (pageSwitch == 9) {
            Image tutorialPicOne = Toolkit.getDefaultToolkit().getImage("tutorialPicFour.png");
            g2D.drawImage(tutorialPicOne, 100, 100, 400, 400, this);


            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(6));
            g2D.drawRoundRect(15, 500, 560, 60, 30, 30);
            g2D.setColor(new Color(255, 111, 51));
            g2D.fillRoundRect(15, 500, 560, 60, 30, 30);


            g2D.setFont(new Font("Plain", Font.BOLD, 18));
            g2D.setColor(Color.BLACK);
            g2D.drawString("Identical blocks merge and add when you shift them together!", 22, 537);


            //Next Button
            g2D.setColor(new Color(136, 55, 23));
            g2D.fillRoundRect(tutorialNext.x, tutorialNext.y, tutorialNext.width, tutorialNext.height, 25, 25);
            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(2));
            g2D.drawRoundRect(tutorialNext.x, tutorialNext.y, tutorialNext.width, tutorialNext.height, 25, 25);


            g2D.setColor(new Color(239, 211, 204));
            g2D.drawString("NEXT", 535, 27);


            //Back Button
            g2D.setColor(new Color(136, 55, 23));
            g2D.fillRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(2));
            g2D.drawRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);


            g2D.setColor(new Color(239, 211, 204));
            g2D.drawString("BACK", 10, 27);
        }


        else if (pageSwitch == 10) {
            Image tutorialPicOne = Toolkit.getDefaultToolkit().getImage("tutorialPicFour.png");
            g2D.drawImage(tutorialPicOne, 100, 100, 400, 400, this);


            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(6));
            g2D.drawRoundRect(30, 500, 540, 60, 30, 30);
            g2D.setColor(new Color(253, 172, 137));
            g2D.fillRoundRect(30, 500, 540, 60, 30, 30);


            //Current score & Best score
            g2D.setStroke(new BasicStroke(3));
            g2D.setColor(Color.BLACK);
            g2D.drawRect(170, 40, 80, 30); //current score
            g2D.drawRect(350, 40, 80, 30); //best score
            g2D.setColor(new Color(230, 144, 123));
            g2D.fillRect(170, 40, 80, 30); //current score
            g2D.fillRect(350, 40, 80, 30); //best score




            //Score texts
            g2D.setColor(Color.BLACK);
            g2D.setFont(new Font("Plain", Font.BOLD, 12));
            g2D.drawString("Current Score", 169, 30);
            g2D.drawString("Best Score", 358, 30);
            g2D.setFont(new Font("Plain", Font.BOLD, 16));
            g2D.drawString("12", 200, 60);
            g2D.drawString("12", 380, 60);




            g2D.setStroke(new BasicStroke(6));
            g2D.setFont(new Font("Plain", Font.BOLD, 18));
            g2D.setColor(Color.BLACK);
            g2D.drawString("Your current score and best score will be shown at the top", 39, 537);


            //Next Button
            g2D.setColor(new Color(119, 62, 39));
            g2D.fillRoundRect(tutorialNext.x, tutorialNext.y, tutorialNext.width, tutorialNext.height, 25, 25);
            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(2));
            g2D.drawRoundRect(tutorialNext.x, tutorialNext.y, tutorialNext.width, tutorialNext.height, 25, 25);


            g2D.setColor(new Color(239, 211, 204));
            g2D.drawString("NEXT", 535, 27);


            //Back Button
            g2D.setColor(new Color(136, 55, 23));
            g2D.fillRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(2));
            g2D.drawRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);


            g2D.setColor(new Color(239, 211, 204));
            g2D.drawString("BACK", 10, 27);
        }


        else if (pageSwitch == 11) {
            Image tutorialPicOne = Toolkit.getDefaultToolkit().getImage("tutorialPicFive.png");
            g2D.drawImage(tutorialPicOne, 100, 100, 400, 400, this);


            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(6));
            g2D.drawRoundRect(140, 100, 320, 70, 30, 30);
            g2D.setColor(new Color(136, 55, 23));
            g2D.fillRoundRect(140, 100, 320, 70, 30, 30);


            g2D.setFont(new Font("Plain", Font.BOLD, 18));
            g2D.setColor(new Color(239, 211, 204));
            g2D.drawString("Create a block of 19683 to win!!!", 158, 140);


            //Home Button
            g2D.drawRoundRect(tutorialHome.x, tutorialHome.y, tutorialHome.width, tutorialHome.height, 30, 30);
            g2D.setColor(new Color(152, 108, 93));
            g2D.fillRoundRect(tutorialHome.x, tutorialHome.y, tutorialHome.width, tutorialHome.height, 30, 30);


            g2D.setColor(new Color(255, 166, 143));
            g2D.drawString("HOME", 271, 566);


            //Back Button
            g2D.setColor(new Color(136, 55, 23));
            g2D.fillRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(2));
            g2D.drawRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);


            g2D.setColor(new Color(239, 211, 204));
            g2D.drawString("BACK", 10, 27);


        }


        else if (pageSwitch == 12) {
            setBackground(new Color(241, 203, 136));
            g2D.setFont(new Font("Plain", Font.BOLD, 24));
            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(6));
            g2D.drawString("Made by", 250, 100);


            g2D.setFont(new Font("Plain", Font.BOLD, 20));
            g2D.drawString("Will", 150, 500);
            g2D.drawString("Aiden", 400, 500);


            //add image of ourselves maybe


            //Back Button
            g2D.setFont(new Font("Plain", Font.BOLD, 18));
            g2D.setColor(new Color(136, 55, 23));
            g2D.fillRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(2));
            g2D.drawRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);


            g2D.setColor(new Color(239, 211, 204));
            g2D.drawString("BACK", 10, 27);
        }
        else if (pageSwitch == 13) {
            setBackground(new Color(184, 163, 139));


            //Settings Box
            g2D.setStroke(new BasicStroke(5));
            g2D.drawRoundRect(90,100,400,450,50,50);
            g2D.setColor(new Color(142, 110, 101));
            g2D.fillRoundRect(90,100,400,450,50,50);


            //Music Button
            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(3));
            g2D.drawRoundRect(140,150,300,80,50,50);
            g2D.setColor(new Color(221, 174, 162));
            g2D.fillRoundRect(140,150,300,80,50,50);


            //SFX Button
            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(3));
            g2D.drawRoundRect(140,280,300,80,50,50);
            g2D.setColor(new Color(221, 174, 162));
            g2D.fillRoundRect(140,280,300,80,50,50);


            //Backgrounds Button
            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(3));
            g2D.drawRoundRect(140,410,300,80,50,50);
            g2D.setColor(new Color(188, 119, 99));
            g2D.fillRoundRect(140,410,300,80,50,50);
            g2D.setFont(new Font("Plain", Font.BOLD, 18));


            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(3));
            g2D.drawString("BACKGROUNDS", 213, 455);




            if (musicButton == 1) {
                //Music ON Texts
                g2D.drawRoundRect(musicOn.x, musicOn.y, musicOn.width, musicOn.height, 20, 20); //MUSIC
                g2D.setColor(new Color(29, 225, 41));
                g2D.fillRoundRect(musicOn.x, musicOn.y, musicOn.width, musicOn.height, 20, 20); //MUSIC
                g2D.setColor(Color.BLACK);
                g2D.drawString("ON", 325, 197);
            }


            else if (musicButton == -1){
                //Music OFF Texts
                g2D.drawRoundRect(musicOn.x, musicOn.y, musicOn.width, musicOn.height, 20, 20); //MUSIC
                g2D.setColor(new Color(237, 69, 69));
                g2D.fillRoundRect(musicOn.x, musicOn.y, musicOn.width, musicOn.height, 20, 20);
                g2D.setColor(Color.BLACK);
                g2D.drawString("OFF", 322, 197);


            }


            if (SFXButton == 1) {
                // SFX ON Texts
                g2D.drawRoundRect(SFXOn.x, SFXOn.y, SFXOn.width, SFXOn.height, 20, 20); //SFX
                g2D.setColor(new Color(29, 225, 41));
                g2D.fillRoundRect(SFXOn.x, SFXOn.y, SFXOn.width, SFXOn.height, 20, 20); //SFX
                g2D.setColor(Color.BLACK);
                g2D.drawString("ON", 325, 327);
            }
            else if (SFXButton == -1){
                // SFX OFF Texts
                g2D.drawRoundRect(SFXOn.x, SFXOn.y, SFXOn.width, SFXOn.height, 20, 20); //SFX
                g2D.setColor(new Color(237, 69, 69));
                g2D.fillRoundRect(SFXOn.x, SFXOn.y, SFXOn.width, SFXOn.height, 20, 20); //SFX
                g2D.setColor(Color.BLACK);
                g2D.drawString("OFF", 322, 327);
            }
            //Settings Texts
            g2D.setColor(Color.BLACK);
            g2D.setFont(new Font("Plain", Font.BOLD, 20));
            g2D.drawString("MUSIC", 190, 197);
            g2D.drawString("SFX", 200, 327);


            //Back Button
            g2D.setFont(new Font("Plain", Font.BOLD, 18));
            g2D.setColor(new Color(136, 55, 23));
            g2D.fillRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(2));
            g2D.drawRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);


            g2D.setColor(new Color(239, 211, 204));
            g2D.drawString("BACK", 10, 27);
        }


        else if (pageSwitch == 14) {


            g2D.drawImage(chongqing, 30, 220, 160, 200, this);
            g2D.drawImage(newYork, 220, 220, 160, 200, this);
            g2D.drawImage(seoul, 410, 220, 160, 200, this);


            setBackground(new Color(189, 211, 116));
            g2D.setFont(new Font("Serif", Font.BOLD, 40));
            g2D.setColor(Color.BLACK);
            g2D.drawString("CITIES", 230, 110);


            g2D.setFont(new Font("Plain", Font.BOLD, 18));
            g2D.setStroke(new BasicStroke(6));


            //image borders
            g2D.draw(chongqingButton);
            g2D.draw(newYorkButton);
            g2D.draw(seoulButton);


            g2D.drawString("Chongqing", 60, 455);
            g2D.drawString("New York", 260, 455);
            g2D.drawString("Seoul", 465, 455);


            //Back Button
            g2D.setFont(new Font("Plain", Font.BOLD, 18));
            g2D.setColor(new Color(136, 55, 23));
            g2D.fillRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(2));
            g2D.drawRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);


            g2D.setColor(new Color(239, 211, 204));
            g2D.drawString("BACK", 10, 27);


            //Reset Button
            g2D.setColor(new Color(136, 55, 23));
            g2D.fillRoundRect(resetButton.x, resetButton.y, resetButton.width, resetButton.height, 25, 25);
            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(2));
            g2D.drawRoundRect(resetButton.x, resetButton.y, resetButton.width, resetButton.height, 25, 25);


            g2D.setColor(new Color(239, 211, 204));
            g2D.drawString("RESET", 270, 542);
        }
    }


    public void actionPerformed(ActionEvent event) {
    }


    // MouseListener methods
    public void mouseClicked(MouseEvent e) {


        Point clicked = e.getPoint();


        //HomePage Buttons


        //Play
        if (pageSwitch == 1) {
            if (playButton.contains(clicked)) {
                pageSwitch = 2;
                repaint();
            }
        }
        //Tutorial
        if (pageSwitch == 1) {
            if (tutorialButton.contains(clicked)) {
                pageSwitch = 3;
                repaint();
            }
        }
        //Credits
        if (pageSwitch == 1) {
            if (creditsButton.contains(clicked)) {
                pageSwitch = 12;
                repaint();
            }
        }
        //Settings
        if (pageSwitch == 1) {
            if (settingsButton.contains(clicked)) {
                pageSwitch = 13;
                repaint();
            }
        }
        //Exit
        if (pageSwitch == 1) {
            if (exitButton.contains(clicked)) {
                System.exit(0);
            }
        }
        //Game
        else if (pageSwitch == 2) {
            if (backButton.contains(clicked)) {
                pageSwitch = 1;
                repaint();
            }
        }


        //Tutorial Buttons
        else if (pageSwitch == 3) {
            if (tutorialNext.contains(clicked)) {
                pageSwitch = 4;
                repaint();
            }
            if (backButton.contains(clicked)) {
                pageSwitch = 1;
                repaint();
            }
        } else if (pageSwitch == 4) {
            if (tutorialNext.contains(clicked)) {
                pageSwitch = 5;
                repaint();
            }
            if (backButton.contains(clicked)) {
                pageSwitch = 3;
                repaint();
            }
        } else if (pageSwitch == 5) {
            if (tutorialNext.contains(clicked)) {
                pageSwitch = 6;
                repaint();
            }
            if (backButton.contains(clicked)) {
                pageSwitch = 4;
                repaint();
            }
        } else if (pageSwitch == 6) {
            if (tutorialNext.contains(clicked)) {
                pageSwitch = 7;
                repaint();
            }
            if (backButton.contains(clicked)) {
                pageSwitch = 5;
                repaint();
            }
        } else if (pageSwitch == 7) {
            if (tutorialNext.contains(clicked)) {
                pageSwitch = 8;
                repaint();
            }
            if (backButton.contains(clicked)) {
                pageSwitch = 6;
                repaint();
            }
        } else if (pageSwitch == 8) {
            if (tutorialNext.contains(clicked)) {
                pageSwitch = 9;
                repaint();
            }
            if (backButton.contains(clicked)) {
                pageSwitch = 7;
                repaint();
            }
        } else if (pageSwitch == 9) {
            if (tutorialNext.contains(clicked)) {
                pageSwitch = 10;
                repaint();
            }
            if (backButton.contains(clicked)) {
                pageSwitch = 8;
                repaint();
            }
        } else if (pageSwitch == 10) {
            if (tutorialNext.contains(clicked)) {
                pageSwitch = 11;
                repaint();
            }
            if (backButton.contains(clicked)) {
                pageSwitch = 9;
                repaint();
            }
        } else if (pageSwitch == 11) {
            if (backButton.contains(clicked)) {
                pageSwitch = 10;
                repaint();
            }
            if (tutorialHome.contains(clicked)) {
                pageSwitch = 1;
                repaint();
            }
        } else if (pageSwitch == 12) {
            if (backButton.contains(clicked)) {
                pageSwitch = 1;
                repaint();
            }
        }


        else if (pageSwitch == 13) {
            if (backButton.contains(clicked)) {
                pageSwitch = 1;
                repaint();
            } else if (musicOn.contains(clicked)) {
                if (musicButton == 1) {
                    musicButton = -1;
                    music.stop();
                    repaint();
                }
                else if (musicButton == -1) {
                    musicButton = 1;
                    music.start();
                    music.loop(Clip.LOOP_CONTINUOUSLY);
                    repaint();
                }
            } else if (SFXOn.contains(clicked)) {
                if (SFXButton == 1) {
                    SFXButton = -1;
                    repaint();
                } else if (SFXButton == -1) {
                    SFXButton = 1;
                    repaint();
                }
            } else if (backgroundButton.contains(clicked)){
                pageSwitch = 14;
                repaint();
            }
        }
        else if (pageSwitch == 14){
            if (backButton.contains(clicked)) {
                pageSwitch = 13;
                repaint();
            }
            else if (chongqingButton.contains(clicked)){
                backgroundChange = 2;
            }
            else if (newYorkButton.contains(clicked)){
                backgroundChange = 3;
            }
            else if (seoulButton.contains(clicked)){
                backgroundChange = 4;
            }
            else if (resetButton.contains(clicked)){
                backgroundChange = 1;
            }
        }
    }




    public void mouseReleased(MouseEvent e) {
    }


    public void mouseEntered(MouseEvent e) {
    }


    public void mouseExited(MouseEvent e) {
    }


    public void mousePressed(MouseEvent e) {
    }


    //Mouse Motion Listener Methods
    public void mouseDragged(MouseEvent e) {
    }


    public void mouseMoved(MouseEvent e) {
    }


    // KeyListener methods
    public void keyPressed(KeyEvent kp) {
    }


    public void keyReleased(KeyEvent e) {
    }


    public void keyTyped(KeyEvent e) {
    }




    public static void main(String[] args) throws IOException{
        game = new JFrame("19683");
        Main panel = new Main();
        game.add(panel);
        game.pack();
        game.setVisible(true);


        //Background Music
        try {
            AudioInputStream backgroundMusic = AudioSystem.getAudioInputStream(new File ("background_music.wav"));
            music = AudioSystem.getClip();
            music.open(backgroundMusic);
        }
        catch (Exception  e) {
        }


        music.start();
        //Loops background music forever
        music.loop(Clip.LOOP_CONTINUOUSLY);


        //Stops music when window closes
        game.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing (java.awt.event.WindowEvent windowEvent) {
                music.close();
            }
        });
    }
}

