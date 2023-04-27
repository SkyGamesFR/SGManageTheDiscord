package fr.skygames.managethediscord.sql;

import fr.skygames.managethediscord.Main;
import fr.skygames.managethediscord.utils.exception.MissingPropertyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class SqlConnector {
	
    private Connection connect;
    private SqlProperties sqlProperties;
	private final Logger logger = LoggerFactory.getLogger(SqlConnector.class);

    public SqlConnector() throws MissingPropertyException, ClassNotFoundException, SQLException, IOException {
		this.getConnection(); // init connection
		this.initQuery();
	}
    
    public Connection getConnection() throws MissingPropertyException, ClassNotFoundException, SQLException{
        if(this.connect == null || this.connect.isClosed() || !this.connect.isValid(1)){
            	Class.forName("org.mariadb.jdbc.Driver");
            	
            	this.sqlProperties = new SqlProperties();
            	
            	String url = this.sqlProperties.buildUrl();
            	
            	Properties properties = this.sqlProperties.buildConnectionProperties();
            	
            	properties.put("useUnicode", "true");
            	properties.put("useJDBCCompliantTimezoneShift", "true");
            	properties.put("useLegacyDatetimeCode", "false");
            	properties.put("serverTimezone", "UTC");
            	properties.put("autoReconnect", "true");
				properties.put("useSSL", "false");
				properties.put("passwordCharacterEncoding", "UTF-8");

            	logger.debug("DriverManager.getConnection...");
                connect = DriverManager.getConnection(url,properties);
                connect.setAutoCommit(true);
                
        }
        return connect;
    }

	private void initQuery() throws SQLException, ClassNotFoundException, MissingPropertyException, IOException{
		
		final String filename = "query.sql";
		InputStream input = Main.class.getClassLoader().getResourceAsStream(filename);
		
		if (input == null) {
			logger.error("Sorry, unable to find " + filename + " in JAR Resources");
			throw new SQLException("query.sql init script error !");
		}
		
		String newLine = System.getProperty("line.separator");
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		StringBuilder result = new StringBuilder();
		boolean flag = false;
		for (String line; (line = reader.readLine()) != null; ) {
		    result.append(flag? newLine: "").append(line);
		    flag = true;
		}
		
		for (String query : result.toString().split("-- ##new_query")) {
			
			Statement stmt = this.getConnection().createStatement();
			logger.warn("Executing query : " + query);
			stmt.executeUpdate(query);
			stmt.close();
			
		}
		
	}
}