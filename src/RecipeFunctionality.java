import org.postgresql.util.PSQLException;

import java.sql.*;

public class RecipeFunctionality {
    String username;
    Connection connection;

    public RecipeFunctionality(String username, Connection connection){
        this.username = username;
        this.connection = connection;
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

    public void inPantryRecommend() throws SQLException{
        //in the pantry
    }

    public void forYouRecommend() throws SQLException{
        //for you
    }


    //Query to find categories a recipe is related to, most likely can be used for other foreign key problems
    //SELECT category from categories inner join recipes r on r.name = categories."recipeName" where r.name = '2'
}
