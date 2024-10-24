package dev.folomkin.sandbox;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.tree.RowMapper;
import javax.swing.tree.TreePath;
import java.security.spec.NamedParameterSpec;

@RestController
@RequestMapping("/api/users")
public class UsersRestController implements RowMapper<UsersRestController.User> {


    private final NamedParameterJdbcOperations jdbcOperations;

    public UsersRestController(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public User map getRowsForPaths(TreePath[] treePaths) {
        return new int[0];
    }


}

