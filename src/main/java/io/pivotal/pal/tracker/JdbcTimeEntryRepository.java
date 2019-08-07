package io.pivotal.pal.tracker;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JdbcTimeEntryRepository implements TimeEntryRepository {
    private static final String DELETE_STATEMENT = "DELETE FROM time_entries WHERE id = ?";
    private static final String UPDATE_STATEMENT = "UPDATE time_entries SET project_id=?,user_id=?,date=?,hours=? WHERE id = ?";
    private static final String CREATE_STATEMENT = "INSERT INTO time_entries (project_id, user_id, date, hours) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_STATEMENT = "SELECT * FROM time_entries";
    private final JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(DataSource source) {
        this.jdbcTemplate = new JdbcTemplate(source);
    }
    @Override
    public TimeEntry find(long timeEntryId) {
        try {
            Map<String, Object> entryMap = jdbcTemplate.queryForMap("SELECT * FROM time_entries WHERE id = " + timeEntryId);
            return new TimeEntry((long)entryMap.get("id"),(long)entryMap.get("project_id"),(long)entryMap.get("user_id"),((Date)entryMap.get("date")).toLocalDate(),(int)entryMap.get("hours"));
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(CREATE_STATEMENT, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1,timeEntry.getProjectId());
            ps.setLong(2,timeEntry.getUserId());
            ps.setDate(3,Date.valueOf(timeEntry.getDate()));
            ps.setInt(4,timeEntry.getHours());

            return ps;
        }, holder);
        return find(holder.getKey().longValue());
    }

    @Override
    public List<TimeEntry> list() {
        List<TimeEntry> timeEntries = new ArrayList<>();
        for (Map<String,Object> entryMap : jdbcTemplate.queryForList(SELECT_ALL_STATEMENT)) {
            timeEntries.add(new TimeEntry((long)entryMap.get("id"),(long)entryMap.get("project_id"),(long)entryMap.get("user_id"),((Date)entryMap.get("date")).toLocalDate(),(int)entryMap.get("hours")));
        }
        return timeEntries;
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(UPDATE_STATEMENT, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1,timeEntry.getProjectId());
            ps.setLong(2,timeEntry.getUserId());
            ps.setDate(3,Date.valueOf(timeEntry.getDate()));
            ps.setInt(4,timeEntry.getHours());
            ps.setLong(5,id);

            return ps;
        });
        return find(id);
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update(DELETE_STATEMENT,id);
    }
}
