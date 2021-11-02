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

    public void createRecipe(String name, String difficulty, String steps, String description, String cook_time, String servings) throws SQLException {
        String value = "'" + name + "'" + "," + "'" +difficulty +"'"+ "," + "'"+steps+"'" + "," + "'"+description+"'" + "," + "'"+cook_time+"'" + "," + "'"+servings+"'";
        PreparedStatement ps = connection.prepareStatement("INSERT INTO recipe (name, difficulty, steps, description, cook_time, servings) " +
                "VALUES(" + value + ")");
        ps.executeUpdate();
    }

}
