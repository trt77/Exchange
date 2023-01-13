import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Objects;

public class CurrencyPairWindow extends JFrame {

    private JLabel pairFromFinalLabel;
    private JLabel pairToFinalLabel;
    private JPanel MainPairPanel;
    private JButton pairConfirmButton;
    private JLabel pairFinalTitleLabel;
    private JTextField pairRateTextField;
    private JLabel pairRateFinalLabel;
    private JComboBox pairFromComboBox;
    private JComboBox pairToComboBox;
    private JButton pairBackButton;
    static JFrame frame1;

    public static void run() {
        frame1 = new JFrame("CurrencyPairWindow");
        frame1.setContentPane(new CurrencyPairWindow().MainPairPanel);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.pack();
        frame1.setVisible(true);


    }

    public CurrencyPairWindow() {
        pairConfirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String currency1;
                String currency2;
                double rate;

                currency1 = Objects.requireNonNull(pairFromComboBox.getSelectedItem()).toString();
                currency2 = Objects.requireNonNull(pairToComboBox.getSelectedItem()).toString();
                rate = Double.parseDouble(pairRateTextField.getText());

                try {
                    Exchange.pst = Exchange.con.prepareStatement("insert into currency_pairs.pairs_data(currencyfrom,currencyto,rate)values(?,?,?)");
                    Exchange.pst.setString(1, currency1);
                    Exchange.pst.setString(2, currency2);
                    Exchange.pst.setDouble(3, rate);
                    Exchange.pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record added");


                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });

        pairBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame1.dispose();
            }
        });
    }
}

