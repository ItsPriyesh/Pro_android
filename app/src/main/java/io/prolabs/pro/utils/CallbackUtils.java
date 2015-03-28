package io.prolabs.pro.utils;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CallbackUtils {
    public static <T> Callback<T> callback(SuccessPart<T> successPart, FailurePart failurePart) {
        return new Callback<T>() {
            public void success(T t, Response resp) {
                successPart.success(t);
            }

            public void failure(RetrofitError err) {
                failurePart.failure(err);
            }
        };
    }

    public interface SuccessPart<T> {
        public void success(T t);
    }

    public interface FailurePart {
        public void failure(RetrofitError err);
    }
}
