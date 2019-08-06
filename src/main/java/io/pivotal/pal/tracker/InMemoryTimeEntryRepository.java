package io.pivotal.pal.tracker;

import java.sql.Time;
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

        if (timeEntry.getId() != -1L) {
            repo.put(timeEntry.getId(), timeEntry);
            return timeEntry;
        }
        else {
            TimeEntry entry = new TimeEntry(idCounter, timeEntry);
            repo.put(idCounter,entry);
            idCounter++;
            return entry;
        }
    }

    @Override
    public List<TimeEntry> list() {
        List<TimeEntry> list = new ArrayList<>();
        for (Map.Entry<Long, TimeEntry> entry : repo.entrySet()){
            list.add(entry.getValue());
        }
        return list;
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        TimeEntry entry = null;
        if(find(id)!=null) {
            entry = new TimeEntry(id, timeEntry);
            return create(entry);
        }
        return null;
    }

    @Override
    public void delete(long id) {
        repo.remove(id);
    }
}
