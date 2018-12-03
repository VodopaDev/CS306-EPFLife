package ch.epfl.sweng.zuluzulu.Database;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.Executor;

class TaskMock <T> extends Task<T> {
        @Override
        public boolean isComplete() {
            return true;
        }

        @Override
        public boolean isSuccessful() {
            return true;
        }

        @Override
        public boolean isCanceled() {
            return false;
        }

        @Nullable
        @Override
        public T getResult() {
            throw new UnsupportedOperationException();
        }

        @Nullable
        @Override
        public <X extends Throwable> T getResult(@NonNull Class<X> aClass) {
            throw new UnsupportedOperationException();
        }

        @Nullable
        @Override
        public Exception getException() {
            return new Exception("mock");
        }

        @NonNull
        @Override
        public com.google.android.gms.tasks.Task<T> addOnSuccessListener(@NonNull OnSuccessListener<? super T> onSuccessListener) {
            return this;
        }

        @NonNull
        @Override
        public com.google.android.gms.tasks.Task<T> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super T> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public com.google.android.gms.tasks.Task<T> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super T> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public com.google.android.gms.tasks.Task<T> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
            onFailureListener.onFailure(this.getException());
            return this;
        }

        @NonNull
        @Override
        public com.google.android.gms.tasks.Task<T> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public com.google.android.gms.tasks.Task<T> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
            return null;
        }
}
