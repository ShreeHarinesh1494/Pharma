package com.cts.Pharma.exception;

public class NoUserFoundException extends RuntimeException
{
    public NoUserFoundException(String msg)
    {
        super(msg);
    }
}
