package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {
    private HashMap<Long,TimeEntry> repo;
    private long idCounter;

    public InMemoryTimeEntryRepository(){
        this.repo = new HashMap<>();
        this.idCounter = 1L;
    }
    @Override
    public TimeEntry find(long timeEntryId) {
        for (Map.Entry<Long, TimeEntry> entry : repo.entrySet()){
            if (timeEntryId == entry.getKey()) {
                return entry.getValue();
            }
        }
        return null;
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
