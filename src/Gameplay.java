package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
    Snake snake = new Snake();
    Apple apple = new Apple();
    private ImageIcon snakeHead;
    private Timer timer;
    private int delay = 50;
    private ImageIcon snakeBody;
    AtomicBoolean speedUp = new AtomicBoolean(true);
    private int snakeHeadXPos = 379;
    private ImageIcon appleImage;
    private Random random = new Random();
    private int xPos = random.nextInt(100);
    private int yPos = random.nextInt(100);
    private ImageIcon titleImage;
    Score score = new Score();
    //    private String highScore;
    private boolean scoresaved  = false;
    private ImageIcon arrowImage;
    private ImageIcon shiftImage;
    private List<Integer> highScores;
    private List<Point> hurdles;
    private final int HurdleWidth = 40;
    private final int HurdleHeight = 40;
    private int appleXPos; // Store actual apple X position
    private int appleYPos; // Store actual apple Y position
    private int level =1;

    public Gameplay() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
        highScores = score.getHighScores();
        hurdles = new ArrayList<>();
        generateApplePosition();
    }

    public void paint(Graphics g) {
        if (snake.moves == 0) {
            initializeSnake();
        }

        drawTitle(g);
        drawGameplayArea(g);
        drawLeaderboard(g);
        drawCurrentScore(g);
        drawControllerInfo(g);
        drawSnake(g);
        drawApple(g);
        drawGameOver(g);
        drawHurdles(g);
        checkCollisions(g);

        g.dispose();
    }

    private void initializeSnake() {
        for (int i = 0; i < 5; i++) {
            snake.snakexLength[i] = snakeHeadXPos;
            snakeHeadXPos -= 6;
            snake.snakeyLength[i] = 355;
        }
    }

    private void drawTitle(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawRect(24, 10, 852, 55);
        titleImage = new ImageIcon("images/title.png");
        titleImage.paintIcon(this, g, 25, 11);
    }

    private void drawGameplayArea(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawRect(24, 71, 620, 614);
        g.setColor(Color.black);
        g.fillRect(25, 72, 619, 613);
    }

    private void drawLeaderboard(Graphics g) {
        g.setColor(Color.black);
        g.drawRect(653, 71, 223, 614);
        g.setColor(Color.black);
        g.fillRect(654, 72, 221, 613);
    }

    private void drawCurrentScore(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("Helvetica", Font.BOLD, 20));
        g.drawString("SCORE : " + score.getScore(), 660, 120);
        g.drawRect(655, 100, 115, 27);

        g.drawRect(775,100,95,27);
        g.drawString("LEVEL: " + level,780,120);

        g.drawString("HIGHSCORE", 705, 180);
        drawHighScores(g,highScores,705,200);
    }

    private void drawControllerInfo(Graphics g) {
        g.drawRect(653, 490, 221, 1);
        g.setFont(new Font("Helvetica", Font.BOLD, 25));
        g.drawString("CONTROLS", 690, 530);
        arrowImage = new ImageIcon("images/keyboardArrow.png");
        arrowImage.paintIcon(this, g, 670, 560);
        g.setFont(new Font("Helvetica", Font.PLAIN, 16));
        g.drawString("Movement", 770, 590);
        shiftImage = new ImageIcon("images/shift.png");
        shiftImage.paintIcon(this, g, 695, 625);
        g.drawString("Boost", 770, 640);
    }

    private void drawSnake(Graphics g) {
        snakeHead = new ImageIcon("images/snakeHead4.png");
        snakeHead.paintIcon(this, g, snake.snakexLength[0], snake.snakeyLength[0]);
        for (int i = 1; i < snake.lengthOfSnake; i++) {
            snakeBody = new ImageIcon("images/snakeimage4.png");
            snakeBody.paintIcon(this, g, snake.snakexLength[i], snake.snakeyLength[i]);
        }
    }

    private void generateApplePosition() {
        int appleXIndex,  appleYIndex;
        boolean isValidPosition;

        do {
            appleXIndex = random.nextInt(apple.applexPos.length);
            appleYIndex = random.nextInt(apple.appleyPos.length);

            isValidPosition = isApplePositionValid(appleXIndex,appleYIndex);
        } while (!isValidPosition); // Check if the position is valid

        appleXPos = apple.applexPos[appleXIndex]; // Set X position
        appleYPos = apple.appleyPos[appleYIndex]; // Set Y position
    }
    private boolean isApplePositionValid(int xindex, int yindex) {
        // Check if the generated apple position overlaps with any hurdle
        int appleX = apple.applexPos[xindex];
        int appleY = apple.appleyPos[yindex];

        for (Point hurdle : hurdles) {
            if (appleX == hurdle.x && appleY == hurdle.y) {
                return false; // Position is invalid if it overlaps with a hurdle
            }
        }
        return true; // Valid position
    }
    private void drawApple(Graphics g) {
        appleImage = new ImageIcon("images/apple4.png");
        if (snake.moves != 0) {
            appleImage.paintIcon(this, g, appleXPos, appleYPos);
            checkAppleCollision();
        }

    }


    private void checkAppleCollision() {
        // Check if the snake eats the apple
        if (appleXPos == snake.snakexLength[0]&& appleYPos == snake.snakeyLength[0]) {
            snake.lengthOfSnake++;
            score.increaseScore();
            generateApplePosition();

            // Speed up the snake whenever the score reaches a multiple of 5
            if (score.getScore() % 5 == 0 && score.getScore() != 0) {
                level++;
                updateSnakeSpeed();
                addHurdles(3); //also adding three hurdles after every 5 score
            }

        }

    }

    private void updateSnakeSpeed() {
        if (delay > 100) {
            delay -= 100;
        } else if (delay == 100) {
            delay -= 50;
        } else if (delay <= 50 && delay > 20) {
            delay -= 10;
        } else {
            delay = 20;
        }
        timer.setDelay(delay);

    }


    private void drawGameOver(Graphics g) {
        if (snake.death) {
            if (!scoresaved) { // Only save once
                score.saveNewScore();
                scoresaved = true; // Set flag to true
            }
            g.setColor(Color.RED);
            g.setFont(new Font("Courier New", Font.BOLD, 50));
            g.drawString("Game Over!", 190, 340);
            g.setColor(Color.GREEN);
            g.setFont(new Font("Courier New", Font.BOLD, 18));
            g.drawString("Your Score : " + score.getScore(), 250, 370);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Courier New", Font.BOLD, 20));
            g.drawString("Press Spacebar to restart!", 187, 400);
        } else {
            scoresaved = false; // Reset flag when game is not over
        }
    }

    private void drawHurdles(Graphics g) {
        g.setColor(Color.BLUE);
        for (Point hurdle : hurdles) {
            g.fillRect(hurdle.x, hurdle.y, HurdleWidth, HurdleHeight);
        }

    }

    private void checkCollisions(Graphics g) {
        for (Point hurdle : hurdles) {
            if (checkHurdleCollision(hurdle)) {
                snake.dead();
                drawGameOver(g);
            }

        }

//        collision with snake body
        for (int i = 1; i < snake.lengthOfSnake; i++) {
            if (snake.snakexLength[i] == snake.snakexLength[0] && snake.snakeyLength[i] == snake.snakeyLength[0]) {
                snake.dead();
                drawGameOver(g);
            }
        }

    }

    private boolean checkHurdleCollision(Point hurdle) {
        return snake.snakexLength[0] < (hurdle.x + HurdleWidth) &&
                snake.snakexLength[0] > hurdle.x &&
                snake.snakeyLength[0] < (hurdle.y + HurdleHeight) &&
                snake.snakeyLength[0] > hurdle.y;

    }


    private void addHurdles(int count) {
        for (int i = 0; i < count; i++) {
            int x = random.nextInt(620 / HurdleWidth) * HurdleWidth + 25;
            int y = random.nextInt(613 / HurdleHeight) * HurdleHeight + 72;
            hurdles.add(new Point(x, y));
        }

    }

    public void drawHighScores(Graphics g, List<Integer> scores, int x, int y) {
        if (scores.isEmpty()) {
            g.drawString("No High Scores", x, y);
            return;
        }
        for (int i = 0; i < scores.size(); i++) {
            g.drawString((i + 1) + ". " + scores.get(i), x, y + (i * 20));
        }

    }

    public void drawString(Graphics g, String text, int x, int y) {
        if (text == null || text.isEmpty()) {
            return;
        }
        for (String line : text.split("\n")) {
            g.drawString(line, x, y += g.getFontMetrics().getHeight());
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (snake.right) {
            snake.movementRight();
            repaint();
        }

        if (snake.left) {
            snake.movementLeft();
            repaint();
        }

        if (snake.up) {
            snake.movementUp();
            repaint();
        }

        if (snake.down) {
            snake.movementDown();
            repaint();
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SHIFT:
                handleSpeedUp();
                break;

            case KeyEvent.VK_SPACE:
                handleGameStartOrRestart();
                break;

            case KeyEvent.VK_RIGHT:
                snake.moveRight();
                break;

            case KeyEvent.VK_LEFT:
                snake.moveLeft();
                break;

            case KeyEvent.VK_UP:
                snake.moveUp();
                break;

            case KeyEvent.VK_DOWN:
                snake.moveDown();
                break;
        }

    }


    //speedup for shift key
    private void handleSpeedUp() {
        if (speedUp.compareAndSet(true, false)) {
            if (delay > 100) {
                timer.setDelay(delay / 10);
            } else {
                timer.setDelay(10);
            }
        }

    }

    private void handleGameStartOrRestart() {
        if (snake.moves == 0) {
            snake.moves++;
            snake.right = true;
        }
        if (snake.death) {
            resetGame();
        }

    }

    private void resetGame() {
        snake.moves = 0;
        snake.lengthOfSnake = 5;
        score.resetScore();
        level = 1;
        highScores = score.getHighScores();
        delay = 50;
        timer.setDelay(delay);
        hurdles.clear();
        repaint();
        snake.death = false;

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            speedUp.set(true);
            timer.setDelay(delay);

        }
    }
}