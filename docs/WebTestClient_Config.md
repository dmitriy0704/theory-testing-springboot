# Config

WebTestClient – это HTTP-клиент, предназначенный для тестирования серверных
приложений. Он оборачивает WebClient из Spring и использует его для выполнения
запросов, но открывает фасад тестирования для проверки ответов. WebTestClient
можно использовать для выполнения сквозных HTTP-тестов. Его также можно
использовать для тестирования приложений Spring MVC и Spring WebFlux без
работающего сервера с помощью имитации объектов-запросов и объектов-ответов
сервера.

## Настройка

Чтобы настроить WebTestClient, необходимо выбрать серверную настройку для
привязки. Это может быть один из нескольких параметров настройки имитируемого
сервера или подключение к реальному серверу.

## Привязка к контроллеру

Такая настройка позволяет тестировать конкретный контроллер(ы) с помощью
имитируемых объектов-запросов и объектов-ответов без работающего сервера.

Для приложений WebFlux используйте следующую конфигурацию, которая загружает
инфраструктуру, эквивалентную Java-конфигурации WebFlux, регистрирует заданный
контроллер(ы) и создает цепочку WebHandler для обработки запросов:

```java
WebTestClient client =
        WebTestClient.bindToController(new TestController()).build();
```

Для Spring MVC используйте следующую конфигурацию, которая делегирует
StandaloneMockMvcBuilder полномочия на загрузку инфраструктуры, эквивалентной
Java-конфигурации WebMvc, регистрирует заданный контроллер(ы) и создает
экземпляр MockMvc для обработки запросов:

```java
WebTestClient client =
        MockMvcWebTestClient.bindToController(new TestController()).build();
```

## Привязка к ApplicationContext

Эта настройка позволяет загрузить конфигурацию Spring с инфраструктурой из
Spring MVC или Spring WebFlux и объявлениями контроллеров, а также использовать
ее для обработки запросов с помощью имитируемых объектов-запросов и
объектов-ответов без работающего сервера.

Для WebFlux используйте следующую конфигурацию, где ApplicationContext из Spring
передается WebHttpHandlerBuilder, чтобы создать цепочку WebHandler для обработки
запросов:

```java

@SpringJUnitConfig(WebConfig.class)
class MyTests {
    WebTestClient client;

    @BeforeEach
    void setUp(ApplicationContext context) {
        client = WebTestClient.bindToApplicationContext(context).build();
    }
}
```

- Укажите конфигурацию для загрузки
- Внедрите конфигурацию
- Создайте WebTestClient

Для Spring MVC используйте следующую конфигурацию, где ApplicationContext из
Spring передается в MockMvcBuilders.webAppContextSetup, чтобы создать экземпляр
MockMvc для обработки запросов:

```java

@ExtendWith(SpringExtension.class)
@WebAppConfiguration("classpath:META-INF/web-resources")
@ContextHierarchy({
        @ContextConfiguration(classes = RootConfig.class),
        @ContextConfiguration(classes = WebConfig.class)
})
class MyTests {
    @Autowired
    WebApplicationContext wac;
    WebTestClient client;

    @BeforeEach
    void setUp() {
        client = MockMvcWebTestClient.bindToApplicationContext(this.wac).build();
    }
}
```

- Укажите конфигурацию для загрузки
- Внедрите конфигурацию
- Создайте WebTestClient


## Привязка к функции маршрутизатора
Такая настройка позволяет тестировать функциональные конечные точки с помощью имитируемых объектов-запросов и объектов-ответов без работающего сервера.

Для WebFlux используйте следующую конфигурацию, которая делегирует RouterFunctions.toWebHandler полномочия на создание серверной настройки для обработки запросов:


```java
RouterFunction<?> route = ...
client = WebTestClient.bindToRouterFunction(route).build();
```

Для Spring MVC в настоящее время отсутствуют опции тестирования функциональных конечных точек WebMvc.

## Привязка к серверу
Эта настройка подключается к работающему серверу для выполнения полных сквозных HTTP-тестов:


## Конфигурация клиента
В дополнение к параметрам настройки сервера, описанным ранее, вы также можете настроить параметры клиента, включая базовый URL-адрес, заголовки по умолчанию, фильтры клиента и другое. Доступ к этим опциям легко можно получить после выполнения bindToServer(). Для всех остальных вариантов конфигурации необходимо использовать configureClient(), чтобы перейти от конфигурации сервера к конфигурации клиента, как это показано ниже:

```java
client = WebTestClient.bindToController(new TestController())
        .configureClient()
        .baseUrl("/test")
        .build();

```
