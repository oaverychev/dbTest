import java.awt.*;
import javax.swing.*;

public class CrashUserWindow extends JFrame {
    private int voron = 0;
    private JLabel idLabel;
    private JLabel emailLabel;

    private JButton addCrow;
    private JButton removeCrow;

    public void setIdLabel(String idLabel) {
        this.idLabel.setText(idLabel);
    }
    public void setEmailLabel(String emailLabel) {
        this.emailLabel.setText(emailLabel);
    }

    public CrashUserWindow() {
        super("Crow calculator");
        //Подготавливаем компоненты объекта
        idLabel = new JLabel("");
        emailLabel = new JLabel("");
        addCrow = new JButton("+");
        removeCrow = new JButton("-");

        //Подготавливаем временные компоненты
        JPanel labelsPanel = new JPanel(new FlowLayout());
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        //Расставляем компоненты по местам
        labelsPanel.add(idLabel);
        labelsPanel.add(emailLabel);

        buttonsPanel.add(addCrow);
        buttonsPanel.add(removeCrow);

        add(labelsPanel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void updateCrowCounter() {
        idLabel.setText("Crows:" + voron);
    }
}