package dev.folomkin.sandbox;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersRestController implements RowMapper<UsersRestController.User> {

    private final NamedParameterJdbcOperations jdbcOperations;

    public UsersRestController(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getInt("id"), rs.getString("c_username"));
    }

    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        return users;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    class User {
        private int id;
        private String name;
    }
}

