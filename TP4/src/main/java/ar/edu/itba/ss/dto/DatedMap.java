package ar.edu.itba.ss.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class DatedMap<K, V>{

    private LocalDateTime date;
    private Map<K, V> map;

    public String getDate() {
        return date.toString();
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public DatedMap<K, V> withDate(LocalDateTime date) {
        setDate(date);
        return this;
    }

    public Map<K, V> getMap() {
        return map;
    }
    public void setMap(Map<K, V> map) {
        this.map = map;
    }
    public DatedMap<K, V> withMap(Map<K, V> map) {
        setMap(map);
        return this;
    }
}
