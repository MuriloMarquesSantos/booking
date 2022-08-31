package com.alten.booking.exceptions;

public class NotAvailableException extends RuntimeException{
  public NotAvailableException(String message) {
    super(message);
  }
}
