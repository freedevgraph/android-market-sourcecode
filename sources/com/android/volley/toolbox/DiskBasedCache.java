package com.android.volley.toolbox;

import com.android.volley.Cache;
import com.android.volley.VolleyLog;
import com.google.android.finsky.utils.Maps;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class DiskBasedCache implements Cache {
    private final Map<String, CacheHeader> mEntries;
    private final int mMaxCacheSizeInBytes;
    private final File mRootDirectory;
    private long mTotalSize;

    public DiskBasedCache(File rootDirectory, int maxCacheSizeInBytes) {
        this.mEntries = Maps.newLinkedHashMap(true);
        this.mTotalSize = 0L;
        this.mRootDirectory = rootDirectory;
        this.mMaxCacheSizeInBytes = maxCacheSizeInBytes;
    }

    public DiskBasedCache(File rootDirectory) {
        this(rootDirectory, 5242880);
    }

    @Override // com.android.volley.Cache
    public synchronized void clear() {
        File[] files = this.mRootDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
        this.mEntries.clear();
        this.mTotalSize = 0L;
    }

    @Override // com.android.volley.Cache
    public synchronized Cache.Entry get(String key) {
        Cache.Entry cacheEntry;
        CacheHeader entry = this.mEntries.get(key);
        if (entry == null) {
            cacheEntry = null;
        } else {
            String filename = getFilenameForKey(key);
            try {
                FileInputStream fis = new FileInputStream(new File(this.mRootDirectory, filename));
                CacheHeader.readHeader(fis);
                byte[] data = streamToBytes(fis);
                fis.close();
                cacheEntry = entry.toCacheEntry(data);
            } catch (IOException e) {
                VolleyLog.d("%s: %s", filename, e.toString());
                remove(key);
                cacheEntry = null;
            }
        }
        return cacheEntry;
    }

    @Override // com.android.volley.Cache
    public synchronized void initialize() {
        File[] files = this.mRootDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                try {
                    FileInputStream fis = new FileInputStream(file);
                    CacheHeader entry = CacheHeader.readHeader(fis);
                    entry.size = file.length();
                    fis.close();
                    putEntry(entry.key, entry);
                } catch (IOException e) {
                    if (file != null) {
                        file.delete();
                    }
                }
            }
        }
    }

    @Override // com.android.volley.Cache
    public synchronized void invalidate(String key, boolean fullExpire) {
        Cache.Entry entry = get(key);
        if (entry != null) {
            entry.softTtl = 0L;
            if (fullExpire) {
                entry.ttl = 0L;
            }
            put(key, entry);
        }
    }

    @Override // com.android.volley.Cache
    public synchronized void put(String key, Cache.Entry entry) {
        pruneIfNeeded(entry.data.length);
        String filename = getFilenameForKey(key);
        try {
            FileOutputStream fos = new FileOutputStream(new File(this.mRootDirectory, filename));
            CacheHeader e = new CacheHeader(key, entry);
            e.writeHeader(fos);
            fos.write(entry.data);
            fos.close();
            putEntry(key, e);
        } catch (IOException e2) {
            new File(this.mRootDirectory, filename).delete();
        }
    }

    public synchronized void remove(String key) {
        new File(this.mRootDirectory, getFilenameForKey(key)).delete();
        removeEntry(key);
    }

    private String getFilenameForKey(String key) {
        int firstHalfLength = key.length() / 2;
        String localFilename = String.valueOf(key.substring(0, firstHalfLength).hashCode());
        return localFilename + String.valueOf(key.substring(firstHalfLength).hashCode());
    }

    private void pruneIfNeeded(int neededSpace) {
        if (this.mTotalSize + ((long) neededSpace) >= this.mMaxCacheSizeInBytes) {
            long before = this.mTotalSize;
            int prunedFiles = 0;
            long startTime = System.currentTimeMillis();
            Iterator<Map.Entry<String, CacheHeader>> iterator = this.mEntries.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, CacheHeader> entry = iterator.next();
                CacheHeader e = entry.getValue();
                new File(getFilenameForKey(e.key)).delete();
                this.mTotalSize -= e.size;
                iterator.remove();
                prunedFiles++;
                if (this.mTotalSize + ((long) neededSpace) < this.mMaxCacheSizeInBytes * 0.9f) {
                    break;
                }
            }
            if (VolleyLog.DEBUG) {
                VolleyLog.v("pruned %d files, %d bytes, %d ms", Integer.valueOf(prunedFiles), Long.valueOf(this.mTotalSize - before), Long.valueOf(System.currentTimeMillis() - startTime));
            }
        }
    }

    private void putEntry(String key, CacheHeader entry) {
        if (!this.mEntries.containsKey(key)) {
            this.mTotalSize += entry.size;
        }
        this.mEntries.put(key, entry);
    }

    private void removeEntry(String key) {
        CacheHeader entry = this.mEntries.get(key);
        if (entry != null) {
            this.mTotalSize -= entry.size;
            this.mEntries.remove(key);
        }
    }

    private static byte[] streamToBytes(InputStream in) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (true) {
            int count = in.read(buffer);
            if (count != -1) {
                bytes.write(buffer, 0, count);
            } else {
                return bytes.toByteArray();
            }
        }
    }

    private static class CacheHeader {
        public String etag;
        public String key;
        public long serverDate;
        public long size;
        public long softTtl;
        public long ttl;

        private CacheHeader() {
        }

        public CacheHeader(String key, Cache.Entry entry) {
            this.key = key;
            this.size = entry.data.length;
            this.etag = entry.etag;
            this.serverDate = entry.serverDate;
            this.ttl = entry.ttl;
            this.softTtl = entry.softTtl;
        }

        public static CacheHeader readHeader(InputStream is) throws IOException {
            CacheHeader entry = new CacheHeader();
            ObjectInputStream ois = new ObjectInputStream(is);
            int version = ois.readByte();
            if (version != 1) {
                throw new IOException();
            }
            entry.key = ois.readUTF();
            entry.etag = ois.readUTF();
            if (entry.etag.equals("")) {
                entry.etag = null;
            }
            entry.serverDate = ois.readLong();
            entry.ttl = ois.readLong();
            entry.softTtl = ois.readLong();
            return entry;
        }

        public Cache.Entry toCacheEntry(byte[] data) {
            Cache.Entry e = new Cache.Entry();
            e.data = data;
            e.etag = this.etag;
            e.serverDate = this.serverDate;
            e.ttl = this.ttl;
            e.softTtl = this.softTtl;
            return e;
        }

        public boolean writeHeader(OutputStream os) {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(os);
                oos.writeByte(1);
                oos.writeUTF(this.key);
                oos.writeUTF(this.etag == null ? "" : this.etag);
                oos.writeLong(this.serverDate);
                oos.writeLong(this.ttl);
                oos.writeLong(this.softTtl);
                oos.flush();
                return true;
            } catch (IOException e) {
                VolleyLog.d("%s", e.toString());
                return false;
            }
        }
    }
}
