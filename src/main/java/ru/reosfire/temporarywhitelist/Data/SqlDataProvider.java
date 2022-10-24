package ru.reosfire.temporarywhitelist.Data;

import ru.reosfire.temporarywhitelist.Configuration.Config;
import ru.reosfire.temporarywhitelist.Lib.Sql.SqlConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SqlDataProvider implements IDataProvider
{
    private final Config configuration;
    private final SqlConnection sqlconnection;

    public SqlDataProvider(Config configuration) throws SQLException
    {
        this.configuration = configuration;

        sqlconnection = new SqlConnection(this.configuration.SqlConfiguration);

        String createTableString = "CREATE TABLE IF NOT EXISTS `" + this.configuration.SqlTable + "` (" +
                "Player VARCHAR(32) NOT NULL UNIQUE, " +
                "Permanent BOOLEAN NOT NULL, " +
                "LastStartTime BIGINT NOT NULL, " +
                "TimeAmount BIGINT NOT NULL);";
        try (Connection conn = sqlconnection.getConnection(); Statement statement = conn.createStatement())
        {
            statement.executeUpdate(createTableString);
        }
    }

    @Override
    public CompletableFuture<Void> update(PlayerData playerData)
    {
        return CompletableFuture.runAsync(() ->
        {
            String setRequest = "INSERT INTO " + configuration.SqlTable + " (Player, Permanent, LastStartTime, " +
                    "TimeAmount)" + "VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE Permanent=?, LastStartTime=?, " +
                    "TimeAmount=?;";
            try (Connection conn = sqlconnection.getConnection(); PreparedStatement statement = conn.prepareStatement(setRequest))
            {
                statement.setString(1, playerData.Name);
                statement.setBoolean(2, playerData.Permanent);
                statement.setLong(3, playerData.StartTime);
                statement.setLong(4, playerData.TimeAmount);
                statement.setBoolean(5, playerData.Permanent);
                statement.setLong(6, playerData.StartTime);
                statement.setLong(7, playerData.TimeAmount);

                statement.executeUpdate();
            }
            catch (Exception e)
            {
                throw new RuntimeException("Error while updating player data for: " + playerData.Name, e);
            }
        });
    }

    @Override
    public CompletableFuture<Void> remove(String playerName)
    {
        return CompletableFuture.runAsync(() ->
        {
            String removeRequest = "DELETE FROM " + configuration.SqlTable + " WHERE Player=?;";
            try(Connection conn = sqlconnection.getConnection(); PreparedStatement statement = conn.prepareStatement(removeRequest))
            {
                statement.setString(1, playerName);
                statement.executeUpdate();
            }
            catch (Exception e)
            {
                throw new RuntimeException("Error while removing player data about: " + playerName, e);
            }
        });
    }

    @Override
    public PlayerData get(String playerName)
    {
        String selectRequest = "SELECT * FROM " + configuration.SqlTable + " WHERE Player=?;";
        try(Connection conn = sqlconnection.getConnection(); PreparedStatement statement = conn.prepareStatement(selectRequest))
        {
            statement.setString(1, playerName);
            try (ResultSet player = statement.executeQuery()) {
                if (!player.next()) return null;
                return new PlayerData(player);
            }
        }
        catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public List<PlayerData> getAll()
    {
        String selectRequest = "SELECT * FROM " + configuration.SqlTable + ";";
        try (Connection conn = sqlconnection.getConnection(); Statement statement = conn.createStatement())
        {
            List<PlayerData> result = new ArrayList<>();
            try (ResultSet resultSet = statement.executeQuery(selectRequest)) {
                while (resultSet.next())
                {
                    result.add(new PlayerData(resultSet));
                }
                return result;
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error while getting all data");
        }
    }
}