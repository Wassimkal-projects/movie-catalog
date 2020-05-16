package com.axeane.moviecatalog.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends AbstractThrowableProblem {
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(final String title) {
        super(URI.create("/resource-not-found-exception"), title, Status.NOT_FOUND);
    }
}
