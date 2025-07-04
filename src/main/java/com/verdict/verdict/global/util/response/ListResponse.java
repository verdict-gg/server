package com.verdict.verdict.global.util.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;


@Getter
@RequiredArgsConstructor
public class ListResponse<T> extends CommonResponse {
    private List<T> dataList;
    private int count;


    public ListResponse(HttpStatus status, String message, List<T> dataList) {
        super(status, message);
        this.dataList = dataList;
        this.count = dataList.size();
    }

    public ListResponse(List<T> dataList) {
        this.dataList = dataList;
        this.count = dataList.size();
    }
}