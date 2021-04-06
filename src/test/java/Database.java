import java.sql.*;

import org.junit.Test;
public class Database {

    public void Database() {
        try(Connection connection = connectionDo()) {
            createTable(connection);

            //selectAll(connection);
            //update(connection, "fdfd", "34343");
        }
        catch (Exception e){

        }
    }

    private Connection connectionDo() throws SQLException{
        String url = "jdbc:sqlite:sqlite/users.db";

        Connection open = DriverManager.getConnection(url);
        System.out.println("Success");
        return open;
    }

    private void createTable(Connection connection) {
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"

                + "name text NOT NULL,\n"
                + "password text NOT NULL\n"
                + ");";
        try(Statement statement = connection.createStatement()){
            statement.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(Connection connection, String name, String password) throws SQLException{
        String sql = "INSERT INTO users(name, password) VALUES(?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setString(1,name);
            preparedStatement.setString(2,password);
            preparedStatement.executeUpdate();
        }
    }

    private void selectAll(Connection connection) {
        String sql = "SELECT name, password FROM users";

        try(Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql)) {

            System.out.println("name\tpassword");

            while(rs.next()){
                System.out.println(
                        rs.getString("name") + "\t" +
                                rs.getString("password"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void update(Connection connection, String name, String password) throws SQLException{
        String sql = "UPDATE users SET "
                + "password = ? "
                + "WHERE name = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setString(1,password);
            preparedStatement.setString(2, name);
            preparedStatement.executeUpdate();
        }

    }
}
