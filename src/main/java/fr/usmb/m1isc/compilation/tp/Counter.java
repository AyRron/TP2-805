package fr.usmb.m1isc.compilation.tp;

public class Counter {
  int count = 0;

  public void increment() {
    count++;
  }

  public String toString() {
    return Integer.toString(count);
  }
}
