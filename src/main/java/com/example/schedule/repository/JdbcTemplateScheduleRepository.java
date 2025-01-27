package com.example.schedule.repository;

import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class JdbcTemplateScheduleRepository implements ScheduleRepository{

    private final JdbcTemplate jdbc;

    public JdbcTemplateScheduleRepository(DataSource dataSource) {
        this.jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public ScheduleResponseDto saveSchedule(Schedule schedule) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc);
        insert.withTableName("schedule").usingGeneratedKeyColumns("id");

        Map<String, Object> param = new HashMap<>();
        param.put("author", schedule.getAuthor());
        param.put("title", schedule.getTitle());
        param.put("contents", schedule.getContents());
        param.put("password", schedule.getPassword());

        Number key = insert.executeAndReturnKey(new MapSqlParameterSource(param));
        return new ScheduleResponseDto(key.longValue()
                , schedule.getAuthor()
                , schedule.getTitle()
                , schedule.getContents()
                , schedule.getPassword()
        );
    }

    @Override
    public List<ScheduleResponseDto> findAllSchedules(String updateAt, String author) {
        StringBuilder sql = new StringBuilder("select * from schedule where 1=1 ");
        List<Object> params = new ArrayList<>();

        if(updateAt != null){
            sql.append("and date_format(update_at, '%Y-%m-%d') = ?");
            params.add(updateAt);
        }

        if (author != null) {
            sql.append("and author = ? ");
            params.add(author);
        }

        sql.append("order by update_at desc");

        return jdbc.query(sql.toString(), params.toArray(), scheduleRowMapper());
    }

    @Override
    public Optional<Schedule> findScheduleById(Long id) {
         List<Schedule> result = jdbc.query("select * from schedule where id = ? ", scheduleRowMapperV2(), id);
        return result.stream().findAny();
    }

    @Override
    public int updateSchedule(Long id, String title, String contents, String author, String password) {
        int result = jdbc.update("update schedule set title = ? , contents = ? , author = ? where id = ? and password = ? " ,
                title, contents, author, id, password);
        return result;
    }

    @Override
    public int deleteSchedule(Long id, String password) {
        int result = jdbc.update("delete from schedule  where id = ? and password = ? " , id, password);
        return result;
    }

    private RowMapper<ScheduleResponseDto> scheduleRowMapper(){
        return new RowMapper<ScheduleResponseDto>() {
            public ScheduleResponseDto mapRow(ResultSet resultSet,  int rowNum) throws SQLException{
                return new ScheduleResponseDto(
                        resultSet.getLong("id"),
                        resultSet.getString("title"),
                        resultSet.getString("contents"),
                        resultSet.getString("author"),
                        resultSet.getString("password")
                );
            }

        };
    }

    private RowMapper<Schedule> scheduleRowMapperV2(){
        return new RowMapper<Schedule>() {
            public Schedule mapRow(ResultSet resultSet,  int rowNum) throws SQLException{
                return new Schedule(
                        resultSet.getLong("id"),
                        resultSet.getString("title"),
                        resultSet.getString("contents"),
                        resultSet.getString("author"),
                        resultSet.getString("password")
                );
            }

        };
    }


}
