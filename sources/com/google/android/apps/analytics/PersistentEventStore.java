package com.google.android.apps.analytics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
class PersistentEventStore implements EventStore {
    private SQLiteStatement compiledCountStatement = null;
    private DataBaseHelper databaseHelper;
    private int numStoredEvents;
    private boolean sessionUpdated;
    private int storeId;
    private long timestampCurrent;
    private long timestampFirst;
    private long timestampPrevious;
    private boolean useStoredVisitorVars;
    private int visits;

    static class DataBaseHelper extends SQLiteOpenHelper {
        public DataBaseHelper(Context context) {
            super(context, "google_analytics.db", (SQLiteDatabase.CursorFactory) null, 2);
        }

        private void createCustomVariableTables(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS custom_variables");
            sQLiteDatabase.execSQL("CREATE TABLE custom_variables (" + String.format(" '%s' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,", "cv_id") + String.format(" '%s' INTEGER NOT NULL,", "event_id") + String.format(" '%s' INTEGER NOT NULL,", "cv_index") + String.format(" '%s' CHAR(64) NOT NULL,", "cv_name") + String.format(" '%s' CHAR(64) NOT NULL,", "cv_value") + String.format(" '%s' INTEGER NOT NULL);", "cv_scope"));
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS custom_var_cache");
            sQLiteDatabase.execSQL("CREATE TABLE custom_var_cache (" + String.format(" '%s' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,", "cv_id") + String.format(" '%s' INTEGER NOT NULL,", "event_id") + String.format(" '%s' INTEGER NOT NULL,", "cv_index") + String.format(" '%s' CHAR(64) NOT NULL,", "cv_name") + String.format(" '%s' CHAR(64) NOT NULL,", "cv_value") + String.format(" '%s' INTEGER NOT NULL);", "cv_scope"));
            for (int i = 1; i <= 5; i++) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("event_id", (Integer) 0);
                contentValues.put("cv_index", Integer.valueOf(i));
                contentValues.put("cv_name", "");
                contentValues.put("cv_scope", (Integer) 3);
                contentValues.put("cv_value", "");
                sQLiteDatabase.insert("custom_var_cache", "event_id", contentValues);
            }
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL("CREATE TABLE events (" + String.format(" '%s' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,", "event_id") + String.format(" '%s' INTEGER NOT NULL,", "user_id") + String.format(" '%s' CHAR(256) NOT NULL,", "account_id") + String.format(" '%s' INTEGER NOT NULL,", "random_val") + String.format(" '%s' INTEGER NOT NULL,", "timestamp_first") + String.format(" '%s' INTEGER NOT NULL,", "timestamp_previous") + String.format(" '%s' INTEGER NOT NULL,", "timestamp_current") + String.format(" '%s' INTEGER NOT NULL,", "visits") + String.format(" '%s' CHAR(256) NOT NULL,", "category") + String.format(" '%s' CHAR(256) NOT NULL,", "action") + String.format(" '%s' CHAR(256), ", "label") + String.format(" '%s' INTEGER,", "value") + String.format(" '%s' INTEGER,", "screen_width") + String.format(" '%s' INTEGER);", "screen_height"));
            sQLiteDatabase.execSQL("CREATE TABLE session (" + String.format(" '%s' INTEGER PRIMARY KEY,", "timestamp_first") + String.format(" '%s' INTEGER NOT NULL,", "timestamp_previous") + String.format(" '%s' INTEGER NOT NULL,", "timestamp_current") + String.format(" '%s' INTEGER NOT NULL,", "visits") + String.format(" '%s' INTEGER NOT NULL);", "store_id"));
            sQLiteDatabase.execSQL("CREATE TABLE install_referrer (referrer TEXT PRIMARY KEY NOT NULL);");
            createCustomVariableTables(sQLiteDatabase);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            if (i2 == 2) {
                createCustomVariableTables(sQLiteDatabase);
            }
        }
    }

    PersistentEventStore(DataBaseHelper dataBaseHelper) {
        this.databaseHelper = dataBaseHelper;
        try {
            dataBaseHelper.getWritableDatabase().close();
        } catch (SQLiteException e) {
            Log.e("googleanalytics", e.toString());
        }
    }

    @Override // com.google.android.apps.analytics.EventStore
    public void deleteEvent(long j) {
        String str = "event_id=" + j;
        try {
            SQLiteDatabase writableDatabase = this.databaseHelper.getWritableDatabase();
            if (writableDatabase.delete("events", str, null) != 0) {
                this.numStoredEvents--;
                writableDatabase.delete("custom_variables", str, null);
            }
        } catch (SQLiteException e) {
            Log.e("googleanalytics", e.toString());
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x007e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    com.google.android.apps.analytics.CustomVariableBuffer getCustomVariables(long r12) throws java.lang.Throwable {
        /*
            r11 = this;
            r9 = 0
            com.google.android.apps.analytics.CustomVariableBuffer r8 = new com.google.android.apps.analytics.CustomVariableBuffer
            r8.<init>()
            com.google.android.apps.analytics.PersistentEventStore$DataBaseHelper r0 = r11.databaseHelper     // Catch: java.lang.Throwable -> L7a android.database.sqlite.SQLiteException -> L89
            android.database.sqlite.SQLiteDatabase r0 = r0.getReadableDatabase()     // Catch: java.lang.Throwable -> L7a android.database.sqlite.SQLiteException -> L89
            java.lang.String r1 = "custom_variables"
            r2 = 0
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L7a android.database.sqlite.SQLiteException -> L89
            r3.<init>()     // Catch: java.lang.Throwable -> L7a android.database.sqlite.SQLiteException -> L89
            java.lang.String r4 = "event_id="
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch: java.lang.Throwable -> L7a android.database.sqlite.SQLiteException -> L89
            java.lang.StringBuilder r3 = r3.append(r12)     // Catch: java.lang.Throwable -> L7a android.database.sqlite.SQLiteException -> L89
            java.lang.String r3 = r3.toString()     // Catch: java.lang.Throwable -> L7a android.database.sqlite.SQLiteException -> L89
            r4 = 0
            r5 = 0
            r6 = 0
            r7 = 0
            android.database.Cursor r0 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch: java.lang.Throwable -> L7a android.database.sqlite.SQLiteException -> L89
        L2a:
            boolean r1 = r0.moveToNext()     // Catch: android.database.sqlite.SQLiteException -> L61 java.lang.Throwable -> L82
            if (r1 == 0) goto L74
            com.google.android.apps.analytics.CustomVariable r1 = new com.google.android.apps.analytics.CustomVariable     // Catch: android.database.sqlite.SQLiteException -> L61 java.lang.Throwable -> L82
            java.lang.String r2 = "cv_index"
            int r2 = r0.getColumnIndex(r2)     // Catch: android.database.sqlite.SQLiteException -> L61 java.lang.Throwable -> L82
            int r2 = r0.getInt(r2)     // Catch: android.database.sqlite.SQLiteException -> L61 java.lang.Throwable -> L82
            java.lang.String r3 = "cv_name"
            int r3 = r0.getColumnIndex(r3)     // Catch: android.database.sqlite.SQLiteException -> L61 java.lang.Throwable -> L82
            java.lang.String r3 = r0.getString(r3)     // Catch: android.database.sqlite.SQLiteException -> L61 java.lang.Throwable -> L82
            java.lang.String r4 = "cv_value"
            int r4 = r0.getColumnIndex(r4)     // Catch: android.database.sqlite.SQLiteException -> L61 java.lang.Throwable -> L82
            java.lang.String r4 = r0.getString(r4)     // Catch: android.database.sqlite.SQLiteException -> L61 java.lang.Throwable -> L82
            java.lang.String r5 = "cv_scope"
            int r5 = r0.getColumnIndex(r5)     // Catch: android.database.sqlite.SQLiteException -> L61 java.lang.Throwable -> L82
            int r5 = r0.getInt(r5)     // Catch: android.database.sqlite.SQLiteException -> L61 java.lang.Throwable -> L82
            r1.<init>(r2, r3, r4, r5)     // Catch: android.database.sqlite.SQLiteException -> L61 java.lang.Throwable -> L82
            r8.setCustomVariable(r1)     // Catch: android.database.sqlite.SQLiteException -> L61 java.lang.Throwable -> L82
            goto L2a
        L61:
            r1 = move-exception
            r10 = r1
            r1 = r0
            r0 = r10
        L65:
            java.lang.String r2 = "googleanalytics"
            java.lang.String r0 = r0.toString()     // Catch: java.lang.Throwable -> L87
            android.util.Log.e(r2, r0)     // Catch: java.lang.Throwable -> L87
            if (r1 == 0) goto L73
            r1.close()
        L73:
            return r8
        L74:
            if (r0 == 0) goto L73
            r0.close()
            goto L73
        L7a:
            r0 = move-exception
            r1 = r9
        L7c:
            if (r1 == 0) goto L81
            r1.close()
        L81:
            throw r0
        L82:
            r1 = move-exception
            r10 = r1
            r1 = r0
            r0 = r10
            goto L7c
        L87:
            r0 = move-exception
            goto L7c
        L89:
            r0 = move-exception
            r1 = r9
            goto L65
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.apps.analytics.PersistentEventStore.getCustomVariables(long):com.google.android.apps.analytics.CustomVariableBuffer");
    }

    @Override // com.google.android.apps.analytics.EventStore
    public int getNumStoredEvents() {
        try {
            if (this.compiledCountStatement == null) {
                this.compiledCountStatement = this.databaseHelper.getReadableDatabase().compileStatement("SELECT COUNT(*) from events");
            }
            return (int) this.compiledCountStatement.simpleQueryForLong();
        } catch (SQLiteException e) {
            Log.e("googleanalytics", e.toString());
            return 0;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x0042  */
    @Override // com.google.android.apps.analytics.EventStore
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String getReferrer() throws java.lang.Throwable {
        /*
            r10 = this;
            r8 = 0
            com.google.android.apps.analytics.PersistentEventStore$DataBaseHelper r0 = r10.databaseHelper     // Catch: android.database.sqlite.SQLiteException -> L2c java.lang.Throwable -> L3e
            android.database.sqlite.SQLiteDatabase r0 = r0.getReadableDatabase()     // Catch: android.database.sqlite.SQLiteException -> L2c java.lang.Throwable -> L3e
            java.lang.String r1 = "install_referrer"
            r2 = 1
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch: android.database.sqlite.SQLiteException -> L2c java.lang.Throwable -> L3e
            r3 = 0
            java.lang.String r4 = "referrer"
            r2[r3] = r4     // Catch: android.database.sqlite.SQLiteException -> L2c java.lang.Throwable -> L3e
            r3 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            r7 = 0
            android.database.Cursor r0 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch: android.database.sqlite.SQLiteException -> L2c java.lang.Throwable -> L3e
            boolean r1 = r0.moveToFirst()     // Catch: java.lang.Throwable -> L46 android.database.sqlite.SQLiteException -> L4d
            if (r1 == 0) goto L52
            r1 = 0
            java.lang.String r1 = r0.getString(r1)     // Catch: java.lang.Throwable -> L46 android.database.sqlite.SQLiteException -> L4d
        L25:
            if (r0 == 0) goto L2a
            r0.close()
        L2a:
            r0 = r1
        L2b:
            return r0
        L2c:
            r0 = move-exception
            r1 = r8
        L2e:
            java.lang.String r2 = "googleanalytics"
            java.lang.String r0 = r0.toString()     // Catch: java.lang.Throwable -> L4b
            android.util.Log.e(r2, r0)     // Catch: java.lang.Throwable -> L4b
            if (r1 == 0) goto L3c
            r1.close()
        L3c:
            r0 = r8
            goto L2b
        L3e:
            r0 = move-exception
            r1 = r8
        L40:
            if (r1 == 0) goto L45
            r1.close()
        L45:
            throw r0
        L46:
            r1 = move-exception
            r9 = r1
            r1 = r0
            r0 = r9
            goto L40
        L4b:
            r0 = move-exception
            goto L40
        L4d:
            r1 = move-exception
            r9 = r1
            r1 = r0
            r0 = r9
            goto L2e
        L52:
            r1 = r8
            goto L25
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.apps.analytics.PersistentEventStore.getReferrer():java.lang.String");
    }

    @Override // com.google.android.apps.analytics.EventStore
    public int getStoreId() {
        return this.storeId;
    }

    CustomVariableBuffer getVisitorVarBuffer() {
        CustomVariableBuffer customVariableBuffer = new CustomVariableBuffer();
        try {
            Cursor cursorQuery = this.databaseHelper.getReadableDatabase().query("custom_var_cache", null, "cv_scope=1", null, null, null, null);
            while (cursorQuery.moveToNext()) {
                customVariableBuffer.setCustomVariable(new CustomVariable(cursorQuery.getInt(cursorQuery.getColumnIndex("cv_index")), cursorQuery.getString(cursorQuery.getColumnIndex("cv_name")), cursorQuery.getString(cursorQuery.getColumnIndex("cv_value")), cursorQuery.getInt(cursorQuery.getColumnIndex("cv_scope"))));
            }
            cursorQuery.close();
        } catch (SQLiteException e) {
            Log.e("googleanalytics", e.toString());
        }
        return customVariableBuffer;
    }

    @Override // com.google.android.apps.analytics.EventStore
    public Event[] peekEvents() {
        return peekEvents(1000);
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x00f6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.google.android.apps.analytics.Event[] peekEvents(int r22) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 259
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.apps.analytics.PersistentEventStore.peekEvents(int):com.google.android.apps.analytics.Event[]");
    }

    void putCustomVariables(Event event, long j) {
        try {
            SQLiteDatabase writableDatabase = this.databaseHelper.getWritableDatabase();
            CustomVariableBuffer customVariableBuffer = event.getCustomVariableBuffer();
            if (this.useStoredVisitorVars) {
                if (customVariableBuffer == null) {
                    customVariableBuffer = new CustomVariableBuffer();
                    event.setCustomVariableBuffer(customVariableBuffer);
                }
                CustomVariableBuffer visitorVarBuffer = getVisitorVarBuffer();
                for (int i = 1; i <= 5; i++) {
                    CustomVariable customVariableAt = visitorVarBuffer.getCustomVariableAt(i);
                    CustomVariable customVariableAt2 = customVariableBuffer.getCustomVariableAt(i);
                    if (customVariableAt != null && customVariableAt2 == null) {
                        customVariableBuffer.setCustomVariable(customVariableAt);
                    }
                }
                this.useStoredVisitorVars = false;
            }
            if (customVariableBuffer != null) {
                for (int i2 = 1; i2 <= 5; i2++) {
                    if (!customVariableBuffer.isIndexAvailable(i2)) {
                        CustomVariable customVariableAt3 = customVariableBuffer.getCustomVariableAt(i2);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("event_id", Long.valueOf(j));
                        contentValues.put("cv_index", Integer.valueOf(customVariableAt3.getIndex()));
                        contentValues.put("cv_name", customVariableAt3.getName());
                        contentValues.put("cv_scope", Integer.valueOf(customVariableAt3.getScope()));
                        contentValues.put("cv_value", customVariableAt3.getValue());
                        writableDatabase.insert("custom_variables", "event_id", contentValues);
                        writableDatabase.update("custom_var_cache", contentValues, "cv_index=" + customVariableAt3.getIndex(), null);
                    }
                }
            }
        } catch (SQLiteException e) {
            Log.e("googleanalytics", e.toString());
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x0130  */
    @Override // com.google.android.apps.analytics.EventStore
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void putEvent(com.google.android.apps.analytics.Event r13) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 318
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.apps.analytics.PersistentEventStore.putEvent(com.google.android.apps.analytics.Event):void");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:20:0x00c9  */
    /* JADX WARN: Type inference failed for: r1v0 */
    /* JADX WARN: Type inference failed for: r1v1 */
    /* JADX WARN: Type inference failed for: r1v3, types: [android.database.Cursor] */
    @Override // com.google.android.apps.analytics.EventStore
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void startNewVisit() throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 210
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.apps.analytics.PersistentEventStore.startNewVisit():void");
    }

    void storeUpdatedSession() {
        try {
            SQLiteDatabase writableDatabase = this.databaseHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("timestamp_previous", Long.valueOf(this.timestampPrevious));
            contentValues.put("timestamp_current", Long.valueOf(this.timestampCurrent));
            contentValues.put("visits", Integer.valueOf(this.visits));
            writableDatabase.update("session", contentValues, "timestamp_first=?", new String[]{Long.toString(this.timestampFirst)});
            this.sessionUpdated = true;
        } catch (SQLiteException e) {
            Log.e("googleanalytics", e.toString());
        }
    }
}
