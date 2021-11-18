import org.postgresql.util.PSQLException;

import java.sql.*;

public class RecipeFunctionality {
    String username;
    Connection connection;

    public RecipeFunctionality(String username, Connection connection){
        this.username = username;
        this.connection = connection;
    }

    public void createRecipe(String name, String difficulty, String steps, String description, String cook_time, String servings, String rating, String creation_date, String created_by, String category) throws SQLException {
        String value = "'" + name + "'" + "," + "'" +difficulty +"'"+ "," + "'"+steps+"'" + "," + "'"+description+"'" + "," + "'"+cook_time+"'" + "," + "'"+servings+"'"+ "," + "'"+rating+"'"
                + "," + "'"+creation_date+"'" + "," + "'"+created_by+"'" + "," + "'"+category+"'";
        System.out.println(value);
        PreparedStatement ps = connection.prepareStatement("INSERT INTO recipe (name, difficulty, steps, description, cook_time, servings, rating, creation_date, created_by, category) " +
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

}
