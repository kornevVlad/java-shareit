# Шеринг вещей
В приложении реализовано создание пользователя, предмета. Пользователь может брать предмет в аренду после подтверждения собственника предмета. Реализованы коментарии для пользователей которые брали в аренду предмет.  
Проект реализован на JAVA.
```
@SpringBootApplication
public class ShareItServer {

	public static void main(String[] args) {
		SpringApplication.run(ShareItServer.class, args);
	}
}
```

## Функционал
### User
**[POST] createUser** //создание   
**[PATCH] updateUser** //обновление   
**[GET] getUsers** //получение списка  
**[GET] getUsersById** //получение по ID  
**[DELETE] deleteUser** //удаление  

### Item
**[POST] createItem** //создание предмета    
**[PATCH] updateItem** //обновление    
**[GET] getItemById** //получение по ID    
**[GET] getItems** //список премдетов  
**[GET] getItemsBySearchQuery** //список предметов совпадение запроса по тексту    
**[POST] addComment** //создание комментария    

### Booking
**[POST] createBooking** //создание бронирования  
**[PATCH] updateBooking** //обновление бронирования  
**[GET] getBooking** //вывод бронирования по ID  
**[GET] getBookings** //список бронирований  
**[GET] getBookingsByBookerItems** //список бронирования пользователя с пагинацией 

### ItemRequest
**[POST] createItemRequest**  //создание запроса аренды      
**[GET] getItemRequestsByRequestorId** //список запросов пользователя    
**[GET] getItemRequestsById** //данные о запросе  
**[GET] getAllItemRequests** //список запросов с пагинацией  

## Стек
Java 11, Docker, Hibernate, Spring Framework, Maven, Lombok, Mockito, БД PostgreSql

## Развертываение
1. Установить JDK amazon coretto [скачать](https://corretto.aws/downloads/latest/amazon-corretto-11-x64-windows-jdk.msi)
2. Установить PostgreSql [скачать](https://www.enterprisedb.com/downloads/postgres-postgresql-downloads)
3. Подготовить БД к работе: создать БД share, создать пользователя share и дать доступ к БД. 
```spring.datasource.driverClassName=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/share
spring.datasource.username=share
spring.datasource.password=share
```
4. Установить Docker [скачать для Windows Pro](https://www.docker.com/products/docker-desktop)
5. Проект готов к запуску!!!

