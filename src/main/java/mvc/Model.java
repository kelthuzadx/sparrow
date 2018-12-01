package mvc;

import java.util.HashMap;
import java.util.Map;

public class Model {
    private HashMap<String, Object> m;

    Model(Map<String, String[]> m) {
        this.m = new HashMap<>();
        m.forEach((a, b) -> {
            if (b != null && b.length == 1) {
                this.m.put(a, b[0]);
            } else {
                this.m.put(a, b);
            }
        });
    }

    public Object get(String key) {
        return m.get(key);
    }

    public void set(String key, Object value) {
        m.put(key, value);
    }

    Map<String, Object> getWholeModel() {
        return m;
    }
}
