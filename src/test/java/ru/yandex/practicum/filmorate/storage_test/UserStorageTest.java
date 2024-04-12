package ru.yandex.practicum.filmorate.storage_test;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserStorageTest {

    private UserStorage userStorage;
    private final JdbcTemplate jdbcTemplate;

    private final LocalDate date = LocalDate.parse("1990-01-01");

    @BeforeEach
    public void storageInit() {
        userStorage = new UserDbStorage(jdbcTemplate);
    }

    @Test
    public void addAndFindUserByIdTest() {

        // Создаю пользователя
//        User user = User.builder().email("jack@mail.ru").login("jack").name("Jack").birthday(date)
//                .likedFilmsId(new ArrayList<>()).friendsId(new HashSet<>()).build();
        User user = User.builder().email("jack@mail.ru").login("jack").name("Jack").birthday(date).build();

        // Добавляю пользователя в БД
        userStorage.addUser(user);

        // Получаю пользователя из БД по id
        User addedUser = userStorage.getUserById(user.getId());

        // Проверяю полученные из БД данные
        assertThat(addedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    public void getAllUsersAndUpdateUserTest() {

        // Создаю пользователя
//        User user = User.builder().email("jack@mail.ru").login("jack").name("Jack").birthday(date)
//                .likedFilmsId(new ArrayList<>()).friendsId(new HashSet<>()).build();
        User user = User.builder().email("jack@mail.ru").login("jack").name("Jack").birthday(date).build();

        // Добавляю пользователя в БД
        User addedUser = userStorage.addUser(user);

        // Получаю список пользователей из БД
        List<User> users1 = userStorage.getUsers();

        // Список содержит 1 элемент. Элемент users1.get(0) == addedUser
        assertEquals(1, users1.size());
        assertEquals(users1.get(0), addedUser);

        // Обновляю пользователя
        addedUser.setName("newName");
        addedUser.setEmail("newEmail");
        userStorage.updateUser(addedUser);

        // Получаю список с обновленным пользователем из БД
        List<User> users2 = userStorage.getUsers();

        // Список по-прежнему содержит 1 элемент. Элемент users2.get(0) == addedUser
        assertEquals(1, users2.size());
        assertEquals(users2.get(0), addedUser);
    }

    @Test
    public void addGetDeleteUserFriendTest() {

        // Создаю пользователей
//        User user = User.builder().email("jack@mail.ru").login("jack").name("Jack").birthday(date)
//                .likedFilmsId(new ArrayList<>()).friendsId(new HashSet<>()).build();
//        User userFriend1 = User.builder().email("john@mail.ru").login("john").name("John").birthday(date)
//                .likedFilmsId(new ArrayList<>()).friendsId(new HashSet<>()).build();
//        User userFriend2 = User.builder().email("Helen@mail.ru").login("helen").name("Helen").birthday(date)
//                .likedFilmsId(new ArrayList<>()).friendsId(new HashSet<>()).build();
        User user = User.builder().email("jack@mail.ru").login("jack").name("Jack").birthday(date).build();
        User userFriend1 = User.builder().email("john@mail.ru").login("john").name("John").birthday(date).build();
        User userFriend2 = User.builder().email("Helen@mail.ru").login("helen").name("Helen").birthday(date).build();

        // Добавляю пользователей в БД
        User addedUser = userStorage.addUser(user);
        User addedUserFriend1 = userStorage.addUser(userFriend1);
        User addedUserFriend2 = userStorage.addUser(userFriend2);

        // Получаю список друзей пользователя из БД и проверяю, что он пуст
        List<User> usersFriends = userStorage.getUserFriends(addedUser.getId());
        assertEquals(0, usersFriends.size());

        // Добавляю пользователю двух друзей
        userStorage.addFriend(addedUser.getId(), addedUserFriend1.getId());
        userStorage.addFriend(addedUser.getId(), addedUserFriend2.getId());

        // Получаю пользователя и проверяю, что в списке его друзей два человека с конкретными id
//        addedUser = userStorage.getUserById(addedUser.getId());
//        Set<Integer> friendsId = addedUser.getFriendsId();
//        assertEquals(2, friendsId.size());
//        assertTrue(friendsId.contains(addedUserFriend1.getId()));
//        assertTrue(friendsId.contains(addedUserFriend2.getId()));

        // Получаю список друзей пользователя из БД и проверяю, что в нем два друга
        List<User> newUsersFriends = userStorage.getUserFriends(addedUser.getId());
        assertEquals(2, newUsersFriends.size());
        assertTrue(newUsersFriends.contains(addedUserFriend1));
        assertTrue(newUsersFriends.contains(addedUserFriend2));
    }

    @Test
    public void getCommonUserFriendsTest() {
        // Создаю пользователей
//        User user1 = User.builder().email("jack@mail.ru").login("jack").name("Jack").birthday(date)
//                .likedFilmsId(new ArrayList<>()).friendsId(new HashSet<>()).build();
//        User user2 = User.builder().email("john@mail.ru").login("john").name("John").birthday(date)
//                .likedFilmsId(new ArrayList<>()).friendsId(new HashSet<>()).build();
//        User user3 = User.builder().email("Helen@mail.ru").login("helen").name("Helen").birthday(date)
//                .likedFilmsId(new ArrayList<>()).friendsId(new HashSet<>()).build();
        User user1 = User.builder().email("jack@mail.ru").login("jack").name("Jack").birthday(date).build();
        User user2 = User.builder().email("john@mail.ru").login("john").name("John").birthday(date).build();
        User user3 = User.builder().email("Helen@mail.ru").login("helen").name("Helen").birthday(date).build();

        // Добавляю пользователей в БД
        User addedUser1 = userStorage.addUser(user1);
        User addedUser2 = userStorage.addUser(user2);
        User addedUser3 = userStorage.addUser(user3);

        // Добавляю пользователю user1 двух друзей user2 и user3
        userStorage.addFriend(addedUser1.getId(), addedUser2.getId());
        userStorage.addFriend(addedUser1.getId(), addedUser3.getId());

        // Добавляю пользователю user3 двух друзей user1 и user2
        userStorage.addFriend(addedUser3.getId(), addedUser1.getId());
        userStorage.addFriend(addedUser3.getId(), addedUser2.getId());

        // Получаю список общих друзей для user1 и user2 и проверяю, что список пуст
        List<User> commonFriends = userStorage.getCommonFriendsList(addedUser1.getId(), addedUser2.getId());
        assertTrue(commonFriends.isEmpty());

        // Получаю список общих друзей для user1 и user3 и проверяю, что список содержит одного друга (user2)
        commonFriends = userStorage.getCommonFriendsList(addedUser1.getId(), addedUser3.getId());
        assertEquals(1, commonFriends.size());
        assertTrue(commonFriends.contains(addedUser2));
    }
}
