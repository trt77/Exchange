import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.sql.*;
import java.sql.Connection;
import java.util.ArrayList;

public class Exchange extends JFrame{
    private JPanel MainPanel;
    private JTabbedPane mainTabbedPane;
    private JPanel logicPanel;
    private JPanel databasePanel;
    private JPanel logicInputPanel;
    private JPanel logicHistoryPanel;
    private JTable databaseTable;
    private JPanel databaseTablePanel;
    private JPanel databaseCrudPanel;
    private JButton databaseCrudAddButton;
    private JButton databaseCrudEditButton;
    private JButton databaseCrudRemoveButton;
    private JButton databaseCrudBackButton;
    private JButton logicHistoryClearButton;
    private JButton logicHistoryPairsButton;
    private JTextField wynikTextField;
    private JButton logicInputRunButton;
    private JLabel toFinalLabel;
    private JLabel exchangeFinalLabel;
    private JLabel fromFinalLabel;
    private JComboBox logicInputCurrency1ComboBox;
    private JComboBox Currency2ComboBox;
    private JTextField textField1;
    private JList logicHistoryList;
    private JLabel logicTransactionHistoryFinalLabel;
    private static ArrayList<String> listString = new ArrayList<String>();

    public static void main(String[] args) {
        JFrame frame = new JFrame("Kantor wymiany walut");
        frame.setContentPane(new Exchange().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
/*
    static Connection con;
    static PreparedStatement pst;

    public void connect() {
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://localhost/Exchange", "postgres", "admin");
            System.out.println("Connection success");
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            System.out.println("Connection failed");
        }
    }
*/
    public Exchange() {
    ConnectionDetails.connect();

        databaseCrudAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CurrencyPairWindow.run();
                try {

                    Statement st = ConnectionDetails.con.createStatement();
                    String query = "SELECT * FROM currency_pairs.pairs_data";
                    ResultSet rs = st.executeQuery(query);
                    ResultSetMetaData rsmd = rs.getMetaData();
                    DefaultTableModel model = (DefaultTableModel) databaseTable.getModel();
                    model.setRowCount(0);

                    int cols = rsmd.getColumnCount();
                    String[] colName = new String[cols];
                    for(int i = 0; i<cols; i++) colName[i] = rsmd.getColumnName(i+1);
                    model.setColumnIdentifiers(colName);
                    String id, currency1, currency2, rate;
                    while(rs.next()) {
                        id = String.valueOf(rs.getInt(1));
                        currency1 = rs.getString(2);
                        currency2 = rs.getString(3);
                        rate = String.valueOf(rs.getDouble(4));
                        String[] row = {id,currency1,currency2,rate};
                        model.addRow(row);
                    }
                    } catch (SQLException exception) {
                    throw new RuntimeException(exception);
                }
            }
        });

        logicHistoryPairsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                mainTabbedPane.setSelectedIndex(1);
                try {

                    Statement st = ConnectionDetails.con.createStatement();
                    String query = "SELECT * FROM currency_pairs.pairs_data";
                    ResultSet rs = st.executeQuery(query);
                    ResultSetMetaData rsmd = rs.getMetaData();
                    DefaultTableModel model = (DefaultTableModel) databaseTable.getModel();
                    model.setRowCount(0);

                    int cols = rsmd.getColumnCount();
                    String[] colName = new String[cols];
                    for(int i = 0; i<cols; i++) colName[i] = rsmd.getColumnName(i+1);
                    model.setColumnIdentifiers(colName);
                    String id, currency1, currency2, rate;
                    while(rs.next()) {
                        id = String.valueOf(rs.getInt(1));
                        currency1 = rs.getString(2);
                        currency2 = rs.getString(3);
                        rate = String.valueOf(rs.getDouble(4));
                        String[] row = {id,currency1,currency2,rate};
                        model.addRow(row);
                    }
                } catch (SQLException exception) {
                    throw new RuntimeException(exception);
                }
            }
        });
        databaseCrudBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainTabbedPane.setSelectedIndex(0);
            }
        });
        logicInputRunButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currency1;
                String currency2;
                double sum;
                double value;
                double rate;

                currency1 = logicInputCurrency1ComboBox.getSelectedItem().toString();
                currency2 = Currency2ComboBox.getSelectedItem().toString();
                value = Double.parseDouble(textField1.getText());

                String query = "SELECT rate FROM currency_pairs.pairs_data WHERE currencyfrom=";
                query+="'"+currency1+"' AND currencyto='" + currency2 + "';";

                try {
                    Statement stmt = ConnectionDetails.con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    if(!rs.next()) {
                        JOptionPane.showMessageDialog(null, "Brak informacji o parze w bazie.", "Uwaga", JOptionPane.PLAIN_MESSAGE);
                    }else{
                    //while (rs.next()) {
                        System.out.println("Running SQL query: " + query);
                        rate = rs.getDouble("rate");
                        sum = rate*value;
                        double sumRounded = Math.round(sum * 100.0) / 100.0;
                        String show = new CurrencyPair(currency1,currency2,rate,sumRounded).toString();
                        String message = "Według wprowadzonego kursu " + currency1 + "/" + currency2 + ": " + rate + " należy wydać: " + sumRounded;
                        JOptionPane.showMessageDialog(null, message ,"Alert" , JOptionPane.PLAIN_MESSAGE);
                        listString.add(show);
                        wynikTextField.setText(String.valueOf(sumRounded));
                    //}
                    }

                    DefaultListModel model = new DefaultListModel();
                    for (int i=0;i<listString.size();i++)
                    {
                        model.add(i,listString.get(i));
                    }
                    //model.addElement(listString);
                    logicHistoryList.setModel(model);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        logicHistoryClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultListModel model = new DefaultListModel();
                model.removeAllElements();
                logicHistoryList.setModel(model);
                listString.clear();
                wynikTextField.setText("Wynik");
            }
        });
        databaseCrudRemoveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String rowToDelete;
                int selected = databaseTable.getSelectedRow();
                rowToDelete = (String) databaseTable.getValueAt(selected, 0);
                String queryToDeleteRow = "DELETE FROM currency_pairs.pairs_data WHERE \"id\" IN (" + rowToDelete + ");";
                System.out.println("Running SQL query: " + queryToDeleteRow);
                try {
                    Statement stmt = ConnectionDetails.con.createStatement();
                    stmt.executeUpdate(queryToDeleteRow);
                    System.out.println("Pair ID:" + rowToDelete + " successfully deleted.");
                } catch (SQLException ex) {
                    System.out.println("Row deletion error.");
                    throw new RuntimeException(ex);
                }
                try {

                    Statement st = ConnectionDetails.con.createStatement();
                    String query = "SELECT * FROM currency_pairs.pairs_data";
                    ResultSet rs = st.executeQuery(query);
                    ResultSetMetaData rsmd = rs.getMetaData();
                    DefaultTableModel model = (DefaultTableModel) databaseTable.getModel();
                    model.setRowCount(0);

                    int cols = rsmd.getColumnCount();
                    String[] colName = new String[cols];
                    for(int i = 0; i<cols; i++) colName[i] = rsmd.getColumnName(i+1);
                    model.setColumnIdentifiers(colName);
                    String id, currency1, currency2, rate;
                    while(rs.next()) {
                        id = String.valueOf(rs.getInt(1));
                        currency1 = rs.getString(2);
                        currency2 = rs.getString(3);
                        rate = String.valueOf(rs.getDouble(4));
                        String[] row = {id,currency1,currency2,rate};
                        model.addRow(row);
                    }
                } catch (SQLException exception) {
                    throw new RuntimeException(exception);
                }

            }
        });
        databaseCrudEditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //indentacja twardą spacją zamiast JLabel w JOptionPane
                String editMessage = "\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0" +
                        "Nowe rekordy muszą być unikalne!\nAby dokonać edycji, usuń parę walut, po czym dodaj nowy rekord.";
                JOptionPane.showMessageDialog(null, editMessage ,"Alert" , JOptionPane.PLAIN_MESSAGE);
            }
        });
    }
    
}
