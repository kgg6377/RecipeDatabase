import Objects.*;
import org.postgresql.util.PSQLException;

import java.awt.*;
import java.sql.*;
import java.time.*;
import java.util.*;
import com.jcraft.jsch.*;
import javax.xml.transform.Result;
import java.sql.PreparedStatement;
import java.util.Properties;

public class Interface {

    public static void main(String[] args) throws SQLException{
        Interface i = new Interface();

        Scanner scan = new Scanner(System.in);

        int lport = 5432;
        String rhost = "starbug.cs.rit.edu";
        int rport = 5432;
       /* System.out.print("Username: ");
        String username = scan.nextLine();
        System.out.print("Password: ");
        String password = scan.nextLine();
*/
        String username = "kgg6377";
        String password = "GomezFam6014!@";
        String databaseName = "p320_13"; //change to your database name

        String driverName = "org.postgresql.Driver";
        Connection conn = null;
        Session session = null;
        try {
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            JSch jsch = new JSch();
            session = jsch.getSession(username, rhost, 22);
            session.setPassword(password);
            session.setConfig(config);
            session.setConfig("PreferredAuthentications","publickey,keyboard-interactive,password");
            session.connect();
            System.out.println("Connected");
            int assigned_port = session.setPortForwardingL(lport, "localhost", rport);
            System.out.println("Port Forwarded");

            // Assigned port could be different from 5432 but rarely happens
            String url = "jdbc:postgresql://localhost:"+ assigned_port + "/" + databaseName;

            System.out.println("database Url: " + url);
            Properties props = new Properties();
            props.put("user", username);
            props.put("password", password);

            Class.forName(driverName);
            conn = DriverManager.getConnection(url, props);
            System.out.println("Database connection established");

            i.RunUI(conn, session);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Closing Database Connection");
                conn.close();
            }
            if (session != null && session.isConnected()) {
                System.out.println("Closing SSH Connection");
                session.disconnect();
            }
        }
    }

    /***
     * Main UI function
     */
    public void RunUI(Connection conn, Session session) {
        if(conn == null) {
            System.out.println("Connection is null");
            return;
        }

        Scanner scan = new Scanner(System.in);

        System.out.println("Welcome to the Recipe Domain UI");
        boolean top = true, bot = true;


        //Two while loops
        //Top while loop for creating a new account and logging in
        //Bottom while loop for all other procedures related to being logged into the database
        while(top){
            boolean validLogin = false;
            bot = true;
            System.out.println("Please select from the following options.");
            System.out.println("\t 1. Create new account");
            System.out.println("\t 2. Enter account information");
            System.out.println("\t 3. Leave Program");

            String username = "";

            try{
                int option = scan.nextInt();
                switch (option){
                    case 1:
                        //ask user for username,password
                        System.out.println("Username: ");
                        String tempUsername = scan.next();
                        System.out.println("Password: ");
                        String tempPassword = scan.next();
                        String date = LocalDate.now().toString();

                        createAccount(tempUsername, tempPassword, date, conn);
                        break;
                    case 2:
                        System.out.println("Username: ");
                        username = scan.next();
                        System.out.println("Password: ");
                        String password = scan.next();
                        validLogin = login(username, password, conn);
                        break;
                    case 3:
                        if (!conn.isClosed()) {
                            System.out.println("Closing Database Connection");
                            conn.close();
                        }
                        if (session != null && session.isConnected()) {
                            System.out.println("Closing SSH Connection");
                            session.disconnect();
                        }
                        System.exit(0);
                    default:
                        System.out.println("Invalid input, try again");
                        break;
                }
            } catch(InputMismatchException | SQLException ime){
                System.out.println("Sorry, the input you entered wasn't an integer");
            }

            while(bot && validLogin){
                System.out.println("Please select from the following options.");
                System.out.println("\t 1. Create, Edit, or Delete a Recipe");
                System.out.println("\t 2. Categorize a Recipe");
                System.out.println("\t 3. Search a Recipe");
                System.out.println("\t 4. Sort Recipes");
                System.out.println("\t 5. Mark a Recipe");
                System.out.println("\t 0. Exit");

                try{
                    int userChoice = scan.nextInt();
                    switch (userChoice){
                        case 1:
                            System.out.println("Please select from the following options.");
                            System.out.println("\t 1. Create a Recipe");
                            System.out.println("\t 2. Edit a Recipe");
                            System.out.println("\t 3. Delete a Recipe");
                            RecipeFunctionality rf = new RecipeFunctionality(username, conn);
                            try{
                                int userChoice2 = scan.nextInt();
                                switch(userChoice2){
                                    case 1:
                                        System.out.println("Recipe Name: ");
                                        String recipeName = scan.next();
                                        System.out.println("Difficulty(easy, medium, hard): ");
                                        String difficulty = scan.next();
                                        System.out.println("Steps(One long description): ");
                                        String steps = scan.next();
                                        System.out.println("Description: ");
                                        String description = scan.next();
                                        System.out.println("Cook time: ");
                                        String cook_time = scan.next();
                                        System.out.println("Servings: ");
                                        String servings = scan.next();

                                        rf.createRecipe(recipeName, difficulty, steps, description, cook_time, servings);
                                        break;
                                    case 2:
                                        //edit recipe
                                        break;
                                    case 3:
                                        //delete recipe
                                        break;
                                    default:
                                        break;
                                }
                            } catch(InputMismatchException ime){
                                System.out.println("Sorry, the input you entered wasn't an integer");
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                            break;
                        case 2:
                            //database work
                            break;
                        case 3:
                            //database work
                            break;
                        case 4:
                            //database work
                            break;
                        case 5:
                            //database work
                            break;
                        case 0:
                            bot = false;
                            validLogin = false;
                            break;
                        default:
                            System.out.println("Invalid input, try again");
                            break;
                    }
                } catch(InputMismatchException ime){
                    System.out.println("Sorry, the input you entered wasn't an integer");
                }
            }
        }

    }
/*
    public static String catchInput(String label, Scanner scan){
        System.out.println(label);
        String output = "";
        boolean isValidResponse = false;
        while(!isValidResponse){
            try{
                output = scan.next();
                isValidResponse = true;
            }catch (InputMismatchException e){
                System.out.println("Error, please enter a string");
                System.out.println(label);
            }
        }
        return output;
    }*/

    /***
     * Create a new account within the database
     * @param username A username created by the user
     * @param password A password created by the user
     * @param currentDate Current Date the users account was created
     * @param conn Connection to the database
     * @throws SQLException If the username already exists an error will be thrown and caught
     */
    public void createAccount(String username, String password, String currentDate, Connection conn) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO public.\"user\" (username, password, created_date, last_accessed)\n" +
                "VALUES ('" + username + "', '" + password + "', '" + currentDate + "', '" + currentDate + "')");
        try{
            preparedStatement.executeUpdate();
        }catch (PSQLException e){
            System.out.println("Username has already been taken");
        }
    }

    /***s
     * Check if the user entered a valid username and password, if the user enters a valid username they must also enter
     * a valid password to be allowed to modify tables and do other operations.
     * @param username
     * @return
     */
    public boolean login(String username, String password, Connection conn) throws SQLException {
        boolean isCorrect = false;
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT exists(SELECT 1 FROM \"user\" WHERE username = 'kevin')");
        if (preparedStatement.execute()){
            PreparedStatement ps = conn.prepareStatement("SELECT password FROM \"user\" where username = '" + username + "'");
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            try{
                String databasePassword = resultSet.getString(1);
                if(databasePassword.equals(password)){
                    System.out.println("Logged in, correct password");
                    isCorrect = true;
                }else {
                    System.out.println("Try again, incorrect password");
                }
            } catch(PSQLException e){
                System.out.println("Try again, make sure that the correct password is being entered for the respective username");
            }
        }else{
            System.out.println("The username you just entered doesn't exist, try again.");
        }
        return isCorrect;
    }
}
