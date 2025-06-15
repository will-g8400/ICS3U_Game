
//bunch of imports for all the stuff we have used

import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
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
import java.util.Arrays;
import javax.swing.Timer;


public class Main extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener {

    //images used for the setting as well as the background of our game
    Image setting_icon;
    BufferedImage chongqing;
    BufferedImage newYork;
    BufferedImage seoul;
    BufferedImage aiden;
    BufferedImage will;

    //the variables defined to use music to play background sound and our merge SFX
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

    Rectangle backgroundButton = new Rectangle(140, 410, 300, 80);

    Rectangle chongqingButton = new Rectangle(30, 220, 160, 200);
    Rectangle newYorkButton = new Rectangle(220, 220, 160, 200);
    Rectangle seoulButton = new Rectangle(410, 220, 160, 200);
    Rectangle resetButton = new Rectangle(260, 515, 80, 40);
    Rectangle resetBoardButton = new Rectangle(250, 506, 100, 40);

    int musicButton = 1;
    int SFXButton = 1;
    int backgroundChange = 1;
    int skin = 0;

    static JFrame game;

    //1: Home, 2: Game, 3-11: Tutorial, 12: Credits, 13: Settings, 14: Custom Backgrounds
    int pageSwitch = 1; //every screen has a unique integer value of pageSwitch, which lets the program know what screen to draw
    // tracks whether mouse is over the buttons
    //each button has their own boolean because if not, then hovering one button would make program think the other buttons are hovering as well

    boolean isHoveringPlay = false;
    boolean isHoveringTutorial = false;
    boolean isHoveringCredits = false;
    boolean isHoveringExit = false;
    boolean isHoveringSettings = false;
    boolean isHoveringMusic = false;
    boolean isHoveringSFX = false;
    boolean isHoveringBackgrounds = false;
    boolean isHoveringReset = false;
    boolean isHoveringBack = false;
    boolean isHoveringNext = false;
    boolean isHoveringHome = false;

    //board used to keep track of current board state
    int[][] board = new int[5][5];

    //saves oldboard state to do animation
    int[][] oldBoard = new int[5][5];

    //keeps tracks of the number of steps each block needs move
    int[][] stepMoved = new int[5][5];

    //keeps tracks of which block can be merged
    int[][] mergeStepMoved = new int[5][5];

    //stores the skin set for our board pieces
    BufferedImage[] images_Number = new BufferedImage[11];
    BufferedImage[] images_Color = new BufferedImage[11];
    BufferedImage[] images_fruit = new BufferedImage[11];

    BufferedImage[][] imageCollection = {images_Number, images_Color, images_fruit};

    //you win and you lose images
    Image you_win = new ImageIcon("you_win.png").getImage();
    Image you_lose = new ImageIcon("you_lose.png").getImage();

    //self explanatory enough
    final int SQUARE_SIZE = 60;
    final int TOP_OFFSET = 150;
    final int BORDER_SIZE = 150;

    //self explanatory enough
    static int score = 0;
    static int highScore = 0;

    // x,y, xMove, and Ymove are used for animation of the blocks
    int x = 0;
    int y = 0;

    int XMove = 0;
    int YMove = 0;

    //these trackers are used to track how many steps the blocks can move in different directions
    int rightTracker;
    int leftTracker;
    int downTracker;
    int upTracker;

    //animationTracker is how many pixels the board pieces are moving
    int animationTracker;
    //biggest show tells me the biggest number of steps a board piece needs to move within a current board
    int biggestStep = 0;

    //timer used for animations
    Timer timer;
    Timer timer2;
    Timer timer3;
    Timer timer4;
    Timer timer5;

    //these variables are used for anti key spamming
    long lastInputTime;
    long cooldown = 200;
    int key;

    //anti_double_animation also used for anti key spamming
    //prevent you from hitting two keys at a time, which sort of looks like
    //the blocks are going diagonal
    boolean anti_double_animation = false;

    //this variable is responsible for checking if a block can spawn on the board
    boolean blockSpawning = true;

    //did move checks if the board actually changed or not
    boolean didMove = false;

    //these two check whether or not to show the lost or winner screen
    boolean winner = false;
    boolean lost = false;

    //index decides based on the value of the board pieces, which picture to show exactly
    int index;

    //these two are used for double buffering and drawing graphics
    Image offscreenImage;
    Graphics2D offscreenGraphics;


    //constructor --> initial set up
    public Main() throws IOException {
        //set the size
        setPreferredSize(new Dimension(600, 600));
        setBackground(new Color(222, 207, 189));

        //get all the images that we need
        setting_icon = Toolkit.getDefaultToolkit().getImage("Setting_Icon.png");
        chongqing = ImageIO.read(new File("chongqingBackground.png"));
        newYork = ImageIO.read(new File("newYorkBackground.png"));
        seoul = ImageIO.read(new File("seoulBackground.png"));

        //aiden and will's faces
        aiden = ImageIO.read(new File("aiden.jpg"));
        will = ImageIO.read(new File("will.jpg"));

        int start = 3;

        for (int x = 0; x < 11; x++) {
            images_Number[x] = ImageIO.read(new File(start + ".png"));
            start *= 2;
        }

        try {
            images_Color[0] = ImageIO.read(new File("red.png"));
            images_Color[1] = ImageIO.read(new File("blue.png"));
            images_Color[2] = ImageIO.read(new File("cyan.png"));
            images_Color[3] = ImageIO.read(new File("lightblue.png"));
            images_Color[4] = ImageIO.read(new File("darkblue.png"));
            images_Color[5] = ImageIO.read(new File("green.png"));
            images_Color[6] = ImageIO.read(new File("orange.png"));
            images_Color[7] = ImageIO.read(new File("pink.png"));
            images_Color[8] = ImageIO.read(new File("purple.png"));
            images_Color[9] = ImageIO.read(new File("yellow.png"));
            images_Color[10] = ImageIO.read(new File("black.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            images_fruit[0] = ImageIO.read(new File("blueberry.jpg"));
            images_fruit[1] = ImageIO.read(new File("grape.jpg"));
            images_fruit[2] = ImageIO.read(new File("strawberry.jpg"));
            images_fruit[3] = ImageIO.read(new File("kiwi.jpg"));
            images_fruit[4] = ImageIO.read(new File("banana.jpg"));
            images_fruit[5] = ImageIO.read(new File("peach.jpg"));
            images_fruit[6] = ImageIO.read(new File("mango.jpg"));
            images_fruit[7] = ImageIO.read(new File("orange.jpg"));
            images_fruit[8] = ImageIO.read(new File("dragonfruit.jpg"));
            images_fruit[9] = ImageIO.read(new File("pineapple.jpg"));
            images_fruit[10] = ImageIO.read(new File("watermelon.jpg"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        //add all the listeners that can help detect button clicks and key clicks
        addMouseListener(this); //detects button clicks
        addMouseMotionListener(this);
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();

        //starter blocks that is randomly generated on the board
        int starterBlock1_X = (int) (Math.random() * 5);
        int starterBlock1_Y = (int) (Math.random() * 5);

        int starterBlock2_X = (int) (Math.random() * 5);
        int starterBlock2_Y = (int) (Math.random() * 5);

        while (starterBlock2_X == starterBlock1_X && starterBlock2_Y == starterBlock1_Y) {
            starterBlock2_X = (int) (Math.random() * 5);
            starterBlock2_Y = (int) (Math.random() * 5);
        }

        int starterBlock3_X = (int) (Math.random() * 5);
        int starterBlock3_Y = (int) (Math.random() * 5);


        while (starterBlock3_X == starterBlock2_X && starterBlock3_Y == starterBlock2_Y
                || starterBlock3_X == starterBlock1_X && starterBlock3_Y == starterBlock1_Y) {
            starterBlock2_X = (int) (Math.random() * 5);
            starterBlock2_Y = (int) (Math.random() * 5);
        }

        //the board is set to these random blocks
        board[starterBlock1_Y][starterBlock1_X] = 3;
        board[starterBlock2_Y][starterBlock2_X] = 3;
        board[starterBlock3_Y][starterBlock3_X] = 3;


        //old board is also set to these blocks
        //this is to intialize so that i can draw the blocks first and then animate the old board to the new board
        oldBoard[starterBlock1_Y][starterBlock1_X] = 3;
        oldBoard[starterBlock2_Y][starterBlock2_X] = 3;
        oldBoard[starterBlock3_Y][starterBlock3_X] = 3;


    }

    //used to do graphics
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        drawnPages(g2D);
    }

    //description: this method is used shift all the blocks to the left, right, up, or down
    //parameter: it will take in the current board as well as the dirction in both X and Y to
    //decide how it will update the board
    //return: nothing
    public void Update_board(int[][] board1, int directionX, int directionY) {

        //right
        if (directionX > 0) {
            //i loop through the entire board 5 times to check if the next block is empty
            //if it is, eventually, all the blocks are shifted to the right
            //rest of the directions basically do the same thing
            for (int checkAll = 1; checkAll <= 5; checkAll++) {
                for (int row = 0; row < 5; row++) {
                    for (int column = 0; column < 4; column++) {
                        if (board1[row][column] != 0 && board1[row][column + 1] == 0) {
                            board1[row][column + 1] = board1[row][column];
                            board1[row][column] = 0;
                        }
                    }
                }
            }
            //left
        } else if (directionX < 0) {

            for (int checkAll = 1; checkAll <= 5; checkAll++) {
                for (int row = 0; row < 5; row++) {
                    for (int column = 4; column > 0; column--) {
                        if (board1[row][column] != 0 && board1[row][column - 1] == 0) {
                            board1[row][column - 1] = board1[row][column];
                            board1[row][column] = 0;
                        }
                    }
                }
            }

            //down
        } else if (directionY > 0) {

            for (int checkAll = 1; checkAll <= 5; checkAll++) {
                for (int column = 0; column < 5; column++) {
                    for (int row = 0; row < 4; row++) {

                        if (board1[row][column] != 0 && board1[row + 1][column] == 0) {
                            board1[row + 1][column] = board1[row][column];
                            board1[row][column] = 0;
                        }
                    }
                }
            }

            //up
        } else if (directionY < 0) {

            for (int checkAll = 1; checkAll <= 5; checkAll++) {
                for (int column = 0; column < 5; column++) {
                    for (int row = 4; row > 0; row--) {

                        if (board1[row][column] != 0 && board1[row - 1][column] == 0) {
                            board1[row - 1][column] = board1[row][column];
                            board1[row][column] = 0;
                        }
                    }
                }
            }
        }
    }

    //description: it will check if the board can randomly generate a new block
    //if so, it will create a new block/board piece
    //parameter: nothing
    //return: nothing
    public void newBlock() {

        //first randomly generate the coordinates of a random block
        int randomblockX = (int) (Math.random() * 5);
        int randomblockY = (int) (Math.random() * 5);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {

                //first i will check that a empty spot, does exist
                if (board[i][j] == 0) {
                    //then i'll keep randomly generate a block position that is empty
                    while (board[randomblockY][randomblockX] != 0) {
                        randomblockX = (int) (Math.random() * 5);
                        randomblockY = (int) (Math.random() * 5);
                    }
                    board[randomblockY][randomblockX] = 3;
                    return;
                }
            }
        }
    }


    //description: this methods helps to keep track how many steps each block needs to move
    //depending on the direction
    //parameter: nothing
    //return: nothing
    public void how_many_steps_moved() {

        //to reset the stepMoved 2D array to prevent remnants from messing things up
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                stepMoved[i][j] = 0;
            }
        }

        //right
        if (x > 0 && y == 0) {
            //i start from subtracting the index of the right side against blocks that are not empty from the position of the board
            //before it updates, which tells me how many steps each block needs to move in the right direction
            //all other direction, basically work the same way

            for (int i = 0; i < 5; i++) {
                rightTracker = 4;
                for (int j = 4; j >= 0; j--) {
                    if (oldBoard[i][j] != 0) {
                        stepMoved[i][j] = rightTracker - j;
                        rightTracker--;
                    }
                }
            }

            //left
        } else if (x < 0 && y == 0) {
            for (int i = 0; i < 5; i++) {
                leftTracker = 0;
                for (int j = 0; j < 5; j++) {
                    if (oldBoard[i][j] != 0) {
                        stepMoved[i][j] = leftTracker - j;
                        leftTracker++;
                    }
                }
            }

            //down
        } else if (y > 0 && x == 0) {
            for (int i = 0; i < 5; i++) {
                downTracker = 4;
                for (int j = 4; j >= 0; j--) {
                    if (oldBoard[j][i] != 0) {
                        stepMoved[j][i] = downTracker - j;
                        downTracker--;
                    }
                }
            }
            //up
        } else if (y < 0 && x == 0) {
            for (int i = 0; i < 5; i++) {
                upTracker = 0;
                for (int j = 0; j < 5; j++) {
                    if (oldBoard[j][i] != 0) {
                        stepMoved[j][i] = upTracker - j;
                        upTracker++;
                    }
                }
            }
        }
    }


    //description: this method helps to keep track the biggest number of animation pixels
    //the blocks need to move within a board
    //parameter: nothing
    //return: the largest number of animation pixels that needs to be animated
    public int animated_Steps() {

        //we first find the biggest number of steps moved by looping through stepMoved
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (Math.abs(stepMoved[i][j]) > Math.abs(biggestStep)) {
                    biggestStep = stepMoved[i][j];
                }
            }
        }
        //multiply biggest step with 60 to find the largest possible animation pixel
        //that needs to be animated
        animationTracker = biggestStep * 60;

        return animationTracker;
    }

    //description: this method is used to update the old board to equal the new board
    //when the animations are finished and is used help draw the board when all blocks are static
    //parameter: nothing
    //return: nothing
    public void copyBoard() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                oldBoard[i][j] = board[i][j];
            }
        }
    }

    //description: it will check if the pieces in the board can be merged
    //parameter: nothing
    //return: nothing
    public void merge() {

        //this is used to play the sound effect if merging occurs
        int merge_count_SFX = 0;

        //we reset this mergeStepdMoved though, which is used to keep track of which block
        //needs to be merged and helps with the merging animation
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                mergeStepMoved[i][j] = 0;
            }
        }

        // Right
        if (x > 0 && y == 0) {
            for (int row = 0; row < 5; row++) {
                for (int col = 4; col > 0; col--) {
                    //i basically check that if the adjacent block is the same i will merge
                    //to merge, i set the current block to twice the value and set the adjacent block to 0
                    //and i do that for all directions
                    if (board[row][col] != 0 && board[row][col] == board[row][col - 1]) {
                        board[row][col] *= 2;

                        //i also do add the updated board piece to the score board
                        //and do that for the rest of the direction as well
                        score += board[row][col];

                        board[row][col - 1] = 0;
                        mergeStepMoved[row][col - 1] = 1;

                        //this is incremented to prove that merge happened and a sound effect
                        //will play at the end
                        merge_count_SFX++;
                    }
                }
            }

            //left
        } else if (x < 0 && y == 0) {
            for (int row = 0; row < 5; row++) {
                for (int col = 0; col < 4; col++) {
                    if (board[row][col] != 0 && board[row][col] == board[row][col + 1]) {
                        board[row][col] *= 2;
                        score += board[row][col];
                        board[row][col + 1] = 0;
                        mergeStepMoved[row][col + 1] = -1;
                        merge_count_SFX++;
                    }
                }
            }


            //down
        } else if (x == 0 && y > 0) {
            for (int col = 0; col < 5; col++) {
                for (int row = 4; row > 0; row--) {
                    if (board[row][col] != 0 && board[row][col] == board[row - 1][col]) {
                        board[row][col] *= 2;
                        score += board[row][col];
                        board[row - 1][col] = 0;
                        mergeStepMoved[row - 1][col] = 1;
                        merge_count_SFX++;
                    }
                }
            }
            //up
        } else if (x == 0 && y < 0) {
            for (int col = 0; col < 5; col++) {
                for (int row = 0; row < 4; row++) {
                    if (board[row][col] != 0 && board[row][col] == board[row + 1][col]) {
                        board[row][col] *= 2;
                        score += board[row][col];
                        board[row + 1][col] = 0;
                        mergeStepMoved[row + 1][col] = -1;
                        merge_count_SFX++;
                    }
                }
            }
        }

        //here is where we play the audio if merge does happen
        if (merge_count_SFX > 0) {
            try {
                AudioInputStream mergeEffectSound = AudioSystem.getAudioInputStream(new File("mergeEffect.wav"));
                mergeEffect = AudioSystem.getClip();
                mergeEffect.open(mergeEffectSound);
            } catch (Exception e) {
            }
            if (SFXButton == 1) {
                mergeEffect.start();
            }

            //i also update my highscore here if the score is higher than the high score
            if (score > highScore) {
                highScore = score;
            }

        }

    }

    //description: this is exaclty as it sounds like, it helps me to check if the board
    //has a winning piece, the board is lost, or the board can continue
    //parameter: nothing
    //return: nothing
    public int checkWinner() {

        //this is used to check if the board is empty
        boolean isEmpty = false;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {

                //we loop through the board to see if it has the winning block
                if (board[i][j] == 3072) {
                    return 1;
                    //or has empty space
                } else if (board[i][j] == 0) {
                    isEmpty = true;
                }
            }
        }

        if (isEmpty) {
            return 2;
        }

        //or by default the board is full
        //and keep in mind that this method is only called after we merge
        return 3;
    }

    //description: this method reset the board and sets it up for a new game
    //parameter: nothing
    //return: nothing
    public void resetBoard() {

        //resets the board here by making everything 0
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                board[i][j] = 0;
                oldBoard[i][j] = 0;
            }
        }

        //then we randomly generate 3 new blocks
        int starterBlock1_X = (int) (Math.random() * 5);
        int starterBlock1_Y = (int) (Math.random() * 5);

        int starterBlock2_X = (int) (Math.random() * 5);
        int starterBlock2_Y = (int) (Math.random() * 5);

        while (starterBlock2_X == starterBlock1_X && starterBlock2_Y == starterBlock1_Y) {
            starterBlock2_X = (int) (Math.random() * 5);
            starterBlock2_Y = (int) (Math.random() * 5);
        }

        int starterBlock3_X = (int) (Math.random() * 5);
        int starterBlock3_Y = (int) (Math.random() * 5);


        while (starterBlock3_X == starterBlock2_X && starterBlock3_Y == starterBlock2_Y
                || starterBlock3_X == starterBlock1_X && starterBlock3_Y == starterBlock1_Y) {
            starterBlock2_X = (int) (Math.random() * 5);
            starterBlock2_Y = (int) (Math.random() * 5);
        }

        board[starterBlock1_Y][starterBlock1_X] = 3;
        board[starterBlock2_Y][starterBlock2_X] = 3;
        board[starterBlock3_Y][starterBlock3_X] = 3;

        oldBoard[starterBlock1_Y][starterBlock1_X] = 3;
        oldBoard[starterBlock2_Y][starterBlock2_X] = 3;
        oldBoard[starterBlock3_Y][starterBlock3_X] = 3;

        //reset the score to 0 as well then repaint
        score = 0;
        repaint();
    }

    //description: this method helps you check for what happens when you lose
    //parameter: nothing
    //return: nothing
    //press control f to search for fillBoardNoMerge method in another location
    //more description over there on what you can do with this
    public void fillBoardNoMerges() {
        int[][] values = {
                {3, 6, 12, 3, 6},
                {6, 12, 3, 6, 12},
                {12, 3, 6, 12, 3},
                {3, 6, 12, 3, 6},
                {6, 12, 3, 6, 12}
        };

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                board[i][j] = values[i][j];
            }
        }

        copyBoard();
        repaint();
    }

    //Description: This method is responsible for drawing all the screens and its components
    //Parameters: The graphics object g2D that allows drawing
    //Return: Nothing
    public void drawnPages(Graphics2D g2D) {

        if (pageSwitch == 1) {
            setBackground(new Color(222, 207, 189));
            //Title
            g2D.setColor(new Color(236, 89, 89));
            g2D.fillRoundRect(167, 110, 270, 110, 80, 80);
            g2D.setColor(Color.BLACK);
            g2D.setFont(new Font("Monospaced", Font.BOLD, 70));
            g2D.drawString("3072", 215, 187);
            g2D.setStroke(new BasicStroke(6));
            g2D.drawRoundRect(167, 110, 270, 110, 80, 80);

            g2D.setStroke(new BasicStroke(4));

            //Play Button
            //If the cursor is on the play button, it will draw an expanded version of the button to make it pop out
            //This mechanism is the same for all buttons
            //We made sure that each button's specific font size was put in each if statement so hovering over one button wouldn't change the font size of another button on the same screen
            if (isHoveringPlay) {
                g2D.setColor(Color.BLACK);
                g2D.drawRoundRect(playButton.x - 2, playButton.y - 2, playButton.width + 4, playButton.height + 5, 30, 30);
                g2D.setColor(new Color(238, 228, 218));
                g2D.fillRoundRect(playButton.x - 2, playButton.y - 2, playButton.width + 4, playButton.height + 5, 30, 30);


                //Play Button Text
                g2D.setColor(Color.BLACK);
                g2D.setFont(new Font("Monospaced", Font.BOLD, 28));
                g2D.drawString("PLAY", 269, 324);
            }
            //If the cursor is NOT on the play button, it will draw the standard version of the button
            //This mechanism is also the same for all buttons
            else {
                g2D.setColor(Color.BLACK);
                g2D.drawRoundRect(playButton.x, playButton.y, playButton.width, playButton.height, 30, 30);
                g2D.setColor(new Color(238, 228, 218));
                g2D.fillRoundRect(playButton.x, playButton.y, playButton.width, playButton.height, 30, 30);


                //Play Button Text
                g2D.setColor(Color.BLACK);
                g2D.setFont(new Font("Monospaced", Font.BOLD, 24));
                g2D.drawString("PLAY", 273, 322);
            }


            //Tutorial Button
            if (isHoveringTutorial) {
                g2D.setColor(Color.BLACK);
                g2D.drawRoundRect(tutorialButton.x - 2, tutorialButton.y - 2, tutorialButton.width + 4, tutorialButton.height + 5, 30, 30);
                g2D.setColor(new Color(237, 224, 200));
                g2D.fillRoundRect(tutorialButton.x - 2, tutorialButton.y - 2, tutorialButton.width + 4, tutorialButton.height + 5, 30, 30);

                //Tutorial Button Text
                g2D.setColor(Color.BLACK);
                g2D.setFont(new Font("Monospaced", Font.BOLD, 28));
                g2D.drawString("TUTORIAL", 236, 384);
            } else {
                g2D.setColor(Color.BLACK);
                g2D.drawRoundRect(tutorialButton.x, tutorialButton.y, tutorialButton.width, tutorialButton.height, 30, 30);
                g2D.setColor(new Color(237, 224, 200));
                g2D.fillRoundRect(tutorialButton.x, tutorialButton.y, tutorialButton.width, tutorialButton.height, 30, 30);

                //Tutorial Button Text
                g2D.setColor(Color.BLACK);
                g2D.setFont(new Font("Monospaced", Font.BOLD, 24));
                g2D.drawString("TUTORIAL", 246, 382);
            }


            //Credits Button
            if (isHoveringCredits) {
                g2D.setColor(Color.BLACK);
                g2D.drawRoundRect(creditsButton.x - 2, creditsButton.y - 2, creditsButton.width + 4, creditsButton.height + 5, 30, 30);
                g2D.setColor(new Color(227, 197, 148));
                g2D.fillRoundRect(creditsButton.x - 2, creditsButton.y - 2, creditsButton.width + 4, creditsButton.height + 5, 30, 30);

                //Credits Button Text
                g2D.setColor(Color.BLACK);
                g2D.setFont(new Font("Monospaced", Font.BOLD, 28));
                g2D.drawString("CREDITS", 243, 443);
            } else {
                g2D.setColor(Color.BLACK);
                g2D.drawRoundRect(creditsButton.x, creditsButton.y, creditsButton.width, creditsButton.height, 30, 30);
                g2D.setColor(new Color(227, 197, 148));
                g2D.fillRoundRect(creditsButton.x, creditsButton.y, creditsButton.width, creditsButton.height, 30, 30);

                //Credits Button Text
                g2D.setColor(Color.BLACK);
                g2D.setFont(new Font("Monospaced", Font.BOLD, 24));
                g2D.drawString("CREDITS", 253, 442);
            }


            //Exit Button
            if (isHoveringExit) {
                g2D.setColor(Color.BLACK);
                g2D.drawRoundRect(exitButton.x - 2, exitButton.y - 2, exitButton.width + 4, exitButton.height + 5, 30, 30);
                g2D.setColor(new Color(236, 188, 109));
                g2D.fillRoundRect(exitButton.x - 2, exitButton.y - 2, exitButton.width + 4, exitButton.height + 5, 30, 30);


                //Exit Button Text
                g2D.setColor(Color.BLACK);
                g2D.setFont(new Font("Monospaced", Font.BOLD, 28));
                g2D.drawString("EXIT", 268, 504);
            } else {
                g2D.setColor(Color.BLACK);
                g2D.drawRoundRect(exitButton.x, exitButton.y, exitButton.width, exitButton.height, 30, 30);
                g2D.setColor(new Color(236, 188, 109));
                g2D.fillRoundRect(exitButton.x, exitButton.y, exitButton.width, exitButton.height, 30, 30);


                //Exit Button Text
                g2D.setColor(Color.BLACK);
                g2D.setFont(new Font("Monospaced", Font.BOLD, 24));
                g2D.drawString("EXIT", 273, 502);
            }


            //Settings Icon
            if (isHoveringSettings) {
                g2D.drawImage(setting_icon, settingsButton.x - 2, settingsButton.y - 2, settingsButton.width + 4, settingsButton.height + 4, this);
            } else {
                g2D.drawImage(setting_icon, settingsButton.x, settingsButton.y, settingsButton.width, settingsButton.height, this);
            }
        }
        //The game playing screen
        else if (pageSwitch == 2) {

            //makes the offscreen graphics if it doesn't exist
            if (offscreenGraphics == null) {
                offscreenImage = createImage(this.getWidth(), this.getHeight());
                offscreenGraphics = (Graphics2D) offscreenImage.getGraphics();
            }

            //set the colour
            offscreenGraphics.setColor(getBackground());
            offscreenGraphics.fillRect(0, 0, getWidth(), getHeight());

            //changes the background of game page
            if (backgroundChange == 1) {
                setBackground(new Color(237, 225, 200));
            } else if (backgroundChange == 2) {
                offscreenGraphics.drawImage(chongqing, 0, 0, getWidth(), getHeight(), this); //sets image as background and scales it
            } else if (backgroundChange == 3) {
                offscreenGraphics.drawImage(newYork, 0, 0, getWidth(), getHeight(), this);
            } else if (backgroundChange == 4) {
                offscreenGraphics.drawImage(seoul, 0, 0, getWidth(), getHeight(), this);
            }


            //draws the background board
            offscreenGraphics.setColor(new Color(187, 173, 160)); //250, 248, 239
            offscreenGraphics.fillRoundRect(140, 140, 320, 320, 30, 30);

            offscreenGraphics.setColor(Color.black);
            offscreenGraphics.setStroke(new BasicStroke(1));
            offscreenGraphics.drawRoundRect(140, 140, 320, 320, 30, 30);

            //draws the outline of each board pieces
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    offscreenGraphics.setColor(new Color(250, 248, 239));
                    offscreenGraphics.fillRoundRect(j * SQUARE_SIZE + BORDER_SIZE, i * SQUARE_SIZE + TOP_OFFSET, SQUARE_SIZE, SQUARE_SIZE, 20, 20);
                    offscreenGraphics.setColor(Color.black);
                    offscreenGraphics.drawRoundRect(j * SQUARE_SIZE + BORDER_SIZE, i * SQUARE_SIZE + TOP_OFFSET, SQUARE_SIZE, SQUARE_SIZE, 20, 20);
                }
            }


            //this is for animating the actual blocks
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {

                    //first we get the value of the current piece
                    int value = oldBoard[i][j];

                    //do some log math which tells us the exponent that it has, which also corresponds to the index
                    //of the picture that it should have
                    index = (int) (Math.log(value / 3.0) / Math.log(2));

                    //this is created so that we can used clipped shapes on images
                    Graphics2D g2dTile = (Graphics2D) offscreenGraphics.create();

                    //now if the board states are different, we are currently animating all the blocks
                    //moving to one direction
                    if (oldBoard[i][j] != 0 && !Arrays.deepEquals(oldBoard, board)) {

                        //predefine x and y draw
                        int x_draw = 0;
                        int y_draw = 0;

                        //right
                        if (x > 0 && y == 0) {

                            //first we see if there is merge animation that needs to be animated
                            if (mergeStepMoved[i][j] != 0) {

                                x_draw = j * SQUARE_SIZE + BORDER_SIZE + XMove;
                                y_draw = i * SQUARE_SIZE + TOP_OFFSET + YMove;

                                //then we see if there is movement animation that needs to be animated
                            } else if (j * SQUARE_SIZE + BORDER_SIZE + XMove < j * SQUARE_SIZE + BORDER_SIZE + stepMoved[i][j] * 60) {

                                x_draw = j * SQUARE_SIZE + BORDER_SIZE + XMove;
                                y_draw = i * SQUARE_SIZE + TOP_OFFSET + YMove;

                                //otherwise, when the current block no longer needs to move, it will not move and remain static
                                //i basically do that for all other directions
                            } else {
                                x_draw = j * SQUARE_SIZE + BORDER_SIZE + stepMoved[i][j] * 60;
                                y_draw = i * SQUARE_SIZE + TOP_OFFSET;

                            }

                            //left
                        } else if (x < 0 && y == 0) {

                            if (mergeStepMoved[i][j] != 0) {

                                x_draw = j * SQUARE_SIZE + BORDER_SIZE + XMove;
                                y_draw = i * SQUARE_SIZE + TOP_OFFSET + YMove;

                            } else if (j * SQUARE_SIZE + BORDER_SIZE + XMove > j * SQUARE_SIZE + BORDER_SIZE + stepMoved[i][j] * 60) {

                                x_draw = j * SQUARE_SIZE + BORDER_SIZE + XMove;
                                y_draw = i * SQUARE_SIZE + TOP_OFFSET + YMove;

                            } else {

                                x_draw = j * SQUARE_SIZE + BORDER_SIZE + stepMoved[i][j] * 60;
                                y_draw = i * SQUARE_SIZE + TOP_OFFSET;

                            }

                            //down
                        } else if (y > 0 && x == 0) {

                            if (mergeStepMoved[i][j] != 0) {

                                x_draw = j * SQUARE_SIZE + BORDER_SIZE + XMove;
                                y_draw = i * SQUARE_SIZE + TOP_OFFSET + YMove;


                            } else if (i * SQUARE_SIZE + TOP_OFFSET + YMove < i * SQUARE_SIZE + TOP_OFFSET + stepMoved[i][j] * 60) {

                                x_draw = j * SQUARE_SIZE + BORDER_SIZE + XMove;
                                y_draw = i * SQUARE_SIZE + TOP_OFFSET + YMove;


                            } else {

                                x_draw = j * SQUARE_SIZE + BORDER_SIZE;
                                y_draw = i * SQUARE_SIZE + TOP_OFFSET + stepMoved[i][j] * 60;

                            }

                            //up
                        } else if (y < 0 && x == 0) {

                            if (mergeStepMoved[i][j] != 0) {

                                x_draw = j * SQUARE_SIZE + BORDER_SIZE + XMove;
                                y_draw = i * SQUARE_SIZE + TOP_OFFSET + YMove;

                            } else if (i * SQUARE_SIZE + TOP_OFFSET + YMove > i * SQUARE_SIZE + TOP_OFFSET + stepMoved[i][j] * 60) {

                                x_draw = j * SQUARE_SIZE + BORDER_SIZE + XMove;
                                y_draw = i * SQUARE_SIZE + TOP_OFFSET + YMove;

                            } else {

                                x_draw = j * SQUARE_SIZE + BORDER_SIZE;
                                y_draw = i * SQUARE_SIZE + TOP_OFFSET + stepMoved[i][j] * 60;

                            }
                        }

                        //after pre defining the x and y draw
                        //here is where i actually draw it
                        Shape clip = new RoundRectangle2D.Float(x_draw, y_draw, SQUARE_SIZE, SQUARE_SIZE, 20, 20);
                        g2dTile.setClip(clip);
                        g2dTile.drawImage(imageCollection[skin][index], x_draw, y_draw, SQUARE_SIZE, SQUARE_SIZE, null);
                        g2dTile.dispose();


                        //after all the animating are done, if the oldboard and board is the same, that means no changes has occured
                        //no animation or merge is needed, then the static board will be drawn here
                    } else if (Arrays.deepEquals(oldBoard, board) && oldBoard[i][j] != 0) {

                        Shape clip = new RoundRectangle2D.Float(j * SQUARE_SIZE + BORDER_SIZE, i * SQUARE_SIZE + TOP_OFFSET, SQUARE_SIZE, SQUARE_SIZE, 20, 20);
                        g2dTile.setClip(clip);
                        g2dTile.drawImage(imageCollection[skin][index], j * SQUARE_SIZE + BORDER_SIZE, i * SQUARE_SIZE + TOP_OFFSET, SQUARE_SIZE, SQUARE_SIZE, null);
                        g2dTile.dispose();

                    }
                }
            }

            g2D.drawImage(offscreenImage, 0, 0, this);

            // Back Button
            g2D.setColor(new Color(136, 55, 23));
            g2D.setStroke(new BasicStroke(2));

            if (isHoveringBack) {
                g2D.setFont(new Font("Plain", Font.BOLD, 22));
                g2D.fillRoundRect(backButton.x-2, backButton.y-2, backButton.width+8, backButton.height+5, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.drawRoundRect(backButton.x-2, backButton.y-2, backButton.width+8, backButton.height+5, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("BACK", 8, 28);
            }
            else{
                g2D.setFont(new Font("Plain", Font.BOLD, 18));
                g2D.fillRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.drawRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("BACK", 10, 27);
            }

            //new game button
            if (isHoveringReset) {
                g2D.setFont(new Font("Plain", Font.BOLD, 18));
                g2D.setColor(new Color(136, 55, 23));
                g2D.fillRoundRect(resetBoardButton.x-2, resetBoardButton.y-2, resetBoardButton.width+4, resetBoardButton.height+5, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.setStroke(new BasicStroke(2));
                g2D.drawRoundRect(resetBoardButton.x-2, resetBoardButton.y-2, resetBoardButton.width+4, resetBoardButton.height+5, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("New Board", 251, 532);
            }
            else{
                g2D.setFont(new Font("Plain", Font.BOLD, 16));
                g2D.setColor(new Color(136, 55, 23));
                g2D.fillRoundRect(resetBoardButton.x, resetBoardButton.y, resetBoardButton.width, resetBoardButton.height, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.setStroke(new BasicStroke(2));
                g2D.drawRoundRect(resetBoardButton.x, resetBoardButton.y, resetBoardButton.width, resetBoardButton.height, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("New Board", 257, 530);
            }
            //score display
            g2D.setStroke(new BasicStroke(3));
            g2D.setColor(Color.BLACK);
            g2D.drawRect(170, 55, 80, 30); //current score
            g2D.drawRect(350, 55, 80, 30); //best score
            g2D.setColor(new Color(230, 144, 123));
            g2D.fillRect(170, 55, 80, 30); //current score
            g2D.fillRect(350, 55, 80, 30); //best score


            g2D.setColor(Color.BLACK);
            g2D.drawRect(165, 25, 89, 19); //current score
            g2D.drawRect(350, 25, 80, 19); //best score
            g2D.setColor(new Color(228, 186, 174));
            g2D.fillRect(165, 25, 89, 19);
            g2D.fillRect(350, 25, 80, 19);

            //score display
            g2D.setFont(new Font("Plain", Font.BOLD, 14));
            g2D.setColor(Color.black);
            g2D.drawString("Score", 187, 40);
            g2D.setFont(new Font("Plain", Font.BOLD, 18));
            g2D.drawString("" + score, 175, 76);

            //highscore display
            g2D.setFont(new Font("Plain", Font.BOLD, 14));
            g2D.setColor(Color.black);
            g2D.drawString("High Score", 352, 40);
            g2D.setFont(new Font("Plain", Font.BOLD, 18));
            g2D.drawString("" + highScore, 355, 76);


            if (winner) {
                //this will display me an image if there is a winning piece on the board
                winner = false;
                lost = false;

                offscreenGraphics.setComposite(AlphaComposite.Clear);
                offscreenGraphics.fillRect(0, 0, 600, 600);
                offscreenGraphics.setComposite(AlphaComposite.SrcOver);

                offscreenGraphics.drawImage(you_win, 0, 0, 600, 600, null);
                g2D.drawImage(offscreenImage, 0, 0, this);

                timer4 = new Timer(4000, e -> {
                    resetBoard();
                });
                timer4.setRepeats(false);
                timer4.start();

            } else if (lost) {
                //this will display me an image if the board is full and player lost
                winner = false;
                lost = false;

                offscreenGraphics.setComposite(AlphaComposite.Clear);
                offscreenGraphics.fillRect(0, 0, 600, 600);
                offscreenGraphics.setComposite(AlphaComposite.SrcOver);

                offscreenGraphics.drawImage(you_lose, 0, 0, 600, 600, null);
                g2D.drawImage(offscreenImage, 0, 0, this);

                timer5 = new Timer(4000, e -> {
                    resetBoard();
                });
                timer5.setRepeats(false);
                timer5.start();
            }

        }
        //The start of the Tutorial pages
        else if (pageSwitch == 3) {
            setBackground(new Color(237, 225, 200));

            //draws example picture
            Image tutorialPicOne = Toolkit.getDefaultToolkit().getImage("tutorialPicOne.png");
            g2D.drawImage(tutorialPicOne, 100, 100, 400, 400, this);

            //Instructions
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


            // Next Button
            if (isHoveringNext) {
                g2D.setFont(new Font("Plain", Font.BOLD, 22));
                g2D.setColor(new Color(136, 55, 23));
                g2D.fillRoundRect(tutorialNext.x-2, tutorialNext.y-2, tutorialNext.width+4, tutorialNext.height+5, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.setStroke(new BasicStroke(2));
                g2D.drawRoundRect(tutorialNext.x-2, tutorialNext.y-2, tutorialNext.width+4, tutorialNext.height+5, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("NEXT", 533, 28);
            }
            else{
                g2D.setFont(new Font("Plain", Font.BOLD, 18));
                g2D.setColor(new Color(136, 55, 23));
                g2D.fillRoundRect(tutorialNext.x, tutorialNext.y, tutorialNext.width, tutorialNext.height, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.setStroke(new BasicStroke(2));
                g2D.drawRoundRect(tutorialNext.x, tutorialNext.y, tutorialNext.width, tutorialNext.height, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("NEXT", 535, 27);
            }


            // Back Button
            g2D.setColor(new Color(136, 55, 23));
            g2D.setStroke(new BasicStroke(2));

            if (isHoveringBack) {
                g2D.setFont(new Font("Plain", Font.BOLD, 22));
                g2D.fillRoundRect(backButton.x-2, backButton.y-2, backButton.width+8, backButton.height+5, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.drawRoundRect(backButton.x-2, backButton.y-2, backButton.width+8, backButton.height+5, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("BACK", 8, 28);
            }
            else{
                g2D.setFont(new Font("Plain", Font.BOLD, 18));
                g2D.fillRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.drawRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("BACK", 10, 27);
            }
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


            // Next Button
            if (isHoveringNext) {
                g2D.setFont(new Font("Plain", Font.BOLD, 22));
                g2D.setColor(new Color(136, 55, 23));
                g2D.fillRoundRect(tutorialNext.x-2, tutorialNext.y-2, tutorialNext.width+4, tutorialNext.height+5, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.setStroke(new BasicStroke(2));
                g2D.drawRoundRect(tutorialNext.x-2, tutorialNext.y-2, tutorialNext.width+4, tutorialNext.height+5, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("NEXT", 533, 28);
            }
            else{
                g2D.setFont(new Font("Plain", Font.BOLD, 18));
                g2D.setColor(new Color(136, 55, 23));
                g2D.fillRoundRect(tutorialNext.x, tutorialNext.y, tutorialNext.width, tutorialNext.height, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.setStroke(new BasicStroke(2));
                g2D.drawRoundRect(tutorialNext.x, tutorialNext.y, tutorialNext.width, tutorialNext.height, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("NEXT", 535, 27);
            }

            // Back Button
            g2D.setColor(new Color(136, 55, 23));
            g2D.setStroke(new BasicStroke(2));

            if (isHoveringBack) {
                g2D.setFont(new Font("Plain", Font.BOLD, 22));
                g2D.fillRoundRect(backButton.x-2, backButton.y-2, backButton.width+8, backButton.height+5, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.drawRoundRect(backButton.x-2, backButton.y-2, backButton.width+8, backButton.height+5, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("BACK", 8, 28);
            }
            else{
                g2D.setFont(new Font("Plain", Font.BOLD, 18));
                g2D.fillRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.drawRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("BACK", 10, 27);
            }


        } else if (pageSwitch == 5) {
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


            // Next Button
            if (isHoveringNext) {
                g2D.setFont(new Font("Plain", Font.BOLD, 22));
                g2D.setColor(new Color(136, 55, 23));
                g2D.fillRoundRect(tutorialNext.x-2, tutorialNext.y-2, tutorialNext.width+4, tutorialNext.height+5, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.setStroke(new BasicStroke(2));
                g2D.drawRoundRect(tutorialNext.x-2, tutorialNext.y-2, tutorialNext.width+4, tutorialNext.height+5, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("NEXT", 533, 28);
            }
            else{
                g2D.setFont(new Font("Plain", Font.BOLD, 18));
                g2D.setColor(new Color(136, 55, 23));
                g2D.fillRoundRect(tutorialNext.x, tutorialNext.y, tutorialNext.width, tutorialNext.height, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.setStroke(new BasicStroke(2));
                g2D.drawRoundRect(tutorialNext.x, tutorialNext.y, tutorialNext.width, tutorialNext.height, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("NEXT", 535, 27);
            }


            // Back Button
            g2D.setColor(new Color(136, 55, 23));
            g2D.setStroke(new BasicStroke(2));

            if (isHoveringBack) {
                g2D.setFont(new Font("Plain", Font.BOLD, 22));
                g2D.fillRoundRect(backButton.x-2, backButton.y-2, backButton.width+8, backButton.height+5, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.drawRoundRect(backButton.x-2, backButton.y-2, backButton.width+8, backButton.height+5, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("BACK", 8, 28);
            }
            else{
                g2D.setFont(new Font("Plain", Font.BOLD, 18));
                g2D.fillRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.drawRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("BACK", 10, 27);
            }

        } else if (pageSwitch == 6) {
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


            // Next Button
            if (isHoveringNext) {
                g2D.setFont(new Font("Plain", Font.BOLD, 22));
                g2D.setColor(new Color(136, 55, 23));
                g2D.fillRoundRect(tutorialNext.x-2, tutorialNext.y-2, tutorialNext.width+4, tutorialNext.height+5, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.setStroke(new BasicStroke(2));
                g2D.drawRoundRect(tutorialNext.x-2, tutorialNext.y-2, tutorialNext.width+4, tutorialNext.height+5, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("NEXT", 533, 28);
            }
            else{
                g2D.setFont(new Font("Plain", Font.BOLD, 18));
                g2D.setColor(new Color(136, 55, 23));
                g2D.fillRoundRect(tutorialNext.x, tutorialNext.y, tutorialNext.width, tutorialNext.height, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.setStroke(new BasicStroke(2));
                g2D.drawRoundRect(tutorialNext.x, tutorialNext.y, tutorialNext.width, tutorialNext.height, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("NEXT", 535, 27);
            }


            // Back Button
            g2D.setColor(new Color(136, 55, 23));
            g2D.setStroke(new BasicStroke(2));

            if (isHoveringBack) {
                g2D.setFont(new Font("Plain", Font.BOLD, 22));
                g2D.fillRoundRect(backButton.x-2, backButton.y-2, backButton.width+8, backButton.height+5, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.drawRoundRect(backButton.x-2, backButton.y-2, backButton.width+8, backButton.height+5, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("BACK", 8, 28);
            }
            else{
                g2D.setFont(new Font("Plain", Font.BOLD, 18));
                g2D.fillRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.drawRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("BACK", 10, 27);
            }

        } else if (pageSwitch == 7) {
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


            // Next Button
            if (isHoveringNext) {
                g2D.setFont(new Font("Plain", Font.BOLD, 22));
                g2D.setColor(new Color(136, 55, 23));
                g2D.fillRoundRect(tutorialNext.x-2, tutorialNext.y-2, tutorialNext.width+4, tutorialNext.height+5, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.setStroke(new BasicStroke(2));
                g2D.drawRoundRect(tutorialNext.x-2, tutorialNext.y-2, tutorialNext.width+4, tutorialNext.height+5, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("NEXT", 533, 28);
            }
            else{
                g2D.setFont(new Font("Plain", Font.BOLD, 18));
                g2D.setColor(new Color(136, 55, 23));
                g2D.fillRoundRect(tutorialNext.x, tutorialNext.y, tutorialNext.width, tutorialNext.height, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.setStroke(new BasicStroke(2));
                g2D.drawRoundRect(tutorialNext.x, tutorialNext.y, tutorialNext.width, tutorialNext.height, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("NEXT", 535, 27);
            }


            // Back Button
            g2D.setColor(new Color(136, 55, 23));
            g2D.setStroke(new BasicStroke(2));

            if (isHoveringBack) {
                g2D.setFont(new Font("Plain", Font.BOLD, 22));
                g2D.fillRoundRect(backButton.x-2, backButton.y-2, backButton.width+8, backButton.height+5, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.drawRoundRect(backButton.x-2, backButton.y-2, backButton.width+8, backButton.height+5, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("BACK", 8, 28);
            }
            else{
                g2D.setFont(new Font("Plain", Font.BOLD, 18));
                g2D.fillRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.drawRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("BACK", 10, 27);
            }

        } else if (pageSwitch == 8) {
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


            // Next Button
            if (isHoveringNext) {
                g2D.setFont(new Font("Plain", Font.BOLD, 22));
                g2D.setColor(new Color(136, 55, 23));
                g2D.fillRoundRect(tutorialNext.x-2, tutorialNext.y-2, tutorialNext.width+4, tutorialNext.height+5, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.setStroke(new BasicStroke(2));
                g2D.drawRoundRect(tutorialNext.x-2, tutorialNext.y-2, tutorialNext.width+4, tutorialNext.height+5, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("NEXT", 533, 28);
            }
            else{
                g2D.setFont(new Font("Plain", Font.BOLD, 18));
                g2D.setColor(new Color(136, 55, 23));
                g2D.fillRoundRect(tutorialNext.x, tutorialNext.y, tutorialNext.width, tutorialNext.height, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.setStroke(new BasicStroke(2));
                g2D.drawRoundRect(tutorialNext.x, tutorialNext.y, tutorialNext.width, tutorialNext.height, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("NEXT", 535, 27);
            }

            // Back Button
            g2D.setColor(new Color(136, 55, 23));
            g2D.setStroke(new BasicStroke(2));

            if (isHoveringBack) {
                g2D.setFont(new Font("Plain", Font.BOLD, 22));
                g2D.fillRoundRect(backButton.x-2, backButton.y-2, backButton.width+8, backButton.height+5, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.drawRoundRect(backButton.x-2, backButton.y-2, backButton.width+8, backButton.height+5, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("BACK", 8, 28);
            }
            else{
                g2D.setFont(new Font("Plain", Font.BOLD, 18));
                g2D.fillRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.drawRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("BACK", 10, 27);
            }

        } else if (pageSwitch == 9) {
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


            // Next Button
            if (isHoveringNext) {
                g2D.setFont(new Font("Plain", Font.BOLD, 22));
                g2D.setColor(new Color(136, 55, 23));
                g2D.fillRoundRect(tutorialNext.x-2, tutorialNext.y-2, tutorialNext.width+4, tutorialNext.height+5, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.setStroke(new BasicStroke(2));
                g2D.drawRoundRect(tutorialNext.x-2, tutorialNext.y-2, tutorialNext.width+4, tutorialNext.height+5, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("NEXT", 533, 28);
            }
            else{
                g2D.setFont(new Font("Plain", Font.BOLD, 18));
                g2D.setColor(new Color(136, 55, 23));
                g2D.fillRoundRect(tutorialNext.x, tutorialNext.y, tutorialNext.width, tutorialNext.height, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.setStroke(new BasicStroke(2));
                g2D.drawRoundRect(tutorialNext.x, tutorialNext.y, tutorialNext.width, tutorialNext.height, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("NEXT", 535, 27);
            }

            // Back Button
            g2D.setColor(new Color(136, 55, 23));
            g2D.setStroke(new BasicStroke(2));

            if (isHoveringBack) {
                g2D.setFont(new Font("Plain", Font.BOLD, 22));
                g2D.fillRoundRect(backButton.x-2, backButton.y-2, backButton.width+8, backButton.height+5, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.drawRoundRect(backButton.x-2, backButton.y-2, backButton.width+8, backButton.height+5, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("BACK", 8, 28);
            }
            else{
                g2D.setFont(new Font("Plain", Font.BOLD, 18));
                g2D.fillRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.drawRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("BACK", 10, 27);
            }

        } else if (pageSwitch == 10) {
            Image tutorialPicOne = Toolkit.getDefaultToolkit().getImage("tutorialPicFour.png");
            g2D.drawImage(tutorialPicOne, 100, 100, 400, 400, this);


            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(6));
            g2D.drawRoundRect(30, 500, 540, 60, 30, 30);
            g2D.setColor(new Color(253, 172, 137));
            g2D.fillRoundRect(30, 500, 540, 60, 30, 30);


            //score display
            g2D.setStroke(new BasicStroke(3));
            g2D.setColor(Color.BLACK);
            g2D.drawRect(170, 55, 80, 30); //current score
            g2D.drawRect(350, 55, 80, 30); //best score
            g2D.setColor(new Color(230, 144, 123));
            g2D.fillRect(170, 55, 80, 30); //current score
            g2D.fillRect(350, 55, 80, 30); //best score


            g2D.setColor(Color.BLACK);
            g2D.drawRect(165, 25, 89, 19); //current score
            g2D.drawRect(350, 25, 80, 19); //best score
            g2D.setColor(new Color(228, 186, 174));
            g2D.fillRect(165, 25, 89, 19);
            g2D.fillRect(350, 25, 80, 19);

            //score display
            g2D.setFont(new Font("Plain", Font.BOLD, 14));
            g2D.setColor(Color.black);
            g2D.drawString("Score", 187, 40);
            g2D.setFont(new Font("Plain", Font.BOLD, 18));
            g2D.drawString("12", 175, 76);

            //highscore display
            g2D.setFont(new Font("Plain", Font.BOLD, 14));
            g2D.setColor(Color.black);
            g2D.drawString("High Score", 352, 40);
            g2D.setFont(new Font("Plain", Font.BOLD, 18));
            g2D.drawString("12", 355, 76);


            g2D.setStroke(new BasicStroke(6));
            g2D.setFont(new Font("Plain", Font.BOLD, 18));
            g2D.setColor(Color.BLACK);
            g2D.drawString("Your current score and best score will be shown at the top", 39, 537);


            // Next Button
            if (isHoveringNext) {
                g2D.setFont(new Font("Plain", Font.BOLD, 22));
                g2D.setColor(new Color(136, 55, 23));
                g2D.fillRoundRect(tutorialNext.x-2, tutorialNext.y-2, tutorialNext.width+4, tutorialNext.height+5, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.setStroke(new BasicStroke(2));
                g2D.drawRoundRect(tutorialNext.x-2, tutorialNext.y-2, tutorialNext.width+4, tutorialNext.height+5, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("NEXT", 533, 28);
            }
            else{
                g2D.setFont(new Font("Plain", Font.BOLD, 18));
                g2D.setColor(new Color(136, 55, 23));
                g2D.fillRoundRect(tutorialNext.x, tutorialNext.y, tutorialNext.width, tutorialNext.height, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.setStroke(new BasicStroke(2));
                g2D.drawRoundRect(tutorialNext.x, tutorialNext.y, tutorialNext.width, tutorialNext.height, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("NEXT", 535, 27);
            }

            // Back Button
            g2D.setColor(new Color(136, 55, 23));
            g2D.setStroke(new BasicStroke(2));

            if (isHoveringBack) {
                g2D.setFont(new Font("Plain", Font.BOLD, 22));
                g2D.fillRoundRect(backButton.x-2, backButton.y-2, backButton.width+8, backButton.height+5, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.drawRoundRect(backButton.x-2, backButton.y-2, backButton.width+8, backButton.height+5, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("BACK", 8, 28);
            }
            else{
                g2D.setFont(new Font("Plain", Font.BOLD, 18));
                g2D.fillRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.drawRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("BACK", 10, 27);
            }

        } else if (pageSwitch == 11) {
            Image tutorialPicOne = Toolkit.getDefaultToolkit().getImage("tutorialPicFive.png");
            g2D.drawImage(tutorialPicOne, 100, 100, 400, 400, this);


            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(6));
            g2D.drawRoundRect(140, 100, 320, 70, 30, 30);
            g2D.setColor(new Color(136, 55, 23));
            g2D.fillRoundRect(140, 100, 320, 70, 30, 30);


            g2D.setFont(new Font("Plain", Font.BOLD, 18));
            g2D.setColor(new Color(239, 211, 204));
            g2D.drawString("Create a block of 3072 to win!!!", 158, 140);


            //Home Button
            if (isHoveringHome) {
                g2D.setFont(new Font("Plain", Font.BOLD, 22));
                g2D.drawRoundRect(tutorialHome.x-2, tutorialHome.y-2, tutorialHome.width+4, tutorialHome.height+5, 30, 30);
                g2D.setColor(new Color(152, 108, 93));
                g2D.fillRoundRect(tutorialHome.x-2, tutorialHome.y-2, tutorialHome.width+4, tutorialHome.height+5, 30, 30);
                g2D.setColor(new Color(255, 166, 143));
                g2D.drawString("HOME", 267, 568);
            }
            else{
                g2D.setFont(new Font("Plain", Font.BOLD, 18));
                g2D.drawRoundRect(tutorialHome.x, tutorialHome.y, tutorialHome.width, tutorialHome.height, 30, 30);
                g2D.setColor(new Color(152, 108, 93));
                g2D.fillRoundRect(tutorialHome.x, tutorialHome.y, tutorialHome.width, tutorialHome.height, 30, 30);
                g2D.setColor(new Color(255, 166, 143));
                g2D.drawString("HOME", 271, 566);
            }

            // Back Button
            g2D.setColor(new Color(136, 55, 23));
            g2D.setStroke(new BasicStroke(2));

            if (isHoveringBack) {
                g2D.setFont(new Font("Plain", Font.BOLD, 22));
                g2D.fillRoundRect(backButton.x-2, backButton.y-2, backButton.width+8, backButton.height+5, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.drawRoundRect(backButton.x-2, backButton.y-2, backButton.width+8, backButton.height+5, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("BACK", 8, 28);
            }
            else{
                g2D.setFont(new Font("Plain", Font.BOLD, 18));
                g2D.fillRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.drawRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("BACK", 10, 27);
            }


        }
        //Credits
        else if (pageSwitch == 12) {
            setBackground(new Color(241, 203, 136));
            g2D.setFont(new Font("Plain", Font.BOLD, 24));
            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(6));
            g2D.drawString("Made by", 250, 100);


            g2D.setFont(new Font("Plain", Font.BOLD, 20));
            g2D.drawString("Will", 150, 500);
            g2D.drawString("Aiden", 400, 500);


            //our faces
            g2D.drawImage(aiden, 315, 150, 250, 300, this);
            g2D.drawImage(will, 35, 150, 250, 300, this);
            //face picture borders
            g2D.setStroke(new BasicStroke(4));
            g2D.drawRect(315, 150, 250, 300);
            g2D.drawRect(35, 150, 250, 300);

            // Back Button
            g2D.setColor(new Color(136, 55, 23));
            g2D.setStroke(new BasicStroke(2));

            if (isHoveringBack) {
                g2D.setFont(new Font("Plain", Font.BOLD, 22));
                g2D.fillRoundRect(backButton.x-2, backButton.y-2, backButton.width+8, backButton.height+5, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.drawRoundRect(backButton.x-2, backButton.y-2, backButton.width+8, backButton.height+5, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("BACK", 8, 28);
            }
            else{
                g2D.setFont(new Font("Plain", Font.BOLD, 18));
                g2D.fillRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.drawRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("BACK", 10, 27);
            }

        }
        //Settings Screen
        else if (pageSwitch == 13) {
            setBackground(new Color(184, 163, 139));


            //Settings Box
            g2D.setStroke(new BasicStroke(5));
            g2D.drawRoundRect(90, 100, 400, 450, 50, 50);
            g2D.setColor(new Color(142, 110, 101));
            g2D.fillRoundRect(90, 100, 400, 450, 50, 50);


            //Music Button
            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(3));
            g2D.drawRoundRect(140, 150, 300, 80, 50, 50);
            g2D.setColor(new Color(221, 174, 162));
            g2D.fillRoundRect(140, 150, 300, 80, 50, 50);


            //SFX Button
            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(3));
            g2D.drawRoundRect(140, 280, 300, 80, 50, 50);
            g2D.setColor(new Color(221, 174, 162));
            g2D.fillRoundRect(140, 280, 300, 80, 50, 50);


            //Backgrounds Button
            g2D.setColor(Color.BLACK);
            g2D.setStroke(new BasicStroke(3));

            if (isHoveringBackgrounds) {
                g2D.drawRoundRect(138, 408, 304, 85, 50, 50);
                g2D.setColor(new Color(188, 119, 99));
                g2D.fillRoundRect(138, 408, 304, 85, 50, 50);
                g2D.setFont(new Font("Plain", Font.BOLD, 22));
                g2D.setColor(Color.BLACK);
                g2D.drawString("BACKGROUNDS", 206, 456);
            } else {
                g2D.drawRoundRect(140, 410, 300, 80, 50, 50);
                g2D.setColor(new Color(188, 119, 99));
                g2D.fillRoundRect(140, 410, 300, 80, 50, 50);
                g2D.setFont(new Font("Plain", Font.BOLD, 18));
                g2D.setColor(Color.BLACK);
                g2D.drawString("BACKGROUNDS", 213, 455);
            }

            //Since both music and SFX button have essentially 2 versions (ON & OFF), there needs to be 2 hovering checks for each button
            if (musicButton == 1) {
                //Music ON Texts
                if (isHoveringMusic) {
                    g2D.drawRoundRect(musicOn.x - 2, musicOn.y - 2, musicOn.width + 4, musicOn.height + 5, 20, 20); //MUSIC
                    g2D.setColor(new Color(29, 225, 41));
                    g2D.fillRoundRect(musicOn.x - 2, musicOn.y - 2, musicOn.width + 4, musicOn.height + 5, 20, 20); //MUSIC
                    g2D.setFont(new Font("Plain", Font.BOLD, 22));
                    g2D.setColor(Color.BLACK);
                    g2D.drawString("ON", 323, 198);
                } else {
                    g2D.drawRoundRect(musicOn.x, musicOn.y, musicOn.width, musicOn.height, 20, 20); //MUSIC
                    g2D.setColor(new Color(29, 225, 41));
                    g2D.fillRoundRect(musicOn.x, musicOn.y, musicOn.width, musicOn.height, 20, 20); //MUSIC
                    g2D.setFont(new Font("Plain", Font.BOLD, 18));
                    g2D.setColor(Color.BLACK);
                    g2D.drawString("ON", 325, 197);
                }
            } else if (musicButton == -1) {
                //Music OFF Texts
                if (isHoveringMusic) {
                    g2D.drawRoundRect(musicOn.x - 2, musicOn.y - 2, musicOn.width + 4, musicOn.height + 5, 20, 20); //MUSIC
                    g2D.setColor(new Color(237, 69, 69));
                    g2D.fillRoundRect(musicOn.x - 2, musicOn.y - 2, musicOn.width + 4, musicOn.height + 5, 20, 20); //MUSIC
                    g2D.setFont(new Font("Plain", Font.BOLD, 22));
                    g2D.setColor(Color.BLACK);
                    g2D.drawString("OFF", 319, 198);
                } else {
                    g2D.drawRoundRect(musicOn.x, musicOn.y, musicOn.width, musicOn.height, 20, 20); //MUSIC
                    g2D.setColor(new Color(237, 69, 69));
                    g2D.fillRoundRect(musicOn.x, musicOn.y, musicOn.width, musicOn.height, 20, 20); //MUSIC
                    g2D.setFont(new Font("Plain", Font.BOLD, 18));
                    g2D.setColor(Color.BLACK);
                    g2D.drawString("OFF", 322, 197);
                }
            }
            if (SFXButton == 1) {
                // SFX ON Texts
                if (isHoveringSFX) {
                    g2D.drawRoundRect(SFXOn.x - 2, SFXOn.y - 2, SFXOn.width + 4, SFXOn.height + 5, 20, 20); //SFX
                    g2D.setColor(new Color(29, 225, 41));
                    g2D.fillRoundRect(SFXOn.x - 2, SFXOn.y - 2, SFXOn.width + 4, SFXOn.height + 5, 20, 20); //SFX
                    g2D.setFont(new Font("Plain", Font.BOLD, 22));
                    g2D.setColor(Color.BLACK);
                    g2D.drawString("ON", 323, 328);
                } else {
                    g2D.drawRoundRect(SFXOn.x, SFXOn.y, SFXOn.width, SFXOn.height, 20, 20); //SFX
                    g2D.setColor(new Color(29, 225, 41));
                    g2D.fillRoundRect(SFXOn.x, SFXOn.y, SFXOn.width, SFXOn.height, 20, 20); //SFX
                    g2D.setFont(new Font("Plain", Font.BOLD, 18));
                    g2D.setColor(Color.BLACK);
                    g2D.drawString("ON", 325, 327);
                }

            } else if (SFXButton == -1) {
                // SFX OFF Texts
                if (isHoveringSFX) {
                    g2D.drawRoundRect(SFXOn.x - 2, SFXOn.y - 2, SFXOn.width + 4, SFXOn.height + 5, 20, 20); //SFX
                    g2D.setColor(new Color(237, 69, 69));
                    g2D.fillRoundRect(SFXOn.x - 2, SFXOn.y - 2, SFXOn.width + 4, SFXOn.height + 5, 20, 20); //SFX
                    g2D.setFont(new Font("Plain", Font.BOLD, 22));
                    g2D.setColor(Color.BLACK);
                    g2D.drawString("OFF", 320, 328);
                } else {
                    g2D.drawRoundRect(SFXOn.x, SFXOn.y, SFXOn.width, SFXOn.height, 20, 20); //SFX
                    g2D.setColor(new Color(237, 69, 69));
                    g2D.fillRoundRect(SFXOn.x, SFXOn.y, SFXOn.width, SFXOn.height, 20, 20); //SFX
                    g2D.setFont(new Font("Plain", Font.BOLD, 18));
                    g2D.setColor(Color.BLACK);
                    g2D.drawString("OFF", 322, 327);
                }

            }
            //Settings Texts
            g2D.setColor(Color.BLACK);
            g2D.setFont(new Font("Plain", Font.BOLD, 20));
            g2D.drawString("MUSIC", 190, 197);
            g2D.drawString("SFX", 200, 327);


            // Back Button
            g2D.setColor(new Color(136, 55, 23));
            g2D.setStroke(new BasicStroke(2));

            if (isHoveringBack) {
                g2D.setFont(new Font("Plain", Font.BOLD, 22));
                g2D.fillRoundRect(backButton.x-2, backButton.y-2, backButton.width+8, backButton.height+5, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.drawRoundRect(backButton.x-2, backButton.y-2, backButton.width+8, backButton.height+5, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("BACK", 8, 28);
            }
            else{
                g2D.setFont(new Font("Plain", Font.BOLD, 18));
                g2D.fillRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.drawRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("BACK", 10, 27);
            }

        }
        //Backgrounds Customize Screen
        else if (pageSwitch == 14) {

            setBackground(new Color(189, 211, 116));
            g2D.setFont(new Font("Serif", Font.BOLD, 40));
            g2D.setColor(Color.BLACK);
            g2D.drawString("CITIES", 230, 110);


            g2D.setFont(new Font("Plain", Font.BOLD, 18));


            //image borders
            Rectangle chongqingBorder = new Rectangle(26, 216, 168, 208);
            Rectangle newYorkBorder = new Rectangle(216, 216, 168, 208);
            Rectangle seoulBorder = new Rectangle(406, 216, 168, 208);

            g2D.setColor(Color.BLACK);
            g2D.fill(chongqingBorder);
            g2D.fill(newYorkBorder);
            g2D.fill(seoulBorder);

            //changes the border outline if a background is chosen
            //mechanism behind backgroundChange is the same as pageSwitch
            if (backgroundChange == 2) {
                g2D.setColor(new Color(244, 114, 76));
                g2D.fill(chongqingBorder);
            } else if (backgroundChange == 3) {
                g2D.setColor(new Color(244, 114, 76));
                g2D.fill(newYorkBorder);
            } else if (backgroundChange == 4) {
                g2D.setColor(new Color(244, 114, 76));
                g2D.fill(seoulBorder);
            }

            g2D.setColor(Color.BLACK);

            g2D.drawString("Chongqing", 60, 455);
            g2D.drawString("New York", 260, 455);
            g2D.drawString("Seoul", 465, 455);

            g2D.drawImage(chongqing, 30, 220, 160, 200, this);
            g2D.drawImage(newYork, 220, 220, 160, 200, this);
            g2D.drawImage(seoul, 410, 220, 160, 200, this);


            // Back Button
            g2D.setColor(new Color(136, 55, 23));
            g2D.setStroke(new BasicStroke(2));

            if (isHoveringBack) {
                g2D.setFont(new Font("Plain", Font.BOLD, 22));
                g2D.fillRoundRect(backButton.x-2, backButton.y-2, backButton.width+8, backButton.height+5, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.drawRoundRect(backButton.x-2, backButton.y-2, backButton.width+8, backButton.height+5, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("BACK", 8, 28);
            }
            else{
                g2D.setFont(new Font("Plain", Font.BOLD, 18));
                g2D.fillRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.drawRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("BACK", 10, 27);
            }


            //Reset Button
            if (isHoveringReset) {
                g2D.setFont(new Font("Plain", Font.BOLD, 22));
                g2D.setColor(new Color(136, 55, 23));
                g2D.fillRoundRect(resetButton.x-2, resetButton.y-2, resetButton.width+4, resetButton.height+5, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.setStroke(new BasicStroke(2));
                g2D.drawRoundRect(resetButton.x-2, resetButton.y-2, resetButton.width+4, resetButton.height+5, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("RESET", 263, 543);
            }
            else{
                g2D.setFont(new Font("Plain", Font.BOLD, 18));
                g2D.setColor(new Color(136, 55, 23));
                g2D.fillRoundRect(resetButton.x, resetButton.y, resetButton.width, resetButton.height, 25, 25);
                g2D.setColor(Color.BLACK);
                g2D.setStroke(new BasicStroke(2));
                g2D.drawRoundRect(resetButton.x, resetButton.y, resetButton.width, resetButton.height, 25, 25);
                g2D.setColor(new Color(239, 211, 204));
                g2D.drawString("RESET", 270, 542);
            }
        }
    }


    public void actionPerformed(ActionEvent event) {
    }


    // MouseListener methods
    //Description: Checks whether buttons were clicked, and if so, brings the user to the screen corresponding to the button they pressed
    //Parameters: The object MouseEvent e that represents mouse-related events (like clicking, releasing, etc)
    //Return: Nothing
    public void mouseClicked(MouseEvent e) {

        Point clicked = e.getPoint();
        //HomePage Buttons
        //Play
        //Each button needs to have the condition that whichever screen they are drawn in, that screen must be the screen shown
        //Or else, the user could for example be on the tutorial screen and click the center and trigger the play button
        if (pageSwitch == 1) {
            //this mechanism is used for all buttons: if the screen it's drawn on is displayed, and it's pressed, it will draw the desired screen or perform the desired action
            if (playButton.contains(clicked)) { //if the play button is pressed, the play screen will be drawn
                pageSwitch = 2;
                repaint();
            }
        }
        //Tutorial
        if (pageSwitch == 1) {
            if (tutorialButton.contains(clicked)) { //if the tutorial button is pressed, the tutorial first page is drawn
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
            if (exitButton.contains(clicked)) {//if exit button is pressed, close the window
                System.exit(0);
            }
        }
        //Game
        else if (pageSwitch == 2) {
            if (backButton.contains(clicked)) {
                pageSwitch = 1;
                repaint();
            } else if (resetBoardButton.contains(clicked)) {
                pageSwitch = 2;
                resetBoard(); //calls resetBoard method and restarts the game
            }
        }


        //Tutorial Buttons
        //Pressing next brings user to the next instruction page
        //Pressing back brings user to the previous page
        //the instruction/tutorial pages increment by 1 for pageSwitch
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
        } else if (pageSwitch == 13) {
            if (backButton.contains(clicked)) {
                pageSwitch = 1;
                repaint();
            }
            else if (musicOn.contains(clicked)) {
                //if the music button is pressed and currently ON (1), turn it OFF (-1) and redraw the button to indicate it's OFF
                if (musicButton == 1) {
                    musicButton = -1;
                    music.stop(); //stops the music
                    repaint();
                }
                //if the music button is pressed and currently OFF (-1), turn it ON (1) and redraw the button to indicate it's ON
                else if (musicButton == -1) {
                    musicButton = 1;
                    music.start(); //turns on music
                    music.loop(Clip.LOOP_CONTINUOUSLY); //loops forever
                    repaint();
                }
            }
            //Basically same thing as musicButton
            else if (SFXOn.contains(clicked)) {
                if (SFXButton == 1) {
                    SFXButton = -1;
                    repaint();
                } else if (SFXButton == -1) {
                    SFXButton = 1;
                    repaint();
                }
            } else if (backgroundButton.contains(clicked)) {
                pageSwitch = 14;
                repaint();
            }
        } else if (pageSwitch == 14) {
            if (backButton.contains(clicked)) {
                pageSwitch = 13;
                repaint();
            }
            //if the option is selected, the background and skin of game gets updated
            else if (chongqingButton.contains(clicked)) {
                backgroundChange = 2;
                skin = 0; //changes skin to numbers
                repaint();
            } else if (newYorkButton.contains(clicked)) {
                backgroundChange = 3;
                skin = 1; //changes skin to colours
                repaint();
            } else if (seoulButton.contains(clicked)) {
                backgroundChange = 4;
                skin = 2; //changes skin to fruits
                repaint();
            } else if (resetButton.contains(clicked)) {
                backgroundChange = 1;
                skin = 0;
                repaint();
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

    //Description: If the cursor hovers over any buttons, this method will notify the program through a boolean variable and result in an "animation"
    //Parameters: The object MouseEvent e that represents mouse-related events (like clicking, releasing, etc)
    //Return: Nothing
    public void mouseMoved(MouseEvent e) {
        //retrieves coordinates of cursor
        int mouseX = e.getX();
        int mouseY = e.getY();


        // Checks if mouse is on the button
        //True means it is on the button, false means it is not
        //We don't have to check for pageSwitch == 1 because even if the cursor is over the button, the button isn't drawn so nothing shows
        //However, for efficiency and speed this might help
        if (pageSwitch == 1) {
            //Play
            if (playButton.contains(mouseX, mouseY)) {
                isHoveringPlay = true;
                repaint();
            } else {
                isHoveringPlay = false;
                repaint();
            }
            //Tutorial
            if (tutorialButton.contains(mouseX, mouseY)) {
                isHoveringTutorial = true;
                repaint();
            } else {
                isHoveringTutorial = false;
                repaint();
            }

            //Credits
            if (creditsButton.contains(mouseX, mouseY)) {
                isHoveringCredits = true;
                repaint();
            } else {
                isHoveringCredits = false;
                repaint();
            }
            //Settings
            if (settingsButton.contains(mouseX, mouseY)) {
                isHoveringSettings = true;
                repaint();
            } else {
                isHoveringSettings = false;
                repaint();
            }
            //Exit
            if (exitButton.contains(mouseX, mouseY)) {
                isHoveringExit = true;
                repaint();
            } else {
                isHoveringExit = false;
                repaint();
            }
        }

        //Game Reset Button
        if (pageSwitch == 2) {
            if (resetBoardButton.contains(mouseX, mouseY)) {
                isHoveringReset = true;
                repaint();
            } else {
                isHoveringReset = false;
                repaint();
            }
        }
        //Background Reset Button
        if(pageSwitch == 14){
            if (resetButton.contains(mouseX, mouseY)) {
                isHoveringReset = true;
                repaint();
            } else {
                isHoveringReset = false;
                repaint();
            }
        }

        //Back Buttons
        //applies to every back button
        if (backButton.contains(mouseX, mouseY)) {
            isHoveringBack = true;
            repaint();
        } else {
            isHoveringBack = false;
            repaint();
        }

        //Next Button
        //applies to every next button
        if (tutorialNext.contains(mouseX, mouseY)) {
            isHoveringNext = true;
            repaint();
        } else {
            isHoveringNext = false;
            repaint();
        }

        //Tutorial Home Button
        if (pageSwitch == 11){
            if (tutorialHome.contains(mouseX, mouseY)) {
                isHoveringHome = true;
                repaint();
            } else {
                isHoveringHome = false;
                repaint();
            }
        }

        //Setting page buttons
        if (pageSwitch == 13) {
            if (musicOn.contains(mouseX, mouseY)) {
                isHoveringMusic = true;
                repaint();
            } else {
                isHoveringMusic = false;
                repaint();
            }

            if (SFXOn.contains(mouseX, mouseY)) {
                isHoveringSFX = true;
                repaint();
            } else {
                isHoveringSFX = false;
                repaint();
            }

            if (backgroundButton.contains(mouseX, mouseY)) {
                isHoveringBackgrounds = true;
                repaint();
            } else {
                isHoveringBackgrounds = false;
                repaint();
            }
        }
    }


    // KeyListener methods
    public void keyPressed(KeyEvent kp) {

        //this method will only activate if we are on the correct page
        if (pageSwitch == 2) {

            //we first store the current time of the system
            long currentTime = System.currentTimeMillis();

            //if the current time is not long enough from the last input time
            //you can't press another key
            if (currentTime - lastInputTime >= cooldown) {
                key = kp.getKeyCode();

                //now according to the direction
                //x and y will be assigned values to help animate the blocks
                if (key == KeyEvent.VK_RIGHT && !anti_double_animation) {
                    x = 4;
                    y = 0;
                    startMoveAnimation();
                } else if (key == KeyEvent.VK_LEFT && !anti_double_animation) {
                    x = -4;
                    y = 0;
                    startMoveAnimation();
                } else if (key == KeyEvent.VK_DOWN && !anti_double_animation) {
                    x = 0;
                    y = 4;
                    startMoveAnimation();
                } else if (key == KeyEvent.VK_UP && !anti_double_animation) {
                    x = 0;
                    y = -4;
                    startMoveAnimation();
                }

                lastInputTime = currentTime;

            }
        }
    }

    //description: this method helps to animate the blocks movement only! no merge animation
    //parameter: nothing
    //return: nothing
    public void startMoveAnimation() {

        //we set both biggest step to 0 and animation tracker each time for the changing board state
        //these were used to calculate the biggest number of animation pixels that needed to be aniamted
        biggestStep = 0;
        animationTracker = 0;

        //this is also set to true to prevent double animation
        anti_double_animation = true;

        //we first copy the board to save to old board state
        copyBoard();

        //we then update the board to move all the blocks in a certain direction
        Update_board(board, x, y);

        //then we calculate how many steps each block has moved
        how_many_steps_moved();

        //if the board still equal though, it will prevent new blocks from spawning
        if (Arrays.deepEquals(oldBoard, board)) {
            blockSpawning = false;
            didMove = false;
        } else {
            blockSpawning = true;
            didMove = true;
        }

        //a timer is set up here for the smooth animations
        timer = new Timer(3, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

                //Xmove and YMove will keep chaning as long as the maximum number of animation pixels hasn't been reached
                if (x != 0 && Math.abs(XMove) < Math.abs(animated_Steps()) || y != 0 && Math.abs(YMove) < Math.abs(animated_Steps())) {
                    XMove += x;
                    YMove += y;
                    repaint();
                } else {

                    timer.stop();
                    XMove = 0;
                    YMove = 0;

                    //after we finishing the movement animation we copy board again
                    copyBoard();
                    repaint();

                    //update the board again with merge
                    merge();

                    //now we double check again, if the board either moved or merged
                    //this will dedicate block spawning
                    if (didMove) {
                        blockSpawning = true;
                    } else if (Arrays.deepEquals(oldBoard, board)) {
                        blockSpawning = false;
                    } else {
                        blockSpawning = true;
                    }

                    //which then calls my merge animation
                    mergeAnimation();

                }
            }
        });
        timer.start();
    }

    //description: this helps with the merge animation
    //parameter: nothing
    //return: nothing
    public void mergeAnimation() {
        timer2 = new Timer(3, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

                //we just keep chaning X and Ymove till 60 to help animate the merging of two blocks
                if (x != 0 && Math.abs(XMove) < 60 || y != 0 && Math.abs(YMove) < 60) {
                    XMove += x;
                    YMove += y;
                    repaint();
                } else {
                    timer2.stop();
                    XMove = 0;
                    YMove = 0;

                    //we then copy the board again
                    copyBoard();
                    repaint();

                    //we then run this post merge animation
                    postMergeAnimation();
                }
            }
        });
        timer2.start();
    }

    //description: this method animates the board one more time after merging
    //because there will be holes after merging and will need to shift all the pieces
    //in the direction of merge again to fill out those outs
    //parameter: nothing
    //return: nothing
    public void postMergeAnimation() {
        timer3 = new Timer(3, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

                //basically same exact process are movement animations
                biggestStep = 0;
                animationTracker = 0;

                Update_board(board, x, y);
                how_many_steps_moved();

                if ((x != 0 && Math.abs(XMove) < Math.abs(animated_Steps())) || (y != 0 && Math.abs(YMove) < Math.abs(animated_Steps()))) {
                    XMove += x;
                    YMove += y;
                    repaint();
                } else {
                    timer3.stop();
                    XMove = 0;
                    YMove = 0;
                    anti_double_animation = false;

                    if (blockSpawning) {
                        newBlock();
                    }
                    copyBoard();
                    repaint();

//                  YOU CAN uncomment and call this method to test the lost page!
//                  since it will take some time to fill the board
//                  fillBoardNoMerges();


                    //when there is a winner becomes true
                    //it'll repaint and show the winner image and resets the board
                    if (checkWinner() == 1) {
                        winner = true;
                        repaint();

                        //nothing really happens if the board can continue
                    } else if (checkWinner() == 2) {
                        System.out.println();
                    } else {
                        //if board can't continue, this is set to true, which repaint
                        //and that triggers the lost page to show up
                        //and then board resets for a new game
                        lost = true;
                        repaint();
                    }
                }
            }
        });
        timer3.start();
    }

    public void keyReleased(KeyEvent e) {
    }


    public void keyTyped(KeyEvent e) {
    }


    public static void main(String[] args) throws IOException {
        game = new JFrame("3072");
        Main panel = new Main();
        game.add(panel);
        game.pack();
        game.setVisible(true);

        //Background Music
        try {
            AudioInputStream backgroundMusic = AudioSystem.getAudioInputStream(new File("background_music.wav"));
            music = AudioSystem.getClip();
            music.open(backgroundMusic);
        } catch (Exception e) {
        }


        music.start();
        //Loops background music forever
        music.loop(Clip.LOOP_CONTINUOUSLY);

        //Stops music when window closes
        game.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                music.close();
            }
        });
    }
}

