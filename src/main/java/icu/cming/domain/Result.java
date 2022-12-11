package icu.cming.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Result {

    private static volatile Result result;
    private final List<Student> success = Collections.synchronizedList(new ArrayList<>());
    private final List<Student> fail = Collections.synchronizedList(new ArrayList<>());

    private Result() {
    }

    public static Result getInstance() {
        if (result == null) {
            synchronized (Result.class) {
                if (result == null) {
                    result = new Result();
                }
            }
        }
        return result;
    }

    public List<Student> getSuccess() {
        return success;
    }

    public List<Student> getFail() {
        return fail;
    }

}
