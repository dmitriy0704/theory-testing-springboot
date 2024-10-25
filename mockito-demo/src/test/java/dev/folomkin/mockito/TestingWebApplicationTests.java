package dev.folomkin.mockito;

import dev.folomkin.mockito.argumentMatchers_demo.ArgumentMatchers_UserService;
import dev.folomkin.mockito.argumentMatchers_demo.User;
import dev.folomkin.mockito.db_demo.DataService;
import dev.folomkin.mockito.spying_demo.ListManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class TestingWebApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    // Demo mock Application
    @Test
    void shouldReturn200() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(
                        content()
                                .string(containsString("Hello, World"))
                );
    }

    // Mock interface
    @Test
    void testMockInterface() {
        // Создание мока:
        DataService mockDataService = Mockito.mock(DataService.class);
        // Создание тестовых переменных
        double input1 = 10.0, input2 = 20.0;
        // Имитация вызова метода
        mockDataService.printSum(input1, input2);
        // Верификация: был ли вызван метод placeOrder с testOrder
        Mockito.verify(mockDataService).printSum(input1, input2);
    }


    // ArgumentMatchers
    @Test
    void test_ArgumentMatchers() {
        ArgumentMatchers_UserService mockUserService = Mockito.mock(ArgumentMatchers_UserService.class);
        User specificUser = new User("Alice", 10);

        // Стаббинг: метод должен быть вызван с конкретными аргументами
        Mockito
                .when(mockUserService
                        .findUser(Mockito.eq("Bob"), Mockito.eq(25)))
                .thenReturn(specificUser);

        // Вызов метода с конкретными аргументами
        User result = mockUserService.findUser("Bob", 25);

        // Проверка
        assertEquals(specificUser, result);
    }

    // spy
    @Test
    void test_spy() {
        ListManager spyListManager = Mockito.spy(new ListManager());

        // Использование реального метода
        List<String> list = spyListManager.createList();
        list.add("Item");

        // Переопределение поведения метода getListSize
        Mockito
                .when(spyListManager.getListSize(list))
                .thenReturn(100);

        // Тестирование
        int size = spyListManager.getListSize(list);

        // Проверка
        assertEquals(100, size);
    }



}