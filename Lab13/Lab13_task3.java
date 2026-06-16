import javax.swing.*;

public class Lab13_task3 {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.setSize(600, 600);
        frame.setLayout(null);

        JLabel userLabel = new JLabel("UserName: ");
        JTextField userText = new JTextField();

        userLabel.setBounds(20, 20, 80, 20);
        userText.setBounds(110, 20, 80, 20);
        userText.setVisible(true);

        frame.add(userLabel);
        frame.add(userText);
    }
}