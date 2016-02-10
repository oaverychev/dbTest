public class Main {

    public static void main(String[] args) {
        DatabaseConnection dbc = new DatabaseConnection();

        CrashUserWindow crashUserWindow = new CrashUserWindow();
        crashUserWindow.setEmailLabel(dbc.getField("email"));
        crashUserWindow.setIdLabel(dbc.getField("id"));
        crashUserWindow.setVisible(true);
        crashUserWindow.pack();
    }
    
}
