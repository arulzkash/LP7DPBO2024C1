import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    //constructor

    int frameWidth = 360;
    int frameHeight = 640;

    Image backgroundImage;
    Image birdImage;
    Image lowerPipeImage;
    Image upperPipeImage;

    int playerStartPosX = frameWidth / 8;
    int playerStartPosY = frameHeight / 2;
    int playerWidth = 34;
    int playerHeight = 24;
    Player player;

    int pipeStartPosX = frameWidth;
    int pipeStartPosY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;
    ArrayList<Pipe> pipes;

    Timer gameLoop;
    Timer pipesCooldown;
    int gravity = 1;
    boolean gameIsOver;
    private int score = 0;
    private JLabel scoreLabel;

    //constructor
    public FlappyBird() {
        setPreferredSize(new Dimension(360, 640));
        setFocusable(true);
        addKeyListener(this);
//        setBackground(Color.blue);

        //load images
        backgroundImage = new ImageIcon(getClass().getResource("assets/background.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();
        lowerPipeImage = new ImageIcon(getClass().getResource("assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("assets/upperPipe.png")).getImage();

        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage );
        pipes = new ArrayList<Pipe>();

        pipesCooldown = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });

        gameLoop = new Timer(  1000/60,  this);
        gameLoop.start();

        pipesCooldown.start();

        // Create and configure score label
        scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setBounds(10, 10, 100, 30);
        this.add(scoreLabel);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);

        if (gameIsOver) {
            // Display game over message directly on the panel
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("Game Over! Press 'R' to restart", 50, frameHeight / 2);
        }
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, frameWidth, frameHeight, null);

        g.drawImage(player.getImage(), player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight(), null);

        for(int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight(), null);
        }

    }



    public void move() {
        // Cek tabrakan dengan pipa
        for (Pipe pipe : pipes) {
            // Posisi vertikal pemain berada di antara pipa
            boolean verticalCollision = player.getPosY() + player.getHeight() > pipe.getPosY() && player.getPosY() < pipe.getPosY() + pipe.getHeight();

            // Posisi horizontal pemain berada di dekat pipa
            boolean horizontalCollision = player.getPosX() + player.getWidth() > pipe.getPosX() && player.getPosX() < pipe.getPosX() + pipe.getWidth();

            if (horizontalCollision && verticalCollision) {
                gameOver();
                return;
            }


        }




        // Cek tabrakan dengan batas bawah JFrame
        if (player.getPosY() >= frameHeight) {
            // Terjatuh ke bawah, hentikan permainan
            gameOver();
            return;
        }

        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());
        player.setPosY(Math.max(player.getPosY(), 0));

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.setPosX(pipe.getPosX() + pipe.getVelocityX());
        }

        // Check for collision with pipes and update score
        boolean scoreUpdated = false; // Flag to ensure score is updated only once per pair of pipes
        for (Pipe pipe : pipes) {
            if (!pipe.isPassed() && pipe.getPosX() + pipe.getWidth() < playerStartPosX) {
                pipe.setPassed(true);
                if (!scoreUpdated) {
                    score++;
                    scoreLabel.setText("Score: " + score);
                    scoreUpdated = true; // Set flag to true once score is updated
                }
            }
        }
    }

    private void gameOver() {
        gameIsOver = true;
        gameLoop.stop();
        pipesCooldown.stop();


    }


    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            player.setVelocityY(-10);
        } else if (e.getKeyCode() == KeyEvent.VK_R && gameIsOver) {
            restartGame();
        }
    }

    private void restartGame() {
        // Reset kondisi permainan
        player.setPosY(playerStartPosY);
        pipes.clear();
        gameIsOver = false;
        score = 0;
        scoreLabel.setText("Score: " + score);

        // Mulai ulang timer dan mulai permainan baru
        gameLoop.start();
        pipesCooldown.start();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void placePipes() {
        int randomPipePosY = (int) (pipeStartPosY - pipeHeight/4 - Math.random() * (pipeHeight/2));
        int openingSpace = frameHeight/4;

        Pipe upperPipe = new Pipe(pipeStartPosX, randomPipePosY, pipeWidth, pipeHeight, upperPipeImage);
        pipes.add(upperPipe);

        Pipe lowerPipe = new Pipe(pipeStartPosX, randomPipePosY + pipeHeight + openingSpace, pipeWidth, pipeHeight, lowerPipeImage);
        pipes.add(lowerPipe);
    }

}


