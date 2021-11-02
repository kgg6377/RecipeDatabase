import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RecipeFunctionality {
    String username;
    Connection connection;

    public RecipeFunctionality(String username, Connection connection){
        this.username = username;
        this.connection = connection;
    }

    public void createRecipe(String name, String difficulty, String steps, String description, String cook_time, String servings, String username) throws SQLException {
        String value = "'" + name + "'" + "," + "'" +difficulty +"'"+ "," + "'"+steps+"'" + "," + "'"+description+"'" + "," + "'"+cook_time+"'" + "," + "'"+servings+"'"+ "," + "'"+username+"'";
        System.out.println(value);
        PreparedStatement ps = connection.prepareStatement("INSERT INTO recipe (name, difficulty, steps, description, cook_time, servings, username) " +
                "VALUES(" + value + ")");
        ps.executeUpdate();
    }

    public void editRecipe(String column, String change, String recipe, String username)throws SQLException{
        PreparedStatement ps = connection.prepareStatement("UPDATE recipe set " + column + " = '" + change +"' WHERE name = '" + recipe +"' AND username = '" + username +"'");
        System.out.println("UPDATE recipe set " + column + " = '" + change +"' WHERE name = '" + recipe +"' AND username = '" + username +"'");
        try{
            ps.executeUpdate();
        }catch(PSQLException e){
            System.out.println("Error, please check input and verify all parameters map to an existing value");
        }
    }

    public void deleteRecipe(String rowName, String username) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DELETE FROM recipe WHERE name = '" + rowName +"' AND username = '" + username +"'");
        try{
            ps.executeUpdate();
        }catch(PSQLException e){
            System.out.println("Error, please check input and verify name exists");
        }
    }

}
