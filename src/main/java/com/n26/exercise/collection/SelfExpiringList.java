package com.n26.exercise.collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SelfExpiringList<V> implements ExpiringList<V> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int DEFAULT_TIME_TO_LIVE = 60000; //milliseconds
    private static final int DEFAULT_EXPIRATION_INTERVAL = 1; //milliseconds
    private CopyOnWriteArrayList<ExpiringObject> container;
    private final CopyOnWriteArrayList<ExpirationListener> expirationListeners;
    private ExpirerJob expirerJob;

    public SelfExpiringList() {
        this(DEFAULT_TIME_TO_LIVE, DEFAULT_EXPIRATION_INTERVAL);
    }

    public SelfExpiringList(final int timeToLive, final int expirationInterval) {
        this.container = new CopyOnWriteArrayList<>();
        this.expirationListeners = new CopyOnWriteArrayList<>();
        this.expirerJob = new ExpirerJob();
        this.expirerJob.setTimeToLiveInMilli(timeToLive);
        this.expirerJob.setExpirationTimeInMilli(expirationInterval);
        expirerJob.start();
    }

    @Override
    public void add(V value, long atTime) {
        container.add(new ExpiringObject(value, atTime));
    }

    @Override
    public boolean remove(Object o) {
        return container.remove(o);
    }

    @Override
    public void clear() {
        container.clear();
    }

    @Override
    public int size() {
        return container.size();
    }

    @Override
    public Iterator<V> iterator() {
        return new ExpiringListIterator(container.iterator());
    }


    public void addExpirationListener(ExpirationListener<? extends V> listener) {
        expirationListeners.add(listener);
    }

    public void removeExpirationListener(
            ExpirationListener<? extends V> listener) {
        expirationListeners.remove(listener);
    }

    private class ExpiringObject {
        private V value;
        private volatile long lastAccessedAt;

        ExpiringObject(final V value, final long lastAccessedAt) {
            this.value = value;
            this.lastAccessedAt = lastAccessedAt;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public long getLastAccessedAt() {
            return lastAccessedAt;
        }

        public void setLastAccessedAt(long lastAccessedAt) {
            this.lastAccessedAt = lastAccessedAt;
        }
    }

    private class ExpirerJob implements Runnable {

        private final ReadWriteLock lock = new ReentrantReadWriteLock();
        private long timeToLiveInMilli;
        private long expirationTimeInMilli;

        private final Thread expirerThread;
        private volatile boolean stop = true;

        public ExpirerJob() {
            expirerThread = new Thread(this);
            expirerThread.setDaemon(true); // run in background
            expirerThread.start();
        }

        @Override
        public void run() {
            while (!stop) {
                expireObjects();
                try {
                    Thread.sleep(expirationTimeInMilli);
                } catch (final InterruptedException e) {
                    logger.error("Error occurred while expiring objects: ", e.getMessage());
                }
            }
        }

        private void expireObjects() {
            final long now = System.currentTimeMillis();
            for (ExpirationListener<V> listener : expirationListeners) {
                listener.expired();
            }
            container.forEach(element -> {
                if (timeToLiveInMilli > 0 && (now - element.lastAccessedAt) >= timeToLiveInMilli) {
                    container.remove(element);

                }
            });
        }

        public void start() {
            lock.writeLock().lock();
            try {
                if (stop) {
                    stop = false;
                }

            } finally {
                lock.writeLock().unlock();
            }
        }

        public boolean isRunning() {
            return !stop;
        }

        public void stop() {
            lock.writeLock().lock();
            try {
                if (!stop) {
                    stop = true;
                }
            } finally {
                lock.writeLock().unlock();
            }
        }

        public void setTimeToLiveInMilli(long timeToLiveInMilli) {
            this.timeToLiveInMilli = timeToLiveInMilli;
        }

        public void setExpirationTimeInMilli(long expirationTimeInMilli) {
            this.expirationTimeInMilli = expirationTimeInMilli;
        }
    }

    private class ExpiringListIterator implements Iterator<V> {

        private final Iterator<ExpiringObject> iterator;

        public ExpiringListIterator(final Iterator<ExpiringObject> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public V next() {
            return iterator.next().getValue();
        }

        @Override
        public void remove() {
            iterator.remove();
        }
    }

}
