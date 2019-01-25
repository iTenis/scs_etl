package com.huawei.main;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//双缓冲队列，线程安全
public class DoubleBufferedQueue<E> extends AbstractQueue<E> implements BlockingQueue<E>, java.io.Serializable {
    private static final long serialVersionUID = 1011398447523020L;
    public static final int DEFAULT_QUEUE_CAPACITY = 5000000;
    public static final long DEFAULT_MAX_TIMEOUT = 0;
    public static final long DEFAULT_MAX_COUNT = 10;
    private Logger logger =    LoggerFactory.getLogger(DoubleBufferedQueue.class.getName());
    /** The queued items */
    private ReentrantLock readLock;
    // 写锁
    private ReentrantLock writeLock;
    // 是否满
    private Condition notFull;
    private Condition awake;
    // 读写数组
    private transient E[] writeArray;
    private transient E[] readArray;
    // 读写计数
    private volatile int writeCount;
    private volatile int readCount;
    // 写数组下标指针
    private int writeArrayTP;
    private int writeArrayHP;
    // 读数组下标指针
    private int readArrayTP;
    private int readArrayHP;
    private int capacity;

    public DoubleBufferedQueue(int capacity) {
        // 默认
        this.capacity = DEFAULT_QUEUE_CAPACITY;

        if (capacity > 0) {
            this.capacity = capacity;
        }

        readArray = (E[]) new Object[capacity];
        writeArray = (E[]) new Object[capacity];

        readLock = new ReentrantLock();
        writeLock = new ReentrantLock();
        notFull = writeLock.newCondition();
        awake = writeLock.newCondition();
    }

    private void insert(E e) {
        writeArray[writeArrayTP] = e;
        ++writeArrayTP;
        ++writeCount;
    }

    private E extract() {
        E e = readArray[readArrayHP];
        readArray[readArrayHP] = null;
        ++readArrayHP;
        --readCount;
        return e;
    }

    /**
     * switch condition: read queue is empty && write queue is not empty
     * 
     * Notice:This function can only be invoked after readLock is grabbed,or may
     * cause dead lock
     * 
     * @param timeout
     * @param isInfinite
     *            : whether need to wait forever until some other thread awake
     *            it
     * @return
     * @throws InterruptedException
     */
    private long queueSwap(long timeout, boolean isInfinite) throws InterruptedException {
        writeLock.lock();
        try {
            if (writeCount <= 0) {
                // logger.debug("Write Count:" + writeCount
                // + ", Write Queue is empty, do not switch!");
                try {
                    // logger.debug("Queue is empty, need wait....");
                    if (isInfinite && timeout <= 0) {
                        awake.await();
                        return -1;
                    } else if (timeout > 0) {
                        return awake.awaitNanos(timeout);
                    } else {
                        return 0;
                    }
                } catch (InterruptedException ie) {
                    awake.signal();
                    throw ie;
                }
            } else {
                E[] tmpArray = readArray;
                readArray = writeArray;
                writeArray = tmpArray;

                readCount = writeCount;
                readArrayHP = 0;
                readArrayTP = writeArrayTP;

                writeCount = 0;
                writeArrayHP = readArrayHP;
                writeArrayTP = 0;

                notFull.signal();
                // logger.debug("Queue switch successfully!");
                return 0;
            }
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        if (e == null) {
            throw new NullPointerException();
        }

        long nanoTime = 0;
        
        if (timeout > 0) {
            nanoTime = unit.toNanos(timeout);
        }
        
        writeLock.lockInterruptibly();
        
        try {
            for (int i = 0; i < DEFAULT_MAX_COUNT; i++) {
                if (writeCount < writeArray.length) {
                    insert(e);
                    if (writeCount == 1) {
                        awake.signal();
                    }
                    return true;
                }

                // Time out
                if (nanoTime <= 0) {
                    // logger.debug("offer wait time out!");
                    return false;
                }
                // keep waiting
                try {
                    // logger.debug("Queue is full, need wait....");
                    nanoTime = notFull.awaitNanos(nanoTime);
                } catch (InterruptedException ie) {
                    notFull.signal();
                    throw ie;
                }
            }
        } finally {
            writeLock.unlock();
        }

        return false;
    }

    // 取
    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        long nanoTime = 0;
        
        if (timeout > 0) {
            nanoTime = unit.toNanos(timeout);
        }
        
        readLock.lockInterruptibly();

        try {
            if (nanoTime > 0) {
                for (int i = 0; i < DEFAULT_MAX_COUNT; i++) {
                    if (readCount > 0) {
                        return extract();
                    }

                    if (nanoTime <= 0) {
                        // logger.debug("poll time out!");
                        return null;
                    }
                    nanoTime = queueSwap(nanoTime, false);
                }
            } else {
                if (readCount > 0) {
                    return extract();
                }

                queueSwap(nanoTime, false);

                if (readCount > 0) {
                    return extract();
                } 
            }
        } finally {
            readLock.unlock();
        }

        return null;
    }

    // 等待500毫秒
    @Override
    public E poll() {
        E ret = null;
        try {
            ret = poll(DEFAULT_MAX_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            ret = null;
        }
        return ret;
    }

    // 查看
    @Override
    public E peek() {
        E e = null;
        readLock.lock();

        try {
            if (readCount > 0) {
                e = readArray[readArrayHP];
            }
        } finally {
            readLock.unlock();
        }

        return e;
    }

    // 默认500毫秒
    @Override
    public boolean offer(E e) {
        boolean ret = false;
        try {
            ret = offer(e, DEFAULT_MAX_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (Exception e2) {
            ret = false;
        }
        return ret;
    }

    @Override
    public void put(E e) throws InterruptedException {
        // never need to // block
        offer(e, DEFAULT_MAX_TIMEOUT, TimeUnit.MILLISECONDS); 
                                                                
    }

    @Override
    public E take() throws InterruptedException {
        return poll(DEFAULT_MAX_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    @Override
    public int remainingCapacity() {
        return this.capacity;
    }

    @Override
    public int drainTo(Collection<? super E> c) {
        return 0;
    }

    @Override
    public int drainTo(Collection<? super E> c, int maxElements) {
        return 0;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    // 当前读队列中还有多少个
    @Override
    public int size() {
        int size = 0;
        readLock.lock();

        try {
            size = readCount;
        } finally {
            readLock.unlock();
        }

        return size;
    }
    
    /**
     * 当前已写入的队列大小
     * */
    public int WriteSize() {
        int size = 0;
        writeLock.lock();

        try {
            size = writeCount;
        } finally {
            writeLock.unlock();
        }

        return size;
    }

    public int unsafeReadSize() {
        return readCount;
    }

    public int unsafeWriteSize() {
        return writeCount;
    }
    
    public int capacity() {
        return capacity;
    }
    
    public String toMemString() {
        return "--read: " + readCount + "/" + capacity + "--write: " + writeCount + "/" + capacity;
    }
    // 清理
    /*
     * public void clear() { readLock.lock(); writeLock.lock(); try { readCount
     * = 0; readArrayHP = 0; writeCount = 0; writeArrayTP = 0;
     * //logger.debug("Queue clear successfully!"); } finally {
     * writeLock.unlock(); readLock.unlock(); } }
     */
}
