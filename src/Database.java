import com.jcraft.jsch.*;
import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

class PostgresSSHTest {

    public static void main(String[] args) throws SQLException {
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


            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO public.\"user\" (username, password, created_date, last_accessed)\n" +
                    "VALUES ('paddy', 'paddy2', '02/06/2001', '023/124/1242')");
            try{
                preparedStatement.executeUpdate();
            }catch (PSQLException e){
                System.out.println("Erorr: " + e);
                System.out.println("Username has already been taken");
            }

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

}
