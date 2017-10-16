package app.common.cache;
/*
    created by xdCao on 2017/10/10
*/
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/*
自己的缓存实现
 */
public class MapCache {

    private static final int DEFAULT_CACHES=1024;

    private static final MapCache instance=new MapCache();

    private Map<String,CacheObject> cachePool;

    public static MapCache getInstance(){
        return instance;
    }

    public MapCache() {
        this(DEFAULT_CACHES);
    }

    public MapCache(int cacheCount) {
        cachePool = new ConcurrentHashMap<>(cacheCount);
    }


    public <T> T get(String key){
        CacheObject cacheObject=cachePool.get(key);
        if (cacheObject!=null){
            long cur=System.currentTimeMillis()/1000;
            if (cacheObject.getExpired()<=0||cacheObject.getExpired()>cur){//默认值是-1代表永久
                Object result=cacheObject.getValue();
                return (T)result;
            }
        }
        return null;
    }

    public <T> T hashGet(String key,String field){
        key=key+":"+field;
        return this.get(key);
    }

    public void set(String key,Object value){
        this.set(key,value,-1);
    }

    public void set(String key,Object value,long expired){
        expired=expired>0?System.currentTimeMillis()/1000+expired:expired;
        CacheObject cacheObject=new CacheObject(key,value,expired);
        cachePool.put(key,cacheObject);
    }

    public void hashSet(String key,String field,Object value){
        this.hashSet(key, field, value,-1);
    }

    public void hashSet(String key,String field,Object value,long expired){
        key=key+":"+field;
        this.set(key,value,expired);
    }

    public void delete(String key){
        cachePool.remove(key);
    }

    public void hashDelete(String key,String field){
        key=key+":"+field;
        this.delete(key);
    }

    public void clean(){
        this.cachePool.clear();
    }


    static class CacheObject{
        private String key;
        private Object value;
        private long expired;

        public CacheObject(String key, Object value, long expired) {
            this.key = key;
            this.value = value;
            this.expired = expired;
        }

        public String getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public long getExpired() {
            return expired;
        }
    }

}
