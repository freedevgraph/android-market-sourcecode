package com.google.android.finsky.config;

import android.content.ContentResolver;
import android.content.Context;
import com.google.android.gsf.GoogleSettingsContract;
import com.google.android.gsf.Gservices;

/* JADX INFO: loaded from: classes.dex */
public abstract class GservicesValue<T> {
    private static GservicesReader sGservicesReader = null;
    protected final T mDefaultValue;
    protected final String mKey;
    private T mOverride = null;

    private interface GservicesReader {
        Boolean getBoolean(String str, Boolean bool);

        Integer getInt(String str, Integer num);

        Long getLong(String str, Long l);

        String getPartnerString(String str, String str2);

        String getString(String str, String str2);
    }

    protected abstract T retrieve(String str);

    public static void init(Context context) {
        sGservicesReader = new GservicesReaderImpl(context.getContentResolver());
    }

    protected GservicesValue(String key, T defaultValue) {
        this.mKey = key;
        this.mDefaultValue = defaultValue;
    }

    public final T get() {
        return this.mOverride != null ? this.mOverride : retrieve(this.mKey);
    }

    public static GservicesValue<Boolean> value(String key, boolean defaultValue) {
        return new GservicesValue<Boolean>(key, Boolean.valueOf(defaultValue)) { // from class: com.google.android.finsky.config.GservicesValue.1
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.android.finsky.config.GservicesValue
            public Boolean retrieve(String str) {
                return GservicesValue.sGservicesReader.getBoolean(this.mKey, (Boolean) this.mDefaultValue);
            }
        };
    }

    public static GservicesValue<Long> value(String key, Long defaultValue) {
        return new GservicesValue<Long>(key, defaultValue) { // from class: com.google.android.finsky.config.GservicesValue.2
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.android.finsky.config.GservicesValue
            public Long retrieve(String str) {
                return GservicesValue.sGservicesReader.getLong(this.mKey, (Long) this.mDefaultValue);
            }
        };
    }

    public static GservicesValue<Integer> value(String key, Integer defaultValue) {
        return new GservicesValue<Integer>(key, defaultValue) { // from class: com.google.android.finsky.config.GservicesValue.3
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.android.finsky.config.GservicesValue
            public Integer retrieve(String str) {
                return GservicesValue.sGservicesReader.getInt(this.mKey, (Integer) this.mDefaultValue);
            }
        };
    }

    public static GservicesValue<String> value(String key, String defaultValue) {
        return new GservicesValue<String>(key, defaultValue) { // from class: com.google.android.finsky.config.GservicesValue.4
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.google.android.finsky.config.GservicesValue
            public String retrieve(String str) {
                return GservicesValue.sGservicesReader.getString(this.mKey, (String) this.mDefaultValue);
            }
        };
    }

    public static GservicesValue<String> partnerSetting(String key, String defaultValue) {
        return new GservicesValue<String>(key, defaultValue) { // from class: com.google.android.finsky.config.GservicesValue.5
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.google.android.finsky.config.GservicesValue
            public String retrieve(String str) {
                return GservicesValue.sGservicesReader.getPartnerString(this.mKey, (String) this.mDefaultValue);
            }
        };
    }

    private static class GservicesReaderImpl implements GservicesReader {
        private final ContentResolver mContentResolver;

        public GservicesReaderImpl(ContentResolver contentResolver) {
            this.mContentResolver = contentResolver;
        }

        @Override // com.google.android.finsky.config.GservicesValue.GservicesReader
        public Boolean getBoolean(String key, Boolean defaultValue) {
            return Boolean.valueOf(Gservices.getBoolean(this.mContentResolver, key, defaultValue.booleanValue()));
        }

        @Override // com.google.android.finsky.config.GservicesValue.GservicesReader
        public Integer getInt(String key, Integer defaultValue) {
            return Integer.valueOf(Gservices.getInt(this.mContentResolver, key, defaultValue.intValue()));
        }

        @Override // com.google.android.finsky.config.GservicesValue.GservicesReader
        public Long getLong(String key, Long defaultValue) {
            return Long.valueOf(Gservices.getLong(this.mContentResolver, key, defaultValue.longValue()));
        }

        @Override // com.google.android.finsky.config.GservicesValue.GservicesReader
        public String getString(String key, String defaultValue) {
            return Gservices.getString(this.mContentResolver, key, defaultValue);
        }

        @Override // com.google.android.finsky.config.GservicesValue.GservicesReader
        public String getPartnerString(String key, String defaultValue) {
            return GoogleSettingsContract.Partner.getString(this.mContentResolver, key, defaultValue);
        }
    }
}
