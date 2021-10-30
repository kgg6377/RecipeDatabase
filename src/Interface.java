import Objects.*;
import java.util.*;

public class Interface {

    public static void main(String[] args) {
        System.out.println("Hello");

    }

    /***
     * Main UI function
     */
    public void RunUI(){
        Scanner scan = new Scanner(System.in);

        System.out.println("Welcome to the Recipe Domain UI");
        boolean top = true, bot = true;

        //Two while loops
        //Top while loop for creating a new account and logging in
        //Bottom while loop for all other procedures related to being logged into the database
        while(top){
            boolean validLogin = false;

            System.out.println("Please select from the following options.");
            System.out.println("\t 1. Create new account");
            System.out.println("\t 2. Enter account information");
            System.out.println("\t 3. Exit");

            try{
                int option = scan.nextInt();
                switch (option){
                    case 1:
                        //ask user for username,password
                        System.out.println("Username: ");
                        String username = scan.nextLine();
                        System.out.println("Password: ");
                        String password = scan.nextLine();
                        //insert user into database
                        break;
                    case 2:
                        //call login function
                        //validLogin = login(String username, String password);
                        break;
                    case 3:
                        System.exit(0);
                    default:
                        System.out.println("Invalid input, try again");
                        break;
                }
            } catch(InputMismatchException ime){
                System.out.println("Sorry, the input you entered wasn't an integer");
            }

            while(bot && validLogin){
                System.out.println("Please select from the following options.");
                System.out.println("\t 1. Create, Edit, or Delete a Recipe");
                System.out.println("\t 2. Categorize a Recipe");
                System.out.println("\t 3. Search a Recipe");
                System.out.println("\t 4. Remove an Entry");
                System.out.println("\t 5. Print out phone book");
                System.out.println("\t 0. Exit");

                int userChoice = scan.nextInt();
                break;
            }
            break;
        }
    }

    /***
     * Check if the
     * @param username
     * @return
     */
    public boolean login(String username, String password){
        //we will need to create a function to check the tables
        //if user name exists first
        //  if name exists allow the user to "login", return true
        //  else return false
        //else return false
        return false;
    }

}
