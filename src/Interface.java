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
            bot = true;
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
                            try{
                                int userChoice2 = scan.nextInt();
                                switch(userChoice2){
                                    case 1:
                                        //create recipe
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
