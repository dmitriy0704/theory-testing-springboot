package dev.folomkin.mockito.mock_annotation;

import dev.folomkin.mockito.mock_annotation_demo.DataProcessor;
import dev.folomkin.mockito.mock_annotation_demo.DataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@SpringBootTest
@AutoConfigureMockMvc
class DataProcessorTest {

    @Mock
    DataService mockDataService;

    @InjectMocks
    DataProcessor dataProcessor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcess() {
        Mockito.when(mockDataService.retrieveData()).thenReturn(5);
        assertEquals(10, dataProcessor.process());
    }
}