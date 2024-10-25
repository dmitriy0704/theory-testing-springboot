package dev.folomkin.mockito.mock_annotation;

import dev.folomkin.mockito.mock_annotation_demo.DataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataServiceTest {

    // Use annotation
    @Mock
    DataService mockDataService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRetrieveData() {
        Mockito
                .when(mockDataService.retrieveData())
                .thenReturn(5);

        assertEquals(5, mockDataService.retrieveData());
    }
}
