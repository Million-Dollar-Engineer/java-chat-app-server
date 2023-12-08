package chatapp.internal.result;

public class Result<T> {
    private final T object;
    private final Exception error;

    private Result(T object, Exception error) {
        this.object = object;
        this.error = error;
    }

    public static <T> Result<T> success(T object) {
        return new Result<>(object, null);
    }

    public static <T> Result<T> failure(Exception error) {
        return new Result<>(null, error);
    }

    public T getObject() {
        return object;
    }

    public Exception getError() {
        return error;
    }
}
