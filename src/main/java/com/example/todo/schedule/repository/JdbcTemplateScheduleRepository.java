package com.example.todo.schedule.repository;

import com.example.todo.schedule.dto.ScheduleResponseDto;
import com.example.todo.schedule.entity.Schedule;
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
        param.put("authorId", schedule.getAuthorId());
        param.put("title", schedule.getTitle());
        param.put("contents", schedule.getContents());
        param.put("password", schedule.getPassword());
        param.put("name", schedule.getName());
        param.put("is_deleted", 0);

        Number key = insert.executeAndReturnKey(new MapSqlParameterSource(param));
        return new ScheduleResponseDto(key.longValue()
                , schedule.getAuthorId()
                , schedule.getTitle()
                , schedule.getContents()
                , schedule.getName()
        );
    }

    @Override
    public List<ScheduleResponseDto> findAllSchedules(String updatedAt, String author, int page, int size) {
        StringBuilder sql = new StringBuilder("select s.*, a.name from schedule s join author a on s.author_id = a.id where s.is_deleted = 0 ");
        List<Object> params = new ArrayList<>();

        if(updatedAt != null){
            sql.append("and date_format(s.updated_at, '%Y-%m-%d') = ?");
            params.add(updatedAt);
        }

        if (author != null) {
            sql.append("and a.name = ? ");
            params.add(author);
        }

        sql.append("order by s.updated_at desc ");

        // 페이징 처리
        sql.append("limit ? offset ?");
        params.add(size);
        params.add(size * (page-1));
        return jdbc.query(sql.toString(), params.toArray(), scheduleRowMapper());
    }

    @Override
    public Schedule findScheduleByIdOrElseThrow(Long id) {
        String sql = "select s.*, a.name from schedule s join author a on s.author_id = a.id where s.id = ? and s.is_deleted = 0";
         List<Schedule> result = jdbc.query(sql, scheduleRowMapperV2(), id);
        return result.stream().findAny().orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "조회된 일정이 존재하지 않습니다."));
    }

    @Override
    public int updateSchedule(Long id, String title, String contents, String password) {
        String sql = "update schedule set title = ? , contents = ?  where id = ? and password = ? and is_deleted = 0";
        int result = jdbc.update(sql ,
                title, contents,  id, password);
        return result;
    }


    @Override
    public void deleteSchedule(Long id, String password) {
        String sql  = "update schedule set  is_Deleted = 1 where id = ? and password = ? and is_deleted = 0";
        jdbc.update(sql , id, password);
    }

    @Override
    public boolean validPassword(Long id, String password) {
        String sql = "select count(*) from schedule where id = ? and password = ? and is_deleted = 0";
        Integer count = jdbc.queryForObject(sql, Integer.class, id, password);
        return count > 0;
    }

    @Override
    public int isDeleted(Long id, String password) {
        String sql = "select is_deleted from schedule where id = ? and password = ? ";
        Integer queryResult = jdbc.queryForObject(sql, Integer.class, id, password);
        return queryResult;
    }

    private RowMapper<ScheduleResponseDto> scheduleRowMapper(){
        return new RowMapper<ScheduleResponseDto>() {
            public ScheduleResponseDto mapRow(ResultSet resultSet,  int rowNum) throws SQLException{
                return new ScheduleResponseDto(
                        resultSet.getLong("id"),
                        resultSet.getLong("author_Id"),
                        resultSet.getString("title"),
                        resultSet.getString("contents"),
                        resultSet.getString("name")
                );
            }

        };
    }

    private RowMapper<Schedule> scheduleRowMapperV2(){
        return new RowMapper<Schedule>() {
            public Schedule mapRow(ResultSet resultSet,  int rowNum) throws SQLException{
                return new Schedule(
                        resultSet.getLong("id"),
                        resultSet.getLong("author_Id"),
                        resultSet.getString("title"),
                        resultSet.getString("contents"),
                        resultSet.getString("password"),
                        resultSet.getString("name")
                );
            }

        };
    }
}
