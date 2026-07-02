package com.google.android.apps.analytics;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import com.google.android.apps.analytics.Dispatcher;
import com.google.android.apps.analytics.PipelinedRequester;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Locale;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.ParseException;
import org.apache.http.message.BasicHttpRequest;

/* JADX INFO: loaded from: classes.dex */
class NetworkDispatcher implements Dispatcher {
    private static final HttpHost GOOGLE_ANALYTICS_HOST = new HttpHost("www.google-analytics.com", 80);
    private DispatcherThread dispatcherThread;
    private final String userAgent;

    private static class DispatcherThread extends HandlerThread {
        private final Dispatcher.Callbacks callbacks;
        private AsyncDispatchTask currentTask;
        private Handler handlerExecuteOnDispatcherThread;
        private int lastStatusCode;
        private int maxEventsPerRequest;
        private final PipelinedRequester pipelinedRequester;
        private final String referrer;
        private long retryInterval;
        private final String userAgent;

        private class AsyncDispatchTask implements Runnable {
            private final LinkedList<Event> events = new LinkedList<>();

            public AsyncDispatchTask(Event[] eventArr) {
                Collections.addAll(this.events, eventArr);
            }

            private void dispatchSomePendingEvents() throws HttpException, ParseException, IOException {
                int i = 0;
                while (true) {
                    int i2 = i;
                    if (i2 >= this.events.size() || i2 >= DispatcherThread.this.maxEventsPerRequest) {
                        break;
                    }
                    Event event = this.events.get(i2);
                    BasicHttpRequest basicHttpRequest = new BasicHttpRequest("GET", "__##GOOGLEPAGEVIEW##__".equals(event.category) ? NetworkRequestUtil.constructPageviewRequestPath(event, DispatcherThread.this.referrer) : NetworkRequestUtil.constructEventRequestPath(event, DispatcherThread.this.referrer));
                    basicHttpRequest.addHeader("Host", NetworkDispatcher.GOOGLE_ANALYTICS_HOST.getHostName());
                    basicHttpRequest.addHeader("User-Agent", DispatcherThread.this.userAgent);
                    DispatcherThread.this.pipelinedRequester.addRequest(basicHttpRequest);
                    i = i2 + 1;
                }
                DispatcherThread.this.pipelinedRequester.sendRequests();
            }

            public Event removeNextEvent() {
                return this.events.poll();
            }

            @Override // java.lang.Runnable
            public void run() {
                DispatcherThread.this.currentTask = this;
                for (int i = 0; i < 5 && this.events.size() > 0; i++) {
                    long jRandom = 0;
                    try {
                        if (DispatcherThread.this.lastStatusCode == 500 || DispatcherThread.this.lastStatusCode == 503) {
                            jRandom = (long) (Math.random() * DispatcherThread.this.retryInterval);
                            if (DispatcherThread.this.retryInterval < 256) {
                                DispatcherThread.access$630(DispatcherThread.this, 2L);
                            }
                        } else {
                            DispatcherThread.this.retryInterval = 2L;
                        }
                        Thread.sleep(jRandom * 1000);
                        dispatchSomePendingEvents();
                    } catch (IOException e) {
                        Log.w("googleanalytics", "Problem with socket or streams.", e);
                    } catch (InterruptedException e2) {
                        Log.w("googleanalytics", "Couldn't sleep.", e2);
                    } catch (HttpException e3) {
                        Log.w("googleanalytics", "Problem with http streams.", e3);
                    }
                }
                DispatcherThread.this.pipelinedRequester.finishedCurrentRequests();
                DispatcherThread.this.callbacks.dispatchFinished();
                DispatcherThread.this.currentTask = null;
            }
        }

        private class RequesterCallbacks implements PipelinedRequester.Callbacks {
            private RequesterCallbacks() {
            }

            @Override // com.google.android.apps.analytics.PipelinedRequester.Callbacks
            public void pipelineModeChanged(boolean z) {
                if (z) {
                    DispatcherThread.this.maxEventsPerRequest = 30;
                } else {
                    DispatcherThread.this.maxEventsPerRequest = 1;
                }
            }

            @Override // com.google.android.apps.analytics.PipelinedRequester.Callbacks
            public void requestSent() {
                Event eventRemoveNextEvent;
                if (DispatcherThread.this.currentTask == null || (eventRemoveNextEvent = DispatcherThread.this.currentTask.removeNextEvent()) == null) {
                    return;
                }
                DispatcherThread.this.callbacks.eventDispatched(eventRemoveNextEvent.eventId);
            }

            @Override // com.google.android.apps.analytics.PipelinedRequester.Callbacks
            public void serverError(int i) {
                DispatcherThread.this.lastStatusCode = i;
            }
        }

        private DispatcherThread(Dispatcher.Callbacks callbacks, PipelinedRequester pipelinedRequester, String str, String str2) {
            super("DispatcherThread");
            this.maxEventsPerRequest = 30;
            this.currentTask = null;
            this.callbacks = callbacks;
            this.referrer = str;
            this.userAgent = str2;
            this.pipelinedRequester = pipelinedRequester;
            this.pipelinedRequester.installCallbacks(new RequesterCallbacks());
        }

        private DispatcherThread(Dispatcher.Callbacks callbacks, String str, String str2) {
            this(callbacks, new PipelinedRequester(NetworkDispatcher.GOOGLE_ANALYTICS_HOST), str, str2);
        }

        static /* synthetic */ long access$630(DispatcherThread dispatcherThread, long j) {
            long j2 = dispatcherThread.retryInterval * j;
            dispatcherThread.retryInterval = j2;
            return j2;
        }

        public void dispatchEvents(Event[] eventArr) {
            if (this.handlerExecuteOnDispatcherThread != null) {
                this.handlerExecuteOnDispatcherThread.post(new AsyncDispatchTask(eventArr));
            }
        }

        @Override // android.os.HandlerThread
        protected void onLooperPrepared() {
            this.handlerExecuteOnDispatcherThread = new Handler();
        }
    }

    public NetworkDispatcher() {
        this("GoogleAnalytics", "1.1");
    }

    public NetworkDispatcher(String str, String str2) {
        Locale locale = Locale.getDefault();
        Object[] objArr = new Object[7];
        objArr[0] = str;
        objArr[1] = str2;
        objArr[2] = Build.VERSION.RELEASE;
        objArr[3] = locale.getLanguage() != null ? locale.getLanguage().toLowerCase() : "en";
        objArr[4] = locale.getCountry() != null ? locale.getCountry().toLowerCase() : "";
        objArr[5] = Build.MODEL;
        objArr[6] = Build.ID;
        this.userAgent = String.format("%s/%s (Linux; U; Android %s; %s-%s; %s; Build/%s)", objArr);
    }

    @Override // com.google.android.apps.analytics.Dispatcher
    public void dispatchEvents(Event[] eventArr) {
        if (this.dispatcherThread != null) {
            this.dispatcherThread.dispatchEvents(eventArr);
        }
    }

    @Override // com.google.android.apps.analytics.Dispatcher
    public void init(Dispatcher.Callbacks callbacks, String str) {
        stop();
        this.dispatcherThread = new DispatcherThread(callbacks, str, this.userAgent);
        this.dispatcherThread.start();
    }

    public void stop() {
        if (this.dispatcherThread == null || this.dispatcherThread.getLooper() == null) {
            return;
        }
        this.dispatcherThread.getLooper().quit();
        this.dispatcherThread = null;
    }
}
