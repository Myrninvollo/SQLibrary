package lib.PatPeter.SQLibrary;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;
import lib.PatPeter.SQLibrary.Delegates.HostnameDatabase;
import lib.PatPeter.SQLibrary.Factory.DatabaseFactory;

/**
 * Child class for the Informix database.<br>
 * Date Created: 2012-12-18 06:20.
 * 
 * @author Nicholas Solin, a.k.a. PatPeter
 */
public class Informix extends Database {
	private HostnameDatabase delegate = DatabaseFactory.hostname();
	
	public enum Statements implements StatementEnum {}
	
	public Informix(Logger log,
					String prefix,
					String hostname,
					int port,
					String database,
					String username,
					String password) {
		super(log,prefix,"[Informix] ");
		setHostname(hostname);
		setPort(port);
		setDatabase(database);
		setUsername(username);
		setPassword(password);
		this.driver = DBMS.Informix;
	}
	
	public Informix(Logger log,
					String prefix,
					String database,
					String username,
					String password) {
		super(log,prefix,"[Informix] ");
		setHostname("localhost");
		setPort(1526);
		setDatabase(database);
		setUsername(username);
		setPassword(password);
		this.driver = DBMS.Informix;
	}
	
	public Informix(Logger log,
					String prefix,
					String database,
					String username) {
		super(log,prefix,"[Informix] ");
		setHostname("localhost");
		setPort(1526);
		setDatabase(database);
		setUsername(username);
		setPassword("");
		this.driver = DBMS.Informix;
	}
	
	public Informix(Logger log,
					String prefix,
					String database) {
		super(log,prefix,"[Informix] ");
		setHostname("localhost");
		setPort(1526);
		setDatabase(database);
		setUsername("");
		setPassword("");
		this.driver = DBMS.Informix;
	}
	
	public String getHostname() {
		return delegate.getHostname();
	}
	
	private void setHostname(String hostname) {
		delegate.setHostname(hostname);
	}
	
	public int getPort() {
		return delegate.getPort();
	}
	
	private void setPort(int port) {
		delegate.setPort(port);
	}
	
	public String getUsername() {
		return delegate.getUsername();
	}
	
	private void setUsername(String username) {
		delegate.setUsername(username);
	}
	
	private String getPassword() {
		return delegate.getPassword();
	}
	
	private void setPassword(String password) {
		delegate.setPassword(password);
	}
	
	public String getDatabase() {
		return delegate.getDatabase();
	}
	
	private void setDatabase(String database) {
		delegate.setDatabase(database);
	}

	@Override
	protected boolean initialize() {
		try {
			Class.forName("com.informix.jdbc.IfxDriver");
			return true;
	    } catch (ClassNotFoundException e) {
	    	this.writeError("Informix driver class missing: " + e.getMessage() + ".", true);
	    	return false;
	    }
	}

	@Override
	public boolean open() {
		if (initialize()) {
			String url = "jdbc:informix-sqli://" + getHostname() + ":" + getPort() + ":informixserver=" + getDatabase() + ";";
			try {
				this.connection = DriverManager.getConnection(url, getUsername(), getPassword());
				return true;
			} catch (SQLException e) {
				this.writeError("Could not establish a Informix connection, SQLException: " + e.getMessage(), true);
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	protected void queryValidation(StatementEnum statement) throws SQLException {}

	@Override
	public StatementEnum getStatement(String query) throws SQLException {
		String[] statement = query.trim().split(" ", 2);
		try {
			Statements converted = Statements.valueOf(statement[0].toUpperCase());
			return converted;
		} catch (IllegalArgumentException e) {
			throw new SQLException("Unknown statement: \"" + statement[0] + "\".");
		}
	}

	@Override
	public boolean isTable(String table) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean truncate(String table) {
		throw new UnsupportedOperationException();
	}
}
