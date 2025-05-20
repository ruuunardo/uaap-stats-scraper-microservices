package com.teamr.runardo.uaapstatscraper.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class GitTest {
    @Test
    void test() {
        Long id = 9L;
        Integer id2 = 9;
        String string = id.toString();
        byte[] bytes = string.getBytes();
        String string1 = id2.toString();
        byte[] bytes2 = string1.getBytes();

        System.out.println(bytes2.length);

        System.out.println(bytes);

    }
}