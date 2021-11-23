import org.postgresql.util.PSQLException;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.Scanner;

public class RecipeFunctionality {
    String username;
    Connection connection;

    public RecipeFunctionality(String username, Connection connection){
        this.username = username;
        this.connection = connection;
    }

    public void insertRecipeIngredients(String recipeName) throws SQLException {
        System.out.println("Please Enter Recipe Ingredients");
        Scanner scan = new Scanner(System.in);

        while(true){
            System.out.println("Ingredient Name: ");
            String name = scan.nextLine();
            System.out.println("Amount: ");
            String qty = scan.nextLine();
            String SQLStatement = "INSERT INTO recipe_ingredients (qty, name, recipe) VALUES ('"+qty+"','"+name+"','"+recipeName+"')";

            PreparedStatement ps = connection.prepareStatement(SQLStatement);
            ps.executeUpdate();
            ps.clearParameters();

            System.out.println("\t 1. Continue");
            System.out.println("\t 2. Exit");
            int userChoice = scan.nextInt();
            if(userChoice == 2) break;
            scan.nextLine();
        }

    }

    public void createRecipe(String name, String difficulty, String steps, String description, String cook_time, String servings, String rating, String creation_date, String created_by) throws SQLException {
        String value = "'" + name + "'" + "," + "'" +difficulty +"'"+ "," + "'"+steps+"'" + "," + "'"+description+"'" + "," + "'"+cook_time+"'" + "," + "'"+servings+"'"+ "," + "'"+rating+"'"
                + "," + "'"+creation_date+"'" + "," + "'"+created_by+"'";
        System.out.println(value);
        PreparedStatement ps = connection.prepareStatement("INSERT INTO recipes (name, difficulty, steps, description, cook_time, servings, rating, creation_date, created_by) " +
                "VALUES(" + value + ")");
        ps.executeUpdate();
    }

    public void editRecipe(String column, String change, String recipe, String username)throws SQLException{
        //validate
        if(!validateAccess(username, recipe)) {
            System.out.println("You cannot modify this recipe, you are not the owner...");
            return;
        }

        PreparedStatement ps = connection.prepareStatement("UPDATE recipe set " + column + " = '" + change +"' WHERE name = '" + recipe +"' AND created_by = '" + username +"'");
        try{
            ps.executeUpdate();
        }catch(PSQLException e){
            System.out.println("Error, please check input and verify all parameters map to an existing value");
        }
    }

    public void deleteRecipe(String recipe, String username) throws SQLException {
        if(!validateAccess(username, recipe)) {
            System.out.println("This recipe doesn't exist or you are not the owner");
            return;
        }
        PreparedStatement ps = connection.prepareStatement("DELETE FROM recipe WHERE name = '" + recipe +"' AND created_by = '" + username +"'");
        try{
            ps.executeUpdate();
        }catch(PSQLException e){
            System.out.println("Error, please check input and verify name exists");
        }
    }

    public boolean validateAccess(String username, String recipeName) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT name FROM recipe WHERE created_by = '" + username +"' AND name = '" + recipeName +"'");
        ResultSet resultSet = ps.executeQuery();
        resultSet.next();

        try{
            String result = resultSet.getString(1);
        }catch (PSQLException e){
            return false;
        }
        return true;
    }

    public void categorizeRecipe(String category, String recipeName) throws SQLException{
        PreparedStatement ps = connection.prepareStatement("Select category from categories where  "+ "\"recipeName\"" + "= '"+ recipeName +"' and category = '"+ category +"'");
        ResultSet resultSet = ps.executeQuery();
        resultSet.next();

        //check if category already exists for the recipeName
        //if true -> do nothing return
        //else -> enter category into database
        try{
            String result = resultSet.getString(0);
            System.out.println("Category/Recipe already has been categorized, please try again");
            return;
        }catch (PSQLException ignored){ }
        ps.clearParameters();
        ps = connection.prepareStatement("INSERT INTO categories (category, "+ "\"recipeName\"" +") VALUES ('"+ category +"', '"+ recipeName +"')");
        ps.executeUpdate();
    }

    public void topRatedRecommend() throws SQLException{
        PreparedStatement ps = connection.prepareStatement("Select name from recipes order by rating desc limit 50");
        ResultSet resultSet = ps.executeQuery();

        try{
            System.out.println("Top 50 Rated Recipes:");
            for(int i = 0; i < 50; i++) {
                if(resultSet.next()) {
                    System.out.println(resultSet.getString(1));
                } else
                    break;
            }
        }catch (PSQLException ignored){ }
        System.out.println();
        ps.clearParameters();
    }

    public void mostRecentRecommend() throws SQLException{
        PreparedStatement ps = connection.prepareStatement("Select name from recipes order by creation_date desc limit 50");
        ResultSet resultSet = ps.executeQuery();

        try{
            System.out.println("Top 50 Most Recent Recipes:");
            System.out.println("---------------------------");
            for(int i = 0; i < 50; i++) {
                if(resultSet.next()) {
                    System.out.println(resultSet.getString(1));
                } else
                    break;
            }
        }catch (PSQLException ignored){ }
        System.out.println();
        ps.clearParameters();
    }

    public void inPantryRecommend(String username) throws SQLException{
        PreparedStatement ps = connection.prepareStatement("select recipe, count(name) from recipe_ingredients ri inner join pantry_ingredients pi on " +
                "ri.qty < pi.current_qty and ri.name=pi.ingredient_name and pi.username='" + username + "' group by recipe");
        ResultSet resultSet = ps.executeQuery();
        ps.clearParameters();
        System.out.println("In the Pantry Recipes:");
        System.out.println("----------------------");
        while(resultSet.next()) {
            String recipe = resultSet.getString(1);
            ps = connection.prepareStatement("select recipe, count(name) from recipe_ingredients where recipe='" + recipe + "' group by recipe");
            ResultSet resultSet1 = ps.executeQuery();
            ps.clearParameters();
            resultSet1.next();

            if(resultSet.getString(2).equals(resultSet1.getString(2))) {
                System.out.println(resultSet.getString(1));
            }
        }
        System.out.println();
    }

    public void forYouRecommend() throws SQLException{
        System.out.println("Recipes Recommended for You:");
        System.out.println("----------------------------");
        System.out.println("Not implemented yet. Sorry :(");
    }

    public int getIngQty(String name) throws SQLException{
        try {
            PreparedStatement ps = connection.prepareStatement("select current_qty from pantry_ingredients where ingredient_name='" + name + "'");
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            ps.clearParameters();
            return resultSet.getInt(1);
        } catch (SQLException sqlException) {
            return 0;
        }
    }

    public boolean ingExists(String name) throws SQLException{
        PreparedStatement ps = connection.prepareStatement("select ingredient from pantry where ingredient='" + name + "'");
        ResultSet resultSet = ps.executeQuery();
        return resultSet.next();
    }

    public void editCurrentQty(String name, int qty) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("select qty_bought, current_qty from pantry_ingredients where ingredient_name='" + name + "'");
        ResultSet resultSet = ps.executeQuery();
        resultSet.next();
        ps.clearParameters();

        int qtyBought = resultSet.getInt(1);
        int currentQty = resultSet.getInt(2);
        if (qty >= 0) {
            qtyBought += qty;
        }
        currentQty += qty;

        ps = connection.prepareStatement("update pantry_ingredients set qty_bought = " + qtyBought + ", current_qty = " + currentQty + "where ingredient_name = '" + name + "'");
        ps.executeUpdate();
        ps.clearParameters();
    }

    public void addPantryIng(String name, String date, int qty, String exp, int currQty, String aisle, String username) throws SQLException{
        currQty += qty;
        PreparedStatement ps = connection.prepareStatement("insert into pantry (ingredient, aisle, username) " +
                "VALUES( '" + name + "','" + aisle + "','" + username + "')");
        ps.executeUpdate();
        String values = "'" + name + "','" + date + "','" + qty + "','" + exp + "','" + currQty + "','" + username + "'";
        System.out.println(values);
        ps = connection.prepareStatement("insert into pantry_ingredients (ingredient_name, purchase_date, qty_bought, expiration_date, current_qty, username) " +
                "VALUES(" + values + ")");
        ps.executeUpdate();
        ps.clearParameters();
    }

    //Query to find categories a recipe is related to, most likely can be used for other foreign key problems
    //SELECT category from categories inner join recipes r on r.name = categories."recipeName" where r.name = '2'

    //SELECT * FROM recipes where name = '1' ORDER BY name ASC

    //TODO Not sure if what we should return  just the recipe name or the entire recipe row
    public void searchRecipe(String sort, int searchParameter) throws SQLException {
        PreparedStatement ps;
        //Sort by name after filtered by search parameter
        if(searchParameter == 1){
            String s = "SELECT * from recipes inner join recipe_ingredients ri on recipes.name = ri.recipe where ri.name = '"+ sort +"' ORDER BY recipes.name ASC";
            ps = connection.prepareStatement("SELECT * FROM recipes inner join recipe_ingredients ri on recipes.name = ri.recipe where ri.name = '"+ sort +"' ORDER BY recipes.name ASC");
            System.out.println(s);
        }else if (searchParameter == 2){
            ps = connection.prepareStatement("SELECT * FROM recipes where name = '"+ sort +"' ORDER BY name ASC");
        }else {
            ps = connection.prepareStatement("SELECT * FROM recipes inner join categories c on recipes.name = c.\"recipeName\" where c.category = '" + sort + "' ORDER BY name ASC");
        }
        ResultSet resultSet = ps.executeQuery();

        try{
            System.out.println("Search Results:");
            for(int i = 0; i < Integer.MAX_VALUE; i++) {
                if(resultSet.next()) {
                    StringBuilder result = new StringBuilder();
                    for(int z = 1; z <  10; z++){
                        if(z == 9){
                            result.append(resultSet.getString((z)));
                            break;
                        }
                        result.append(resultSet.getString(z)).append(", ");
                    }
                    System.out.println(result.toString());
                } else
                    break;
            }
        }catch (PSQLException ignored){ }
        System.out.println();
    }
}
