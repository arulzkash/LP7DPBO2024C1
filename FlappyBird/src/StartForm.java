import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StartForm extends JFrame {
    public StartForm() {
        setTitle("Flappy Bird Start");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Tutup form saat tombol ditekan
                openFlappyBird(); // Buka game FlappyBird
            }
        });

        panel.add(startButton);
        add(panel);
    }

    private void openFlappyBird() {
        JFrame frame = new JFrame("Flappy Bird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360, 640);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird);
        frame.pack();
        flappyBird.requestFocus();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                StartForm startForm = new StartForm();
                startForm.setVisible(true); // Tampilkan form start saat aplikasi dijalankan
            }
        });
    }
}
