import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DatabaseConnectionTest {

    public static void main(String[] args) {

        DatabaseManager db = new DatabaseManager();

        try (Connection connection = db.connect(args[0]);
             Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery(
                     "SELECT COUNT(*) FROM properties")) {

            System.out.println("Database connected successfully!");
            System.out.println("Total properties: " + results.getInt(1));
            System.out.println("Properties loaded as Java objects: "
                + db.getAllProperties(args[0]).size());

        } catch (Exception ex) {
            System.out.println("Connection failed: " + ex.getMessage());
        }

        



}}