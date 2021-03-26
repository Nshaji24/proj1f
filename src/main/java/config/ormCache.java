package config;


import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.map.LRUMap;

import java.util.ArrayList;
//cache tutorial at https://crunchify.com/how-to-create-a-simple-in-memory-cache-in-java-lightweight-cache/

public class ormCache<K, T> {

        private long timeToLive;
        private LRUMap ormCacheMap;

        protected class cormCacheObject {
                public long lastAccessed = System.currentTimeMillis();
                public T value;

                protected cormCacheObject(T value) {
                        this.value = value;
                }
        }

        public ormCache(long timeToLive, final long timerInterval, int maxItems) {
                this.timeToLive = timeToLive * 1000;

                ormCacheMap = new LRUMap(maxItems);

                if (timeToLive > 0 && timerInterval > 0) {

                        Thread t = new Thread(new Runnable() {
                                public void run() {
                                        while (true) {
                                                try {
                                                        Thread.sleep(timerInterval * 1000);
                                                } catch (InterruptedException ex) {
                                                }
                                                cleanup();
                                        }
                                }
                        });
                        t.setDaemon(true);
                        t.start();
                }
        }

        public void put(K key, T value) {
                synchronized (ormCacheMap) {
                        ormCacheMap.put(key, new cormCacheObject(value));
                }
        }

        @SuppressWarnings("unchecked")
        public T get(K key) {
                synchronized (ormCacheMap) {
                        cormCacheObject c = (cormCacheObject) ormCacheMap.get(key);

                        if (c == null)
                                return null;
                        else {
                                c.lastAccessed = System.currentTimeMillis();
                                return c.value;
                        }
                }
        }

        public void remove(K key) {
                synchronized (ormCacheMap) {
                        ormCacheMap.remove(key);
                }
        }

        public int size() {
                synchronized (ormCacheMap) {
                        return ormCacheMap.size();
                }
        }

        @SuppressWarnings("unchecked")
        public void cleanup() {

                long now = System.currentTimeMillis();
                ArrayList<K> deleteKey = null;

                synchronized (ormCacheMap) {
                        MapIterator itr = ormCacheMap.mapIterator();

                        deleteKey = new ArrayList<K>((ormCacheMap.size() / 2) + 1);
                        K key = null;
                        cormCacheObject c = null;

                        while (itr.hasNext()) {
                                key = (K) itr.next();
                                c = (cormCacheObject) itr.getValue();

                                if (c != null && (now > (timeToLive + c.lastAccessed))) {
                                        deleteKey.add(key);
                                }
                        }
                }

                for (K key : deleteKey) {
                        synchronized (ormCacheMap) {
                                ormCacheMap.remove(key);
                        }

                        Thread.yield();
                }
        }
}