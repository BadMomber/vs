package server;

import java.sql.*;

public class PsqlDatabase {
    int messageCounter = 0;
    String server;

    public Connection connect() throws SQLException {
        System.out.println("Trying to connect to server: " + server);
        return DriverManager.getConnection("jdbc:postgresql://" + this.server + ":5432/server_db", "lekko", "123456789");
    }

    public int sendData(String json, Boolean _testMode) {

        try (Connection connection = connect()) {
            String sql = "INSERT INTO public.messages(valueset) VALUES( ? ) ON CONFLICT (valueset) DO NOTHING";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, json);
            preparedStatement.executeUpdate();
            connection.close();
            this.messageCounter++;
        } catch (SQLException e) {
            System.out.println("Connection failure.");
            System.out.println("Error writing in database: " + e);
        }

        return this.messageCounter;
    }

    public int getMessageCount() {
        String SQL = "SELECT count(*) FROM server_tank";
        int count = 0;

        try (Connection conn = connect()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            rs.next();
            count = rs.getInt(1);
            conn.close();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return count;
    }

    public Timestamp getLastMessage() {
        String SQL = "SELECT timestamp from server_tank ORDER BY timestamp DESC LIMIT 1";
        Timestamp ts = new Timestamp(System.currentTimeMillis());

        try (Connection conn = connect()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            rs.next();
            ts = rs.getTimestamp(1);
            conn.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return ts;
    }

    public Timestamp getFirstMessage() {
        String SQL = "SELECT timestamp from server_tank ORDER BY timestamp ASC LIMIT 1";
        Timestamp ts = new Timestamp(System.currentTimeMillis());

        try (Connection conn = connect()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            rs.next();
            ts = rs.getTimestamp(1);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return ts;
    }

}
