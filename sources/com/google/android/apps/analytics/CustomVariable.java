package com.google.android.apps.analytics;

/* JADX INFO: loaded from: classes.dex */
class CustomVariable {
    private final int index;
    private final String name;
    private final int scope;
    private final String value;

    public CustomVariable(int i, String str, String str2, int i2) {
        if (i2 != 1 && i2 != 2 && i2 != 3) {
            throw new IllegalArgumentException("Invalid Scope:" + i2);
        }
        if (i < 1 || i > 5) {
            throw new IllegalArgumentException("Index must be between 1 and 5 inclusive.");
        }
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("Invalid argument for name:  Cannot be empty");
        }
        if (str2 == null || str2.length() == 0) {
            throw new IllegalArgumentException("Invalid argument for value:  Cannot be empty");
        }
        int length = AnalyticsParameterEncoder.encode(str + str2).length();
        if (length > 64) {
            throw new IllegalArgumentException("Encoded form of name and value must not exceed 64 characters combined.  Character length: " + length);
        }
        this.index = i;
        this.scope = i2;
        this.name = str;
        this.value = str2;
    }

    public int getIndex() {
        return this.index;
    }

    public String getName() {
        return this.name;
    }

    public int getScope() {
        return this.scope;
    }

    public String getValue() {
        return this.value;
    }
}
