package com.android.volley;

import android.os.Handler;
import com.android.volley.Response;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public class ExecutorDelivery implements ResponseDelivery {
    private int mDiscardBefore = 0;
    private final Executor mResponsePoster;

    public ExecutorDelivery(final Handler handler) {
        this.mResponsePoster = new Executor() { // from class: com.android.volley.ExecutorDelivery.1
            @Override // java.util.concurrent.Executor
            public void execute(Runnable command) {
                handler.post(command);
            }
        };
    }

    @Override // com.android.volley.ResponseDelivery
    public void postResponse(Request<?> request, Response<?> response) {
        request.addMarker("post-response");
        this.mResponsePoster.execute(new ResponseDeliveryRunnable(request, response));
    }

    @Override // com.android.volley.ResponseDelivery
    public void postError(Request<?> request, Response.ErrorCode error) {
        request.addMarker("post-error");
        Response<?> response = Response.error(error, null);
        this.mResponsePoster.execute(new ResponseDeliveryRunnable(request, response));
    }

    @Override // com.android.volley.ResponseDelivery
    public void discardBefore(int sequence) {
        this.mDiscardBefore = sequence;
    }

    private class ResponseDeliveryRunnable implements Runnable {
        private final Request mRequest;
        private final Response mResponse;

        public ResponseDeliveryRunnable(Request request, Response response) {
            this.mRequest = request;
            this.mResponse = response;
        }

        @Override // java.lang.Runnable
        public void run() {
            boolean shouldDrain = this.mRequest.isDrainable() && this.mRequest.getSequence() < ExecutorDelivery.this.mDiscardBefore;
            if (!shouldDrain && !this.mRequest.isCanceled()) {
                if (this.mResponse.error == Response.ErrorCode.OK && this.mResponse.result != 0) {
                    this.mRequest.deliverResponse(this.mResponse.result);
                } else {
                    this.mRequest.deliverError(this.mResponse.error, this.mResponse.message);
                }
                if (this.mResponse.intermediate) {
                    this.mRequest.addMarker("intermediate-response");
                    return;
                } else {
                    this.mRequest.finishMarking("done");
                    return;
                }
            }
            this.mRequest.finishMarking("canceled-at-delivery");
        }
    }
}
