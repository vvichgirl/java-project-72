package repository;

import model.Url;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlsRepository extends BaseRepository {
    public static void save(Url url) throws SQLException {
        String sql = "INSERT INTO urls (name, created_at) VALUES (?, ?)";
        try (
                var conn = dataSource.getConnection();
                var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            preparedStatement.setString(1, url.getName());
            preparedStatement.setTimestamp(2, url.getCreatedAt());
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                url.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("DB have not returned an id after saving an entity");
            }
        }
    }

    public static Optional<Url> getUrlById(Long id) throws SQLException {
        var sql = "SELECT * FROM urls WHERE id = ?";
        try (
            var conn = dataSource.getConnection();
            var stmt = conn.prepareStatement(sql)
        ) {
            stmt.setLong(1, id);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                var urlTitle = resultSet.getString("name");
                var createdAt = resultSet.getTimestamp("created_at");
                var url = new Url(urlTitle, createdAt);
                url.setId(id);
                return Optional.of(url);
            }
            return Optional.empty();
        }
    }

    public static Optional<Url> getUrlByName(String urlName) throws SQLException {
        var sql = "SELECT * FROM urls WHERE name = ?";
        try (
            var conn = dataSource.getConnection();
            var stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, urlName);
            var resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                var urlTitle = resultSet.getString("name");
                var createdAt = resultSet.getTimestamp("created_at");
                var url = new Url(urlTitle, createdAt);
                url.setId(resultSet.getLong("id"));
                return Optional.of(url);
            }

            return Optional.empty();
        }
    }

    public static boolean existUrl(String url) throws SQLException {
        var sql = "SELECT * FROM urls WHERE name = ?";
        try (
                var conn = dataSource.getConnection();
                var stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, url);
            var resultSet = stmt.executeQuery();
            return resultSet.next();
        }
    }

    public static List<Url> getEntities() throws SQLException {
        var sql = "SELECT * FROM urls";
        try (
                var conn = dataSource.getConnection();
                var stmt = conn.prepareStatement(sql)
        ) {
            var resultSet = stmt.executeQuery();
            var result = new ArrayList<Url>();
            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var urlTitle = resultSet.getString("name");
                var createdAt = resultSet.getTimestamp("created_at");
                var url = new Url(urlTitle, createdAt);
                url.setId(id);
                result.add(url);
            }
            return result;
        }
    }
}
