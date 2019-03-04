package org.orangeplayer.playerdesktop.sys;

import java.util.HashMap;

public class Session {
    private HashMap<SessionKey, Object> mapSession;

    private static Session session;

    public synchronized static Session getInstance() {
        if (session == null)
            session = new Session();
        return session;
    }

    private Session() {
        mapSession = new HashMap<>();
    }

    public void add(SessionKey key, Object value) {
        mapSession.put(key, value);
    }

    public HashMap<SessionKey, Object> getMapSession() {
        return mapSession;
    }

    public Object get(SessionKey key) {
        return mapSession.get(key);
    }
    
    public <T> T get(SessionKey key, Class<? extends T> clazz) {
        return (T) mapSession.get(key);
    }

    public void set(SessionKey key, Object value) {
        add(key, value);
    }

    public void remove(SessionKey key) {
        mapSession.remove(key);
    }

}
