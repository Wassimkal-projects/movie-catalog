package com.axeane.moviecatalog.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestAlertException extends AbstractThrowableProblem {
    private static final long serialVersionUID = 1L;

    public BadRequestAlertException(final String title) {
        super(URI.create("/bad-request-exception"), title, Status.BAD_REQUEST);
    }
}
