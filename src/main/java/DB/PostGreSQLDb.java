package DB;


import DB.Model.Transaction;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of the Db interface.
 * This is thought to be used as the primary interface to
 * the database.
 */
public class PostGreSQLDb implements DbI {

    private static String DATABASE_NAME = "ExjobbDatabas";

    Connection connection;

    /**
     * Creates a connection to the PostgreSQL database.
     * @return True if connection could be made, false if not.
     */
    public boolean createConnection() {
        try{
            Log.e(this,"bababababa");
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+DATABASE_NAME,
                    "postgres","admin");
            connection.setAutoCommit(true);
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (SQLException e) {
            Log.e(this,"Connection to "+DATABASE_NAME+" was not successful");
            return false;
        }
        Log.i(this,"Connection to "+DATABASE_NAME+" was successful!");
        return true;
    }

    /**
     * Authenticated the user based on username and password.
     * @param username Username to be authenticated.
     * @param password Password for the authentication.
     * @return True if authentication was successful, false if not.
     */
    public boolean authenticateUser(String username, String password) {
        String selectString = "SELECT * FROM users WHERE name=? and password=?;";
        try {
            PreparedStatement ps = connection.prepareStatement(selectString);
            ps.setString(1,username);
            ps.setString(2,password);
            ResultSet rs = ps.executeQuery();
            if(!rs.isBeforeFirst()){
                Log.i(this,"Authentication unsuccessful");
                return false;
            }
            Log.i(this,"Authentication successful");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Attempts to store a transaction in the database.
     * @param usernameTo Receiveing user.
     * @param usernameFrom Sending user.
     * @param amount Amount to send.
     * @return True if the transaction could be created, false if not.
     */
    public boolean makeTransaction(String usernameTo,String usernameFrom, double amount) {
        String insertString = "INSERT INTO transactions (amount,fromuser,touser) VALUES(?,?,?);";
        try{
            PreparedStatement ps = connection.prepareStatement(insertString);
            ps.setDouble(1,amount);
            ps.setString(2,usernameFrom);
            ps.setString(3,usernameTo);
            ps.executeUpdate();
        }catch(SQLException e){
            Log.e(this,"Transaction could not be created.");
            return false;
        }
        Log.i(this,"Transaction successfully created!");
        return true;
    }

    /**
     * Retrieves all the transactions in the form of a LinkedList.
     * @param username Username to retrieve transactions for.
     * @return LinkedList with transactions.
     */
    public LinkedList<Transaction> retrieveAllTransactions(String username) {
        String selectString ="SELECT * FROM transactions WHERE touser=?;";
        LinkedList<Transaction> trans = new LinkedList<Transaction>();
        try {
            PreparedStatement ps = connection.prepareStatement(selectString);
            ps.setString(1,username);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                trans.add(new Transaction(rs.getDouble("amount"),
                        rs.getString("touser"),rs.getString("fromuser"),rs.getInt("transactionid")));
            }
        } catch (SQLException e) {
            Log.e(this,"Could not retrieve transactions");
            e.printStackTrace();
        }
        return trans;
    }


    /**
     * Retrieves X last transactions from a user in the form of a LinkedList
     * @param username
     * @param nrOfTransactions
     * @return returns the last X transactions
     */
    public LinkedList<Transaction> retrieveNrOfTransactions(String username,int nrOfTransactions) {
        String selectString ="SELECT * FROM transactions WHERE touser=? LIMIT ?;";
        LinkedList<Transaction> trans = new LinkedList<Transaction>();
        try {
            PreparedStatement ps = connection.prepareStatement(selectString);
            ps.setString(1,username);
            ps.setInt(2,nrOfTransactions);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                trans.add(new Transaction(rs.getDouble("amount"),
                        rs.getString("touser"),rs.getString("fromuser"),rs.getInt("transactionid")));
            }
        } catch (SQLException e) {
            Log.e(this,"Could not retrieve transactions");
            e.printStackTrace();
        }
        return trans;
    }


    /**
     * @return A linkedlist of strings with all the usernames available in the database.
     */
    public LinkedList<String> retrieveAllUsernames(){
        String selectString = "SELECT name FROM users";
        LinkedList<String> usernames = new LinkedList<String>();
        try{
            PreparedStatement ps = connection.prepareStatement(selectString);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                usernames.add(new String(rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usernames;
    }

}
