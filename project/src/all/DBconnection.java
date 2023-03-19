package all;
 
 import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {

	public Connection db_connector() {
 

		try {
		 
			Class.forName("org.sqlite.JDBC");
			Connection connection = DriverManager.getConnection("jdbc:sqlite:MenuHotPizza.db");
			return connection;

		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			return null;
		}

	}

	public boolean is_connection() {

		Connection db_connector = db_connector();
		if (db_connector == null) {
			System.exit(1);

		} else
			try {
				return !db_connector.isClosed();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e);

			}

		return false;

	}

}
