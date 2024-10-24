# Docker Compose и Testcontainers в Spring Boot 3.1

Одними из наиболее значимых нововведений в недавнем релизе Spring Boot 3.1 на
мой взгляд являются поддержка Docker Compose и Testcontainers, а так же новая
концепция подключений к сервисам, которая позволяет с минимальным количеством
кода подключаться к сервисам, развёрнутым в контейнерах.

## Подключения к сервисам

В Spring Boot до версии 3.1 настройки подключений к сервисам описывались
классами, отмеченными аннотациями @ConfigurationProperties, значения свойств
которых загружались из файлов свойств.

Теперь же для указания основных настроек подключений к сервисам, хоть и не всем,
используются классы, реализующие интерфейс ConnectionDetails и его наследников (
JdbcConnectionDetails, MongoConnectionDetails и т.д.). Так, например, для
указания настроек подключения к реляционным базам данных теперь используются
реализации интерфейса JdbcConnectionDetails.

Если же в контексте приложения нет зарегистрированных компонентов
ConnectionDetails с настройками подключения к какому-нибудь сервису, то Spring
Boot попытается создать соответствующий компонент на основе значений из файлов
свойств, как это было и раньше. Например, если в контексте приложения
отсутствует компонент с настройками подключения к реляционной базе данных,
Spring Boot попытается зарегистрировать компонент
PropertiesJdbcConnectionDetails. То есть с точки зрения разработчика приложения
на Spring Boot кардинально ничего не изменилось.

Стоит отметить, что ConnectionDetails содержит только основные параметры для
подключения к сервисам: ip-адрес, имя хоста, порт, имя пользователя, пароль и
т.д. Различные специфичные или не столь важные параметры, например параметры
источника данных HikariCP, будут по-прежнему браться из файлов свойств.

Так же нужно упомянуть факт того, что список поддерживаемых сервисов сейчас
невелик, но он, скорее всего, будет увеличиваться. В настоящее время этот список
выглядит следующим образом:

- Apache Cassandra
- Couchbase
- Elasticsearch
- Flyway
- JDBC-совместимые СУБД
- Apache Kafka
- Liquibase
- MongoDB
- Neo4j
- R2DBC-совместимые СУБД
- RabbitMQ
- Redis
- Zipkin

## Интеграция с Docker Compose

Теперь Spring Boot может при запуске приложения автоматически запускать
необходимые контейнеры, описанные в файле Docker Compose.

Чтобы использовать интеграцию с Docker Compose потребуется зависимость
spring-boot-docker-compose:

```xml

<project>
    <!-- -->
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-docker-compose</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>
    <!-- -->
</project>
```

Если вы хотите использовать интеграцию с Docker Compose не только в процессе
разработки, но и в процессе реальной эксплуатации сервиса, то необходимо
выключить исключение этого модуля при сборке проекта в настройках плагина Spring
Boot:

```xml

<project>
    <!-- -->
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-docker-compose</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>
    <!-- -->
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>3.1.0</version>
                <configuration>
                    <excludeDockerCompose>false</excludeDockerCompose>
                </configuration>
            </plugin>
        </plugins>
    </build>
    <!-- -->
</project>
```

Настраивать интеграцию с Docker Compose можно следующими свойствами:

```yaml

spring:
  docker:
    compose:
      # Включение поддержки Docker Compose
      enabled: true
      # Специфичный файл Docker Compose
      file: compose.yaml
      # Управление жизненным циклом контейнеров
      lifecycle-management: start_and_stop
      # Имя или ip-адрес хоста, на котором запускаются контейнеры
      host: localhost
      # Параметры запуска контейнеров
      start:
        # Уровень логгирования
        log-level: info
        # Команда запуска
        command: up
      # Параметры остановки контейнеров
      stop:
        # Время ожидания остановки контейнеров
        timeout: 10s
        # Команда остановки
        command: stop
      # Профили Docker Compose
      profiles:
        # Активные профили
        active: [ ]
      # Не использовать Docker Compose
      skip:
        # В тестах
        in-tests: true
      # Проверки доступности контейнера
      readiness:
        # По TCP
        tcp:
          # Таймаут подключения
          connect-timeout: 200ms
          # Таймаут чтения
          read-timeout: 200ms
        # Таймаут
        timeout: 2m
        # Ожидание
        wait: always
```

Кроме того, Spring Boot может автоматически сконфигурировать подключения к
некоторым сервисам на основе данных, полученных от запущенных контейнеров,
благодаря реализации новой концепции подключений к сервисам.

Файлы Docker Compose
По умолчанию Spring Boot ищет в директории приложения следующие файлы:

- compose.yml
- compose.yaml
- docker-compose.yml
- docker-compose.yaml
- Впрочем, вы можете самостоятельно указать используемый файл при помощи
  свойства
- spring.docker.compose.file, указанного выше.

## Управление жизненным циклом

Настраивать управление жизненным циклом контейнеров можно при помощи свойства
`spring.docker.compose.lifecycle-management`, которое может иметь следующие
значения:

- start_and_stop - запуск контейнеров при запуске приложения и остановка - при
  остановке приложения, по умолчанию
- start_only - запуск контейнеров при запуске приложения, контейнеры продолжат
  работать после остановки приложения
- none - не использовать управление жизненным циклом контейнеров, этот вариант
  предполагает ручное управление Docker Compose

Так же можно настраивать процесс запуска и остановки контейнеров. При помощи
свойства `spring.docker.compose.start.command` можно задать команду, при помощи
которой будут запускаться контейнеры, это может быть start или up (по
умолчанию). При помощи свойства spring.docker.compose.stop.command можно указать
команду для остановки контейнеров: down или stop (по умолчанию). Так же можно
указать время ожидания запуска и остановки контейнеров при помощи свойств
spring.docker.compose.start.timeout и spring.docker.compose.stop.timeout
соответственно.

Ожидание доступности контейнеров
На запуск контейнера в Docker может потребоваться какое-то время. И чтобы при
запуске разрабатываемого приложения не возникло ошибок нужно дождаться
готовности запускаемых контейнеров.

Параметры проверок доступности сервисов обычно задаются в свойстве healthchecks
контейнера в compose.yaml. Кроме этого Spring Boot может проверять доступность
сервиса напрямую, обращаясь к нему по TCP. По умолчанию контейнер считается
доступным, когда TCP/IP-соединение на целевой порт может быть установлено. Это
поведение можно отключить для контейнера при помощи метки
`org.springframework.boot.readiness-check.tcp.disable: true`:

```yaml
services:
  db:
    image: postgres:15
    ports:
      - 5432
    labels:
      # Отключение проверки доступности по TCP
      org.springframework.boot.readiness-check.tcp.disable: true
```

Так же при помощи свойств Spring Boot, описанных выше, можно задавать свои
таймауты проверок доступности:

```yaml
spring:
  docker:
    compose:
      # Проверки доступности контейнера
      readiness:
        # По TCP
        tcp:
          # Таймаут подключения
          connect-timeout: 200ms
          # Таймаут чтения
          read-timeout: 200ms
        # Таймаут
        timeout: 2m
        # Ожидание
        wait: always
```

При помощи параметра spring.docker.compose.readiness.wait можно указывать
вариант ожидания доступности контейнера:

- always - всегда ожидать доступности, по умолчанию
- never - никогда не ожидать доступности
- only_if_started - ожидать, если жизненным циклом контейнера управляет Spring
  Boot

## Собственные образы поддерживаемых сервисов

Подключение к сервису, развёрнутому в контейнере, будет автоматически
сконфигурировано только в том случае, если контейнер использует оригинальный
образ сервиса из списка, указанного выше. Если вы используете ваш собственный
образ, основанный на оригинальном, то вы можете подсказать Spring Boot, как
настраивать подключение при помощи метки
org.springframework.boot.service-connection. Например, в одном из моих проектов
используется модифицированный образ СУБД PostgreSQL, и чтобы Spring Boot смог
работать с этим образом, как с оригинальным, достаточно сделать следующее:

```yaml
services:
  db:
    image: my_own_postgres:15
    labels:
      # Настройки подключения как у PostgreSQL
      org.springframework.boot.service-connection: postgres
    ports:
      - 5432
```

Список всех поддерживаемых значений метки
org.springframework.boot.service-connection:

- cassandra
- elasticsearch
- mariadb
- mongo
- mysql
- gvenzl/oracle-xe
- rabbitmq
- redis
- mssql/server
- openzipkin/zipkin

## Игнорирование подключений к сервисам

Если вы не хотите, чтобы Spring Boot автоматически создавал подключение к
сервису, запущенному в контейнере, то вы можете для этого использовать метку
org.springframework.boot.ignore: true:

```yaml
services:
  db:
    image: postgres:15
    labels:
      # Подключение будет проигнорировано
      org.springframework.boot.ignore: true
```

## Активация профилей Docker Compose

Для запуска разного набора контейнеров в Docker Compose в зависимости от
окружения можно использовать профили. Список активных профилей Docker Compose
можно задать в свойстве spring.docker.compose.profile.active:

```yaml
services:
  db_pg15:
    image: postgres:15
    profiles:
      - pg15
    ports:
      - 5432
  db_pg14:
    image: postgres:14
    profiles:
      - pg14
    ports:
      - 5432
```

Для запуска Docker Compose с профилем pg14:

```yaml
spring:
  docker:
    compose:
      profiles:
        active: pg14

```

Интеграция с Testcontainers
Начиная с версии 3.1 Spring Boot BOM включает Testcontainers, версию которого
при желании можно специфицировать при помощи свойства testcontainers.version.

Для использования Testcontainers в тестах со Spring Boot потребуются как минимум
следующие зависимости:

```xml

<project>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>junit-jupiter</artifactId>
            <scope>test</scope>
        </dependency>
        <!-- Зависимость для работы с контейнерами PostgreSQL -->
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>postgresql</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

Теперь можно создавать контейнеры в тестах:

```java
@SpringBootTest
@Testcontainers
class SandboxTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15");

    @Test
    void test() {
        // тест
    }
}
```

Аннотация @Testcontainers добавляет расширение Testcontainers для JUnit, которое
автоматически запускает и останавливает контейнеры в тестах.

При помощи аннотации @Container вы можете отметить свойства, описывающие
контейнеры, жизненным циклом которых должен управлять фреймворк Testcontainers.

Контейнер, описываемый статическим свойством тестового класса, будет запускаться
один раз и использоваться при выполнении всех тестов в классе. Контейнер,
описываемый свойством объекта, будет запускаться для каждого теста заново.

## Подключения к сервисам

Контейнеры, управляемые Testcontainers, могут использоваться в качестве
источников параметров для подключений к сервисам. Для этого вам потребуется
зависимость spring-boot-testcontainers:

```xml
<project>
    <!-- Прочие настройки -->
    <dependencies>
        <!-- Прочие зависимости -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-testcontainers</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

Теперь можно автоматически создать подключение к сервису, развёрнутому в
контейнере при помощи аннотации @ServiceConnection:

```java
@SpringBootTest
@Testcontainers
class SandboxTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15");

    @Test
    void test() {
        // тест
    }
}
```

## Динамические источники свойств

Контейнеры, развёрнутые при помощи Testcontainers можно использовать в качестве
динамических источников свойств. Для добавления свойств в тесте необходимо
создать статический метод, отмеченный аннотацией @DynamicPropertySource и
принимающий аргумент типа DynamicPropertyRegistry:

```java
@SpringBootTest
@Testcontainers
class SandboxTest {

    @Autowired
    ApplicationContext applicationContext;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15");

    @DynamicPropertySource
    static void postgreSqlProperties(DynamicPropertyRegistry registry) {
        registry.add("postgres.driver",
                postgreSQLContainer::getDriverClassName);
    }

    @Test
    void test() {
        assertEquals("org.postgresql.Driver",
                applicationContext.getEnvironment().getProperty("postgres.driver"));
    }
}
```

## Использование Testcontainers для ручного тестирования

Иногда в процессе разработки возникают ситуации, когда нужно запустить сервис и
вручную проверить работу ту или иной функции. Обычно для этого нужно
развёртывать и настраивать какое-то окружение - те же базы данных.

Интеграция Spring Boot и Testcontainers позволяет упростить этот процесс,
автоматизируя развёртывание требуемого окружения в контейнерах. В отличие от
интеграции Spring Boot с Docker Compose Testcontainers по умолчанию удаляет
контейнеры после остановки приложения.

Чтобы использовать Testcontainers для ручного тестирования, нужно создать
стартовый класс в директории с исходными кодами тестов, а так же тестовый
класс-конфигурацию:

```java
@TestConfiguration(proxyBeanMethods = false)
public class SandboxConfiguration {

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgreSQLContainer(
            DynamicPropertyRegistry registry) {
        var container = new PostgreSQLContainer<>("postgres:15");
        // Да, здесь мы тоже можем динамически изменять свойства
        registry.add("postgres.driver",
                container::getDriverClassName);

        return container;
    }
}
```

Обратите внимание на аннотацию @TestConfiguration, класс отмеченные этой
аннотацией не используются в качестве классов конфигурации автоматически - их
нужно указывать вручную в нужных тестах.

Допустим, главный стартовый класс приложения -
pro.akosarev.sandbox.SandboxApplication, тогда можно создать стартовый класс для
тестирования pro.akosarev.sandbox.TestSandboxApplication со следующим методом
main:

```java
public class TestSandboxApplication {

    public static void main(String[] args) {
        SpringApplication.from(SandboxApplication::main)
                // использовать конфигурацию для тестов
                .with(SandboxConfiguration.class)
                .run(args);
    }
}
```

Теперь можно запустить приложение через main-метода класса
TestSandboxApplication. В Maven это можно сделать при помощи команды mvn
spring-boot:test-run.