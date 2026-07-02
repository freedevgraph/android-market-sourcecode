package com.google.android.apps.analytics;

/* JADX INFO: loaded from: classes.dex */
class CustomVariableBuffer {
    private CustomVariable[] customVariables = new CustomVariable[5];

    private void throwOnInvalidIndex(int i) {
        if (i < 1 || i > 5) {
            throw new IllegalArgumentException("Index must be between 1 and 5 inclusive.");
        }
    }

    public CustomVariable[] getCustomVariableArray() {
        return (CustomVariable[]) this.customVariables.clone();
    }

    public CustomVariable getCustomVariableAt(int i) {
        throwOnInvalidIndex(i);
        return this.customVariables[i - 1];
    }

    public boolean hasCustomVariables() {
        for (int i = 0; i < this.customVariables.length; i++) {
            if (this.customVariables[i] != null) {
                return true;
            }
        }
        return false;
    }

    public boolean isIndexAvailable(int i) {
        throwOnInvalidIndex(i);
        return this.customVariables[i - 1] == null;
    }

    public void setCustomVariable(CustomVariable customVariable) {
        throwOnInvalidIndex(customVariable.getIndex());
        this.customVariables[customVariable.getIndex() - 1] = customVariable;
    }
}
