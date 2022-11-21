# search-engine

# Поисковой движок по сайту, обходит страницы сайта, создает поисковые индексы.

  Простой веб-интерфейс и API, через который можно управлять и получать результаты поисковой выдачи по запросу.
  
  Поисковый движок представляет из себя Spring-приложение Spring-boot 2.5.5
  
# Инструкция для запуска
  
  1. Установить MySQL-сервер, если он ещё не установлен, создать пустую базу данных search_engine c кодировкой utf8mb4.
  2. Создать пользователя для подключения к базе данных root.
  3. Установить пароль к базе данных в конфигурационном фйле приложения "application.properties".
  4. При первом запуске, установить в конфигурационном фйле приложения "application.properties" => "spring.jpa.hibernate.ddl-auto=create".
  5. В конфигурационном файле "application.yaml" задать адреса и имена сайтов, по которым движок должен осуществлять поиск.
  6. В конфигурационном файле "application.yaml" задать логин и пароль для авторизации admin в веб интефейсе.

   