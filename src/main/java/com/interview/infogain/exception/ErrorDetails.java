package com.interview.infogain.exception;

import java.util.Date;

public record ErrorDetails(Date timestamp, String message, String details) {

}
