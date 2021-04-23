package com.example.mobile_programming_map;

import java.util.concurrent.Callable;

public interface CustomCallable<ArrayList> extends Callable<ArrayList> {
    void setDataAfterLoading(ArrayList result);
    void setUiForLoading();
}