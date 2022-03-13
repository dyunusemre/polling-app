package com.api.poling.question.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Status {
    A("Approved"),
    W("Waiting");

    private final String status;

}
