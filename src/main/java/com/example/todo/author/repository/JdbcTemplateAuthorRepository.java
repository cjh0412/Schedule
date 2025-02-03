package com.example.todo.author.repository;

import com.example.todo.author.dto.AuthorResponseDto;
import com.example.todo.author.entity.Author;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcTemplateAuthorRepository implements AuthorRepository{
    private final JdbcTemplate jdbc;

    public JdbcTemplateAuthorRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Optional<Author> findByAuthorId(String name, String email) {
        String sql = "select * from author where name = ? and email = ?";
        List<Author> author = jdbc.query(sql, authorRowMapper(), name, email);
        return author.stream().findFirst();
    }

    @Override
    public AuthorResponseDto createAuthor(String name, String email) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc);
        insert.withTableName("author").usingGeneratedKeyColumns("id");

        Map<String, Object> param = new HashMap<>();
        param.put("name", name);
        param.put("email", email);
        Number key = insert.executeAndReturnKey(new MapSqlParameterSource(param));

        return new AuthorResponseDto(key.longValue(), name, email);

    }

    @Override
    public int updateAuthor(Long id , String name) {
        String sql = "update author set name = ?  where id = (select author_id from schedule where id = ?) ";
        return jdbc.update(sql , name, id);
    }


    private RowMapper<Author> authorRowMapper(){
        return new RowMapper<Author>() {
            public Author mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                return new Author(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email")
                );
            }

        };
    }
}
