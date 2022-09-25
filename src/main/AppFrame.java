package src.main;

import javax.swing.*;

public class AppFrame extends JFrame {// inheriting JFrame
    JFrame f;

    AppFrame() {
        JButton b = new JButton("click");// create button
        b.setBounds(130, 100, 100, 40);

        add(b);// adding button on frame
        setSize(400, 500);
        setLayout(null);
        setVisible(true);
    }
}
