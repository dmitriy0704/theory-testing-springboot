package dev.folomkin.mockito.mock_annotation;

import dev.folomkin.mockito.mock_annotation_demo.DataService;
import dev.folomkin.mockito.mock_annotation_demo.spy.ListManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListManagerTest {

    @Spy
    ListManager spyListManager;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetListSize() {

        List<String> list = new ArrayList<>(Arrays.asList("one", "two", "three"));
        Mockito.doReturn(100).when(spyListManager).getListSize(list);
        assertEquals(100, spyListManager.getListSize(list));
    }
}
