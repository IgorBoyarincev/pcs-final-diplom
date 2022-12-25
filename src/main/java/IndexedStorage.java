import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexedStorage {
    protected static IndexedStorage storage;
    protected Map<String, List<PageEntry>> wordIndexing = new HashMap<>();

    public static synchronized IndexedStorage getIndexedStorage() {
        if (storage == null) {
            storage = new IndexedStorage();
        }
        return storage;
    }

    public Map<String, List<PageEntry>> getStorage() {
        return wordIndexing;
    }
}
