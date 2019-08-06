package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {
    private HashMap<Long,TimeEntry> repo = new HashMap<>();
    private long idCounter = 1L;

    @Override
    public TimeEntry find(long timeEntryId) {
        return repo.get(timeEntryId);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        TimeEntry entry = timeEntry.getId() != -1L ? new TimeEntry(timeEntry.getId(),timeEntry) : new TimeEntry(idCounter++, timeEntry);
        repo.put(entry.getId(),entry);
        return entry;
    }

    @Override
    public List<TimeEntry> list() {
        return new ArrayList<>(repo.values());
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        if(repo.containsKey(id)) {
            return create(new TimeEntry(id, timeEntry));
        }
        return null;
    }                                                                     

    @Override
    public void delete(long id) {
        repo.remove(id);
    }
}
