
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.Timer;
import javax.sound.sampled.*;
import java.io.File;
import java.util.*;
import java.io.*;


public class Main extends JPanel implements ActionListener, MouseListener, KeyListener {

    //yea why did heck did we end up with 4 2D arrays in total
    int[][] board = new int[5][5];
    int[][] oldBoard = new int[5][5];
    int[][] stepMoved = new int[5][5];
    int[][] mergeStepMoved = new int[5][5];



    //not sure if we'll actually use this
    Color[] colors = {
            Color.RED,
            Color.BLUE,
            Color.YELLOW,
            Color.GREEN,
            Color.PINK,
            Color.ORANGE,
            Color.WHITE,
            Color.GRAY,
            Color.CYAN,
            Color.MAGENTA,
            Color.LIGHT_GRAY,
            Color.DARK_GRAY
    };

    final int SQUARE_SIZE = 60;
    final int TOP_OFFSET = 150;
    final int BORDER_SIZE = 150;

    //used to move the blocks
    int x = 20;
    int y = 20;

    int XMove = 0;
    int YMove = 0;

    int rightTracker;
    int leftTracker;
    int downTracker;
    int upTracker;

    int animationTracker;
    int biggestStep = 0;

    Timer timer;
    Timer timer2;
    Timer timer3;

    long lastInputTime;
    long cooldown = 200;
    int key;

    boolean anti_double_animation = false;
    boolean blockSpawning=true;
    boolean didMove=false;

    int index;

    Image offscreenImage;
    Graphics2D offscreenGraphics;
    Clip mergeEffect;


    //constructor
    public Main() {
        setPreferredSize(new Dimension(600, 600));
        setBackground(new Color(222, 207, 189));

        setFocusable(true);
        addKeyListener(this);

        //starter blocks
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


        //to intialize so that i can draw the blocks first and then animate the old board to the new board
        oldBoard[starterBlock1_Y][starterBlock1_X] = 3;
        oldBoard[starterBlock2_Y][starterBlock2_X] = 3;
        oldBoard[starterBlock3_Y][starterBlock3_X] = 3;

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (offscreenGraphics == null) {
            offscreenImage = createImage(this.getWidth(), this.getHeight());
            offscreenGraphics = (Graphics2D) offscreenImage.getGraphics();
        }

        offscreenGraphics.setColor(getBackground());
        offscreenGraphics.fillRect(0, 0, getWidth(), getHeight());

        offscreenGraphics.setColor(new Color(187, 173, 160)); //250, 248, 239
        offscreenGraphics.fillRoundRect(140, 140, 320, 320, 30, 30);

        offscreenGraphics.setColor(Color.black);
        offscreenGraphics.setStroke(new BasicStroke(1));
        offscreenGraphics.drawRoundRect(140, 140, 320, 320, 30, 30);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                offscreenGraphics.setColor(new Color(250, 248, 239));
                offscreenGraphics.fillRoundRect(j * SQUARE_SIZE + BORDER_SIZE, i * SQUARE_SIZE + TOP_OFFSET, SQUARE_SIZE, SQUARE_SIZE, 20, 20);
                offscreenGraphics.setColor(Color.black);
                offscreenGraphics.drawRoundRect(j * SQUARE_SIZE + BORDER_SIZE, i * SQUARE_SIZE + TOP_OFFSET, SQUARE_SIZE, SQUARE_SIZE, 20, 20);
            }
        }


        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (oldBoard[i][j] != 0 && !Arrays.deepEquals(oldBoard, board)) {

                    int value = oldBoard[i][j];
                    index = (int) (Math.log(value/3.0)/Math.log(2));
                    offscreenGraphics.setColor(colors[index]);


                    if (x > 0 && y == 0) {


                        if (mergeStepMoved[i][j] != 0) {
                            offscreenGraphics.fillRoundRect(j * SQUARE_SIZE + BORDER_SIZE + XMove, i * SQUARE_SIZE + TOP_OFFSET + YMove, SQUARE_SIZE, SQUARE_SIZE, 20, 20);

                        } else if (j * SQUARE_SIZE + BORDER_SIZE + XMove < j * SQUARE_SIZE + BORDER_SIZE + stepMoved[i][j] * 60) {
                            offscreenGraphics.fillRoundRect(j * SQUARE_SIZE + BORDER_SIZE + XMove, i * SQUARE_SIZE + TOP_OFFSET + YMove, SQUARE_SIZE, SQUARE_SIZE, 20, 20);

                        } else {
                            offscreenGraphics.fillRoundRect(j * SQUARE_SIZE + BORDER_SIZE + stepMoved[i][j] * 60, i * SQUARE_SIZE + TOP_OFFSET, SQUARE_SIZE, SQUARE_SIZE, 20, 20);
                        }

                    } else if (x < 0 && y == 0) {

                        if (mergeStepMoved[i][j] != 0) {
                            offscreenGraphics.fillRoundRect(j * SQUARE_SIZE + BORDER_SIZE + XMove, i * SQUARE_SIZE + TOP_OFFSET + YMove, SQUARE_SIZE, SQUARE_SIZE, 20, 20);
                        } else if (j * SQUARE_SIZE + BORDER_SIZE + XMove > j * SQUARE_SIZE + BORDER_SIZE + stepMoved[i][j] * 60) {
                            offscreenGraphics.fillRoundRect(j * SQUARE_SIZE + BORDER_SIZE + XMove, i * SQUARE_SIZE + TOP_OFFSET + YMove, SQUARE_SIZE, SQUARE_SIZE, 20, 20);
                        } else {
                            offscreenGraphics.fillRoundRect(j * SQUARE_SIZE + BORDER_SIZE + stepMoved[i][j] * 60, i * SQUARE_SIZE + TOP_OFFSET, SQUARE_SIZE, SQUARE_SIZE, 20, 20);
                        }

                    } else if (y > 0 && x == 0) {

                        if (mergeStepMoved[i][j] != 0) {
                            offscreenGraphics.fillRoundRect(j * SQUARE_SIZE + BORDER_SIZE + XMove, i * SQUARE_SIZE + TOP_OFFSET + YMove, SQUARE_SIZE, SQUARE_SIZE, 20, 20);

                        } else if (i * SQUARE_SIZE + TOP_OFFSET + YMove < i * SQUARE_SIZE + TOP_OFFSET + stepMoved[i][j] * 60) {
                            offscreenGraphics.fillRoundRect(j * SQUARE_SIZE + BORDER_SIZE + XMove, i * SQUARE_SIZE + TOP_OFFSET + YMove, SQUARE_SIZE, SQUARE_SIZE, 20, 20);

                        } else {
                            offscreenGraphics.fillRoundRect(j * SQUARE_SIZE + BORDER_SIZE, i * SQUARE_SIZE + TOP_OFFSET + stepMoved[i][j] * 60, SQUARE_SIZE, SQUARE_SIZE, 20, 20);
                        }
                    } else if (y < 0 && x == 0) {

                        if (mergeStepMoved[i][j] != 0) {

                            offscreenGraphics.fillRoundRect(j * SQUARE_SIZE + BORDER_SIZE + XMove, i * SQUARE_SIZE + TOP_OFFSET + YMove, SQUARE_SIZE, SQUARE_SIZE, 20, 20);

                        } else if (i * SQUARE_SIZE + TOP_OFFSET + YMove > i * SQUARE_SIZE + TOP_OFFSET + stepMoved[i][j] * 60) {
                            offscreenGraphics.fillRoundRect(j * SQUARE_SIZE + BORDER_SIZE + XMove, i * SQUARE_SIZE + TOP_OFFSET + YMove, SQUARE_SIZE, SQUARE_SIZE, 20, 20);

                        } else {
                            offscreenGraphics.fillRoundRect(j * SQUARE_SIZE + BORDER_SIZE, i * SQUARE_SIZE + TOP_OFFSET + stepMoved[i][j] * 60, SQUARE_SIZE, SQUARE_SIZE, 20, 20);
                        }
                    }
                } else if (Arrays.deepEquals(oldBoard, board) && oldBoard[i][j] != 0) {

                    int value = oldBoard[i][j];
                    index = (int) (Math.log(value/3.0)/Math.log(2));
                    offscreenGraphics.setColor(colors[index]);

                    offscreenGraphics.fillRoundRect(j * SQUARE_SIZE + BORDER_SIZE, i * SQUARE_SIZE + TOP_OFFSET, SQUARE_SIZE, SQUARE_SIZE, 20, 20);

                }
            }
        }


        g2d.drawImage(offscreenImage, 0, 0, this);

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


    public void Update_board(int[][] board1, int directionX, int directionY) {

        if (directionX > 0) {

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

    public void newBlock() {

        int randomblockX = (int) (Math.random() * 5);
        int randomblockY = (int) (Math.random() * 5);

        for(int i=0; i< 5; i++) {

            for (int j = 0; j < 5; j++) {
                if (board[i][j] == 0) {
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


    public void how_many_steps_moved() {

        //to reset the stepMoved 2D array to prevent remeants from the last time messing up animations
        for(int i = 0; i<5; i++){
            for(int j = 0; j<5; j++){
                stepMoved[i][j]=0;
            }
        }

        if (x > 0 && y == 0) {
            for (int i = 0; i < 5; i++) {
                rightTracker = 4;
                for (int j = 4; j >= 0; j--) {
                    if (oldBoard[i][j] != 0) {
                        stepMoved[i][j] = rightTracker - j;
                        rightTracker--;
                    }
                }
            }
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

    public int animated_Steps() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (Math.abs(stepMoved[i][j]) > Math.abs(biggestStep)) {
                    biggestStep = stepMoved[i][j];
                }
            }
        }
        animationTracker = biggestStep * 60;

        return animationTracker;
    }

    public void copyBoard() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                oldBoard[i][j] = board[i][j];
            }
        }
    }

    public void merge() {

        int merge_count_SFX=0;

        for(int i =0; i<5; i++){
            for(int j = 0; j<5; j++){
                mergeStepMoved[i][j]=0;
            }
        }

        if (x > 0 && y == 0) { // Right
            for (int row = 0; row < 5; row++) {
                for (int col = 4; col > 0; col--) {
                    if (board[row][col] != 0 && board[row][col] == board[row][col - 1]) {
                        board[row][col] *= 2;
                        board[row][col - 1] = 0;
                        mergeStepMoved[row][col - 1] = 1;
                        merge_count_SFX++;
                    }
                }
            }

        } else if (x < 0 && y == 0) {
            for (int row = 0; row < 5; row++) {
                for (int col = 0; col < 4; col++) {
                    if (board[row][col] != 0 && board[row][col] == board[row][col + 1]) {
                        board[row][col] *= 2;
                        board[row][col + 1] = 0;
                        mergeStepMoved[row][col + 1] = -1;
                        merge_count_SFX++;
                    }
                }
            }
        } else if (x == 0 && y > 0) {
            for (int col = 0; col < 5; col++) {
                for (int row = 4; row > 0; row--) {
                    if (board[row][col] != 0 && board[row][col] == board[row - 1][col]) {
                        board[row][col] *= 2;
                        board[row - 1][col] = 0;
                        mergeStepMoved[row - 1][col] = 1;
                        merge_count_SFX++;

                    }
                }
            }
        } else if (x == 0 && y < 0) {
            for (int col = 0; col < 5; col++) {
                for (int row = 0; row < 4; row++) {
                    if (board[row][col] != 0 && board[row][col] == board[row + 1][col]) {
                        board[row][col] *= 2;
                        board[row + 1][col] = 0;
                        mergeStepMoved[row + 1][col] = -1;
                        merge_count_SFX++;
                    }
                }
            }
        }

        if(merge_count_SFX>0){
            try {
                AudioInputStream mergeEffectSound = AudioSystem.getAudioInputStream(new File ("mergeEffect.wav"));
                mergeEffect = AudioSystem.getClip();
                mergeEffect.open(mergeEffectSound);
            }
            catch (Exception  e) {
            }
            mergeEffect.start();
        }

    }

    public int checkWinner() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (board[i][j] == 3072) {
                    return 1;
                }else if(board[i][j]==0){
                    return 2;
                }
            }
        }
        return 3;
    }

    public void keyPressed(KeyEvent kp) {

        long currentTime=System.currentTimeMillis();

        if(currentTime-lastInputTime>=cooldown){
            key = kp.getKeyCode();

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

            lastInputTime=currentTime;

        }
    }

    public void startMoveAnimation() {

        biggestStep = 0;
        animationTracker = 0;
        anti_double_animation = true;

        copyBoard();
        Update_board(board, x, y);
        how_many_steps_moved();

        if(Arrays.deepEquals(oldBoard,board)){
            blockSpawning=false;
            didMove=false;
        }else{
            blockSpawning=true;
            didMove=true;
        }


        timer = new Timer(3, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

                if ( x != 0 && Math.abs(XMove) < Math.abs(animated_Steps()) || y != 0 && Math.abs(YMove) < Math.abs(animated_Steps())) {
                    XMove += x;
                    YMove += y;
                    repaint();
                } else {

                    timer.stop();
                    XMove = 0;
                    YMove = 0;
                    anti_double_animation=false;
                    copyBoard();
                    repaint();

                    merge();
                    if(didMove){
                        blockSpawning=true;
                    }else if(Arrays.deepEquals(oldBoard,board)){
                        blockSpawning=false;
                    } else{
                        blockSpawning=true;
                    }
                    mergeAnimation();

                }
            }
        });
        timer.start();
    }

    public void mergeAnimation() {
        timer2 = new Timer(3, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

                if (x!=0 && Math.abs(XMove) < 60 || y!=0 && Math.abs(YMove) < 60) {
                    XMove += x;
                    YMove += y;
                    repaint();
                } else {
                    timer2.stop();
                    XMove = 0;
                    YMove = 0;
                    copyBoard();
                    repaint();
                    postMergeAnimation();
                }
            }
        });
        timer2.start();
    }

    public void postMergeAnimation(){
        timer3 = new Timer(3, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

                biggestStep = 0;
                animationTracker = 0;

                Update_board(board,x,y);
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

                    if(blockSpawning){
                        newBlock();
                    }
                    copyBoard();
                    repaint();

//                  checkWinner();
                }
            }
        });
        timer3.start();
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