package animalsBeans;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.annotation.Resource;
import javax.annotation.sql.DataSourceDefinition;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.sql.DataSource;

@DataSourceDefinition(
        name = "java:global/jdbc/animals_database",
        className = "org.apache.derby.jdbc.ClientDataSource",
        url = "jdbc:derby://localhost:1527/animals_database",
        databaseName = "animals_database",
        user = "ozhar86",
        password = "ozhar86"
)

@ManagedBean(eager=true)
@ApplicationScoped
public class DBLoader {
    @Resource(lookup = "java:global/jdbc/animals_database")
    private DataSource dataSource;
    Connection connection; 
    
    public class Statement
    {
        private PreparedStatement prpStatement;
        private int index;
        
        public Statement(PreparedStatement prpSt)
        {
            index = 1;
            prpStatement = prpSt;
        }
        
        public void addString(String str) throws SQLException
        {
            prpStatement.setString(index++, str);
        }
        
        public void addInt(int number) throws SQLException
        {
            prpStatement.setInt(index++, number);
        }
        
        public void addDate(Date date) throws SQLException
        {
            prpStatement.setDate(index++, date);
        }
        
        public void addBoolean(boolean bool) throws SQLException
        {
            prpStatement.setBoolean(index++, bool);
        }
        
        public void addLong(long num) throws SQLException
        {
            prpStatement.setLong(index++, num);
        }
    }
    
    public class ResultQuary
    {
        private ResultSet res;
        
        public ResultQuary(ResultSet res)
        {
            this.res = res;
        }
        
        public boolean next() throws SQLException
        {
            return res.next();
        }
        
        public int getIntForAttribute(String attribute) throws SQLException
        {
            return res.getInt(attribute);
        }
        
        public String getStringForAttribute(String attribute) throws SQLException
        {
            return res.getString(attribute);
        }
        
        public Date getDateForAttribute(String attribute) throws SQLException
        {
            return res.getDate(attribute);
        }
        
        public boolean getBooleanForAttribute(String attribute) throws SQLException
        {
            return res.getBoolean(attribute);
        }
        
        public long getLongForAttribute(String attribute) throws SQLException
        {
            return res.getLong(attribute);
        }
    }
    
    public Statement getStatement(String command) throws SQLException
    {
        try
        {
            if (dataSource != null)
            {
                connection = dataSource.getConnection();
                if (connection == null)
                {
                        return null;
                }
                return new Statement(connection.prepareStatement(command));   
            }
        }
        finally
        {
            connection.close();
            connection = null;
        }
        return null;
    }
    
    public void executeUpdate(Statement statement) throws SQLException
    {
        statement.prpStatement.executeUpdate();
    }
    
    public ResultQuary executeQuary(Statement statement) throws SQLException
    {
        return new ResultQuary(statement.prpStatement.executeQuery());
    }
    
}
