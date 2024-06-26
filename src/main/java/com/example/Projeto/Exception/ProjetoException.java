package com.example.Projeto.Exception;

public class Exception extends RuntimeException {

    public void ConsultaException(String message) {
        super(message);
    }

    public void PacienteException(String message, Throwable cause) {
        super(message, cause);
    }
}
