package com.basic.board.domain;

import org.springframework.data.domain.Sort.Direction;

public class PageRequest {
  private int page = 1;
  private int size = 10;
  private Direction direction = Direction.DESC;

  public void setPage(int page) {
    this.page = page <= 0 ? 1 : page;
  }

  public void setSize(int size) {
    int DEFAULT_SIZE = 10;
    int MAX_SIZE = 50;
    if (size <= 0) {
      size = DEFAULT_SIZE;
    }
    this.size = size > MAX_SIZE ? DEFAULT_SIZE : size;
  }

  public void setDirection(Direction direction) {
    this.direction = direction;
  }

  public org.springframework.data.domain.PageRequest of() {
    return org.springframework.data.domain.PageRequest.of(page - 1, size, direction, "createAt");
  }
}
