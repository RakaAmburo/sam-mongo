package com.sam.repo.webClients;

public class MongoRepoClientRetryExhaustedException extends RuntimeException {

  public MongoRepoClientRetryExhaustedException(final String message) {
    super(message);
  }
}
