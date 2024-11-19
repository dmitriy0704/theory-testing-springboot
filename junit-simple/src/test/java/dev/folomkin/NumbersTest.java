package dev.folomkin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class NumbersTest {

    @Test
    void num() {
        Numbers numbers = new Numbers();
        int actual = numbers.num(5);
        int expected = 15;
        assertEquals(expected, actual);
    }
}