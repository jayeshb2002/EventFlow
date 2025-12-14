package com.eventflow.eventManagement.auth.repository;

import com.eventflow.eventManagement.common.dto.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> userRowMapper = new RowMapper<>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(
                    rs.getLong("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password_hash"),
                    rs.getString("role"),
                    rs.getTimestamp("created_at").toLocalDateTime()
            );
        }
    };

    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM eventflowdb.users WHERE username = ?";
        return jdbcTemplate.query(sql, userRowMapper, username).stream().findFirst();
    }

    public int save(User user) {
        String sql = "INSERT INTO eventflowdb.users(username, email, password_hash, role, created_at) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, user.getUsername(), user.getEmail(), user.getPasswordHash(), user.getRole(), LocalDateTime.now());
    }
}