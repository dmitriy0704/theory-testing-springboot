package dev.folomkin.mockito.spying_demo;

import java.util.ArrayList;
import java.util.List;

public class ListManager {
    public List<String> createList() {
        return new ArrayList<>();
    }

    public int getListSize(List<String> list) {
        return list.size();
    }
}