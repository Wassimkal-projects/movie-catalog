package com.axeane.moviecatalog.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FormatNotValidException extends AbstractThrowableProblem {
    private static final long serialVersionUID = 1L;

    public FormatNotValidException(final String title) {
        super(URI.create("/format-not-valid-exception"), title, Status.BAD_REQUEST);
    }
}
