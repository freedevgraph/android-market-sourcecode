package com.google.android.apps.analytics;

import android.util.Log;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
class NetworkRequestUtil {
    NetworkRequestUtil() {
    }

    public static String constructEventRequestPath(Event event, String str) {
        Locale locale = Locale.getDefault();
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(String.format("5(%s*%s", encode(event.category), encode(event.action)));
        if (event.label != null) {
            sb2.append("*").append(encode(event.label));
        }
        sb2.append(")");
        if (event.value > -1) {
            sb2.append(String.format("(%d)", Integer.valueOf(event.value)));
        }
        sb2.append(getCustomVariableParams(event));
        sb.append("/__utm.gif");
        sb.append("?utmwv=4.5ma");
        sb.append("&utmn=").append(event.randomVal);
        sb.append("&utmt=event");
        sb.append("&utme=").append(sb2.toString());
        sb.append("&utmcs=UTF-8");
        sb.append(String.format("&utmsr=%dx%d", Integer.valueOf(event.screenWidth), Integer.valueOf(event.screenHeight)));
        sb.append(String.format("&utmul=%s-%s", locale.getLanguage(), locale.getCountry()));
        sb.append("&utmac=").append(event.accountId);
        sb.append("&utmcc=").append(getEscapedCookieString(event, str));
        Log.d("NetworkRequestUtil/ConstructEventRequestPath", sb.toString());
        return sb.toString();
    }

    public static String constructPageviewRequestPath(Event event, String str) {
        String str2 = event.action != null ? event.action : "";
        if (!str2.startsWith("/")) {
            str2 = "/" + str2;
        }
        String strEncode = encode(str2);
        String customVariableParams = getCustomVariableParams(event);
        Locale locale = Locale.getDefault();
        StringBuilder sb = new StringBuilder();
        sb.append("/__utm.gif");
        sb.append("?utmwv=4.5ma");
        sb.append("&utmn=").append(event.randomVal);
        if (customVariableParams.length() > 0) {
            sb.append("&utme=").append(customVariableParams);
        }
        sb.append("&utmcs=UTF-8");
        sb.append(String.format("&utmsr=%dx%d", Integer.valueOf(event.screenWidth), Integer.valueOf(event.screenHeight)));
        sb.append(String.format("&utmul=%s-%s", locale.getLanguage(), locale.getCountry()));
        sb.append("&utmp=").append(strEncode);
        sb.append("&utmac=").append(event.accountId);
        sb.append("&utmcc=").append(getEscapedCookieString(event, str));
        Log.d("NetworkRequestUtil/ConstructPageviewRequestPath", sb.toString());
        return sb.toString();
    }

    private static void createX10Project(CustomVariable[] customVariableArr, StringBuilder sb, int i) {
        sb.append(i).append("(");
        boolean z = true;
        for (int i2 = 0; i2 < customVariableArr.length; i2++) {
            if (customVariableArr[i2] != null) {
                CustomVariable customVariable = customVariableArr[i2];
                if (z) {
                    z = false;
                } else {
                    sb.append("*");
                }
                sb.append(customVariable.getIndex()).append("!");
                switch (i) {
                    case 8:
                        sb.append(x10Escape(encode(customVariable.getName())));
                        break;
                    case 9:
                        sb.append(x10Escape(encode(customVariable.getValue())));
                        break;
                    case 11:
                        sb.append(customVariable.getScope());
                        break;
                }
            }
        }
        sb.append(")");
    }

    private static String encode(String str) {
        return AnalyticsParameterEncoder.encode(str);
    }

    public static String getCustomVariableParams(Event event) {
        StringBuilder sb = new StringBuilder();
        CustomVariableBuffer customVariableBuffer = event.getCustomVariableBuffer();
        if (customVariableBuffer == null || !customVariableBuffer.hasCustomVariables()) {
            return "";
        }
        CustomVariable[] customVariableArray = customVariableBuffer.getCustomVariableArray();
        createX10Project(customVariableArray, sb, 8);
        createX10Project(customVariableArray, sb, 9);
        createX10Project(customVariableArray, sb, 11);
        return sb.toString();
    }

    public static String getEscapedCookieString(Event event, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("__utma=");
        sb.append("999").append(".");
        sb.append(event.userId).append(".");
        sb.append(event.timestampFirst).append(".");
        sb.append(event.timestampPrevious).append(".");
        sb.append(event.timestampCurrent).append(".");
        sb.append(event.visits);
        if (str != null) {
            sb.append("+__utmz=");
            sb.append("999").append(".");
            sb.append(event.timestampFirst).append(".");
            sb.append("1.1.");
            sb.append(str);
        }
        return encode(sb.toString());
    }

    private static String x10Escape(String str) {
        return str.replace("'", "'0").replace(")", "'1").replace("*", "'2").replace("!", "'3");
    }
}
