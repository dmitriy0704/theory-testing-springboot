# Mockito

Создание мока интерфейса:

```java
public interface DataService {
    double retrieveData();
}

public class DataProcessor {
    private final DataService dataService;

    public DataProcessor(DataService dataService) {
        this.dataService = dataService;
    }

    public double processData() {
        return dataService.retrieveData() * 2;
    }
}

@Test
void testMockInterface() {
    DataService mockDataService = Mockito.mock(DataService.class);
    Mockito.when(mockDataService.retrieveData()).thenReturn(15.0);
    DataProcessor processor = new DataProcessor(mockDataService);
    double result = processor.processData();
    assertEquals(30.0, result, 0.01);
}
```

`mockDataService` - это мок-объект, который можно использовать в тестах. Он
будет иметь поведение по умолчанию для всех методов (например, возвращать 0 для
методов, возвращающих double).

Чтобы мок был полезен, нам нужно определить его поведение. Допустим, мы хотим,
чтобы метод retrieveData() возвращал определенное значение. Сделать это можно
следующим образом:

## Методы заглушек

`when-thenReturn` - позволяет определить возвращаемое значение для метода
мок-объекта.

```java
public interface CalculatorService {
    double add(double input1, double input2);
}

@Test
public void whenThenReturnExample() {
    CalculatorService calculatorService = Mockito.mock(CalculatorService.class);

    // Стаббинг: определение поведения
    Mockito.when(calculatorService.add(10.0, 20.0)).thenReturn(30.0);

    // Проверка: метод add возвращает 30.0
    assertEquals(30.0, calculatorService.add(10.0, 20.0), 0.01);
}
```

`doReturn-when` - когда метод возвращает void.

Предположим, у нас есть метод void printSum(double input1, double input2),
который не возвращает значение, но мы хотим удостовериться, что он был вызван с
определенными параметрами. Мы можем использовать doReturn-when для этой цели:

```java
public interface CalculatorService {
    double add(double input1, double input2);

    void printSum(double input1, double input2);
}

@Test
void testMockInterface() {
    DataService mockDataService = Mockito.mock(DataService.class);
    Mockito.doNothing().when(mockDataService).printSum(10.0, 20.0);
    mockDataService.printSum(10.0, 20.0);
    Mockito.verify(mockDataService).printSum(10.0, 20.0);
}
```

## Верификация вызовов методов в Mockito

Может понадобиться убедиться, что метод `printSum()` был действительно вызван:

```java
public interface DataService {
    void printSum(double input1, double input2);
}

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
```

Проверка создания мока и того что метод вызван с определенными параметрами.

## Мокирование исключений

Мокирование исключений полезно, когда вы хотите тестировать поведение вашего
кода в условиях возникновения исключения.

Допустим, у нас есть интерфейс FileReader, который читает файл и может
выбрасывать IOException:

```java
public interface FileReader {
    String readFile(String path) throws IOException;
}
```

Теперь, представим, что мы хотим протестировать поведение нашего класса
FileProcessor, который зависит от FileReader, в ситуации, когда IOException
выбрасывается.

```java
public class FileProcessor {
    private FileReader fileReader;

    public FileProcessor(FileReader fileReader) {
        this.fileReader = fileReader;
    }

    public String processFile(String path) {
        try {
            return "Processed: " + fileReader.readFile(path);
        } catch (IOException e) {
            return "Error";
        }
    }
}
```

Чтобы протестировать этот случай, мы можем использовать Mockito для мокирования
IOException:

```java

@Test
public void whenIOException_thenReturnsError() throws IOException {
    // Создание мока
    FileReader mockFileReader = Mockito.mock(FileReader.class);

    // Мокирование исключения
    Mockito
            .when(mockFileReader.readFile(Mockito.anyString()))
            .thenThrow(new IOException()
            );

    // Тестирование
    FileProcessor fileProcessor = new FileProcessor(mockFileReader);
    String result = fileProcessor.processFile("test.txt");

    // Проверка
    assertEquals("Error", result);
}
```

В этом тесте мы гарантируем, что когда метод readFile вызывается, IOException
выбрасывается, и FileProcessor корректно обрабатывает это исключение.

Mockito также позволяет мокировать исключения для конкретных параметров
вызываемого метода. Это полезно, когда поведение метода зависит от
предоставленных аргументов.

```java

@Test
public void whenSpecificArgument_thenThrowException() throws IOException {
    // Создание мока
    FileReader mockFileReader = Mockito.mock(FileReader.class);

    // Мокирование исключения для конкретного аргумента
    Mockito
            .when(mockFileReader.readFile("specific.txt"))
            .thenThrow(new IOException());

    // Тестирование
    FileProcessor fileProcessor = new FileProcessor(mockFileReader);
    String result = fileProcessor.processFile("specific.txt");

    // Проверка
    assertEquals("Error", result);
}
```

Здесь мы гарантируем, что IOException будет выброшен только тогда, когда
readFile вызывается с "specific.txt" в качестве аргумента.

## ArgumentMatchers в Mockito

[argumentMatchers_demo](../mockito-demo/src/main/java/dev/folomkin/mockito/argumentMatchers_demo)

ArgumentMatchers - это специальные условия, которые мы можем применять к
аргументам вызываемых методов. Они позволяют определить правила для любых
аргументов или их определенных комбинаций.

Интерфейс UserService, который мы хотим протестировать:

```java
public interface UserService {
    User findUser(String username, int age);
}
```

Теперь, мы хотим замокировать этот сервис и определить поведение метода
findUser.

Допустим, нам не важно, с какими аргументами вызывается метод - мы хотим вернуть
определенного пользователя для любых значений.

```java

@Test
void test_ArgumentMatchers() {
    ArgumentMatchers_UserService mockUserService = Mockito.mock(ArgumentMatchers_UserService.class);
    User expectedUser = new User("Alice", 10);

    // Стаббинг с использованием any()
    Mockito
            .when(mockUserService.findUser(Mockito.anyString(), Mockito.anyInt()))
            .thenReturn(expectedUser);

    // Вызов метода с любыми аргументами
    User result = mockUserService.findUser("Bob", 25);

    // Проверка
    assertEquals(expectedUser, result);
}
```

Вне зависимости от передаваемых в findUser аргументов, всегда будет возвращаться
expectedUser.

Теперь, если нам нужно определить поведение метода для конкретных аргументов:

```java

@Test
public void whenUsingEqMatcher_thenSpecificUserIsReturned() {
    UserService mockUserService = Mockito.mock(UserService.class);
    User specificUser = new User("Bob", 25);

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
```

Здесь findUser возвращает specificUser только если вызывается с аргументами
"Bob" и 25.

ArgumentMatchers также полезны при верификации вызовов методов:

```java

@Test
public void verifyWithArgumentMatchers() {
    // Создание мока
    UserService mockUserService = Mockito.mock(UserService.class);

    // Вызов метода
    mockUserService.findUser("Alice", 30);

    // Верификация: был ли метод вызван с определенными аргументами
    Mockito
            .verify(mockUserService)
            .findUser(Mockito.eq("Alice"), Mockito.eq(30));
}
```

## Спай-объекты (Spying) в Mockito

[spying_demo](../mockito-demo/src/main/java/dev/folomkin/mockito/spying_demo)

Спай-объекты - это специальные моки, которые в основном ведут себя как обычные
объекты, но позволяют переопределять поведение отдельных методов. Они
мегаполезны, когда вам нужно тестировать реальный объект, но при этом изменить
поведение некоторых его частей.

Допустим, у нас есть класс `ListManager`, который мы хотим протестировать:

```java
public class ListManager {
    public List<String> createList() {
        return new ArrayList<>();
    }

    public int getListSize(List<String> list) {
        return list.size();
    }
}

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
```

Мы создали спай ListManager, который использует реальный метод createList.
Однако, мы переопределили поведение метода getListSize, чтобы он возвращал 100,
независимо от реального размера списка.

Также, как и с моками, мы можем использовать верификацию с спай-объектами:

```java

@Test
public void verifySpy() {
    // Создание спай-объекта
    ListManager spyListManager = Mockito.spy(new ListManager());

    // Вызов метода
    List<String> list = spyListManager.createList();
    list.add("Item");
    spyListManager.getListSize(list);

    // Верификация вызова метода
    Mockito.verify(spyListManager).getListSize(list);
}
```

Здесь мы проверяем, что метод getListSize был действительно вызван на
спай-объекте.

## Тестирование с использованием аннотаций

(mock_annotation_demo]

### @Mock

Аннотация @Mock используется для создания мок-объектов. Вместо явного вызова
Mockito.mock(Class), вы можете аннотировать поля вашего тестового класса.

Рассмотрим очередной пример с интерфейсом DataService:

```java
public interface DataService {
    double retrieveData();
}

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
                .thenReturn(5.0);

        assertEquals(5.0, mockDataService.retrieveData());
    }
}
```

### @InjectMocks

Автоматически вставляет мок-объекты в тестируемый объект. Это удобно, когда у
вас есть сложные зависимости.

Предположим, у нас есть класс DataProcessor, который зависит от DataService:

```java
public class DataProcessor {
    private DataService dataService;

    public DataProcessor(DataService dataService) {
        this.dataService = dataService;
    }

    public int process() {
        return dataService.retrieveData() * 2;
    }
}
```

Мы можем использовать @InjectMocks для автоматического внедрения мока
DataService в DataProcessor:

```java
public class DataProcessorTest {

    @Mock
    DataService mockDataService;

    @InjectMocks
    DataProcessor dataProcessor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcess() {
        Mockito.when(mockDataService.retrieveData()).thenReturn(5);
        assertEquals(10, dataProcessor.process());
    }
}
```

### @Spy

Позволяет создать спай-объекты, которые частично мокируют реальные объекты.

Допустим, у нас есть класс ListManager:

```java
public class ListManager {
    public int getListSize(List<String> list) {
        return list.size();
    }
}
```

Мы можем использовать @Spy для тестирования этого класса:

```java
public class ListManagerTest {

    @Spy
    ListManager spyListManager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetListSize() {
        List<String> list = new ArrayList<>(Arrays.asList("item1", "item2"));
        Mockito.doReturn(100).when(spyListManager).getListSize(list);
        assertEquals(100, spyListManager.getListSize(list));
    }
}
```

## @RunWith(MockitoJUnitRunner.class)

Это аннотация JUnit, которая указывает, что тесты в классе должны запускаться с
помощью специального "раннера" - MockitoJUnitRunner. Этот раннер инициализирует
поля, аннотированные как @Mock, @Spy, @InjectMocks, автоматически, без
необходимости вызывать MockitoAnnotations.initMocks(this) в каждом тестовом
классе.

Допустим, у нас есть следующие классы:

```java
public interface DataService {
    int retrieveData();
}

public class DataProcessor {
    private DataService dataService;

    public DataProcessor(DataService dataService) {
        this.dataService = dataService;
    }

    public int process() {
        return dataService.retrieveData() * 2;
    }
}
```

Наша цель - протестировать DataProcessor, используя мок DataService.

Для этого мы используем аннотацию @RunWith(MockitoJUnitRunner.class):

```java
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataProcessorTest {

    @Mock
    DataService mockDataService;

    @Test
    public void testProcess() {
        when(mockDataService.retrieveData()).thenReturn(5);

        DataProcessor dataProcessor = new DataProcessor(mockDataService);

        assertEquals(10, dataProcessor.process());
    }
}
```

Здесь MockitoJUnitRunner автоматически инициализирует mockDataService. Вам не
нужно вызывать MockitoAnnotations.initMocks(this) в методе @Before.

## Mockito и Spring Boot

В Spring Boot, Mockito используется для создания моков или спай-объектов для
Spring Beans. Это позволяет имитировать поведение этих бинов, таким образом,
чтобы тестировать взаимодействие различных слоев приложения (например,
контроллеров, сервисов и репозиториев) без необходимости взаимодействовать с
реальными внешними системами, такими как базы данных.

Допустим, у нас есть следующий сервис в приложении Spring Boot:

```java

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
```

И соответствующий репозиторий:

```java
public interface UserRepository extends JpaRepository<User, Long> {
    // Базовые методы JpaRepository
}
```

Нам нужно протестировать метод getUserById в UserService, не обращаясь к
реальной бд.

Используем Spring Boot Test и Mockito для создания моков:

```java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void whenGetUserById_thenUserReturned() {
        Long userId = 1L;
        User mockUser = new User(userId, "Test User");
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(mockUser));

        User result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(mockUser, result);
    }
}
```

В этом тесте:

- @ExtendWith(SpringExtension.class): Интегрирует тест с Spring TestContext
  Framework.
- @SpringBootTest: Говорит Spring Boot загрузить контекст приложения, чтобы
  тесты могли быть интеграционными.
- @Mock: Создает мок-объект для UserRepository.
- @InjectMocks: Автоматически вставляет мок userRepository в userService.
- Mockito - это не просто инструмент для мокирования, он повышает качества кода
  и упрощает тестирование.

Не за горой новогодние праздники, в связи с этим хочу порекомендовать вам
бесплатный урок, где вы создадите интерактивную Новогоднюю открытку -
графическое приложение с использованием Java и LibGDX, с нуля. В ходе вебинара
вы сможете предлагать идеи, которые хотелось бы реализовать в проекте.

