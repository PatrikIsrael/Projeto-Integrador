package com.example.Projeto.Exception;

public class ProjetoException extends RuntimeException {

    public ProjetoException(String message) {
        super(message);
    }

    public ProjetoException(String message, Throwable cause) {
        super(message, cause);
    }
}
