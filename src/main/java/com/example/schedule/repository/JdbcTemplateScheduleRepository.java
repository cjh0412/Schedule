package com.example.schedule.repository;

import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class JdbcTemplateScheduleRepository implements ScheduleRepository{

    private static final Logger log = LoggerFactory.getLogger(JdbcTemplateScheduleRepository.class);
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
        param.put("is_deleted", 0);

        Number key = insert.executeAndReturnKey(new MapSqlParameterSource(param));
        return new ScheduleResponseDto(key.longValue()
                , schedule.getAuthor()
                , schedule.getTitle()
                , schedule.getContents()
        );
    }

    @Override
    public List<ScheduleResponseDto> findAllSchedules(String updateAt, String author) {
        StringBuilder sql = new StringBuilder("select * from schedule where is_deleted = 0 ");
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
    public Schedule findScheduleByIdOrElseThrow(Long id) {
         List<Schedule> result = jdbc.query("select * from schedule where id = ? and is_deleted = 0", scheduleRowMapperV2(), id);
        return result.stream().findAny().orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "조회된 일정이 존재하지 않습니다."));
    }

    @Override
    public int updateSchedule(Long id, String title, String contents, String author, String password) {
        int result = jdbc.update("update schedule set title = ? , contents = ? , author = ? where id = ? and password = ? and is_deleted = 0" ,
                title, contents, author, id, password);
        return result;
    }

    @Override
    public void deleteSchedule(Long id, String password) {
        jdbc.update("update schedule set  is_Deleted = 1 where id = ? and password = ? and is_deleted = 0" , id, password);
    }

    @Override
    public boolean validPassword(Long id, String password) {
        String sql = "select count(*) from schedule where id = ? and password = ? and is_deleted = 0";
        Integer count = jdbc.queryForObject(sql, Integer.class, id, password);
        return (count > 0 ) ? true : false;
    }

    private RowMapper<ScheduleResponseDto> scheduleRowMapper(){
        return new RowMapper<ScheduleResponseDto>() {
            public ScheduleResponseDto mapRow(ResultSet resultSet,  int rowNum) throws SQLException{
                return new ScheduleResponseDto(
                        resultSet.getLong("id"),
                        resultSet.getString("author"),
                        resultSet.getString("title"),
                        resultSet.getString("contents")
                );
            }

        };
    }

    private RowMapper<Schedule> scheduleRowMapperV2(){
        return new RowMapper<Schedule>() {
            public Schedule mapRow(ResultSet resultSet,  int rowNum) throws SQLException{
                return new Schedule(
                        resultSet.getLong("id"),
                        resultSet.getString("author"),
                        resultSet.getString("title"),
                        resultSet.getString("contents"),
                        resultSet.getString("password")
                );
            }

        };
    }


}
