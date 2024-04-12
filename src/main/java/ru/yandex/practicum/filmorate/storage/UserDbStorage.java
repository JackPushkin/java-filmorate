package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.UserAlreadyRegisteredException;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Repository
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id_user");
    }

    @Override
    public User addUser(User user) {
        Map<String, Object> parameters = Map.of(
                "email", user.getEmail(),
                "login", user.getLogin(),
                "name", user.getName(),
                "birthday", user.getBirthday());
        try {
            user.setId((Integer) simpleJdbcInsert.executeAndReturnKey(parameters));
        } catch (DuplicateKeyException e) {
            throw new UserAlreadyRegisteredException("User with such email already registered.");
        }
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sqlSelectQuery = "SELECT * FROM users WHERE id_user = ?";
        String sqlQuery = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id_user = ?";

        User selectedUser;

        try {
            String email = user.getEmail();
            String login = user.getLogin();
            String name = user.getName();
            LocalDate birthday = user.getBirthday();

            selectedUser = jdbcTemplate.queryForObject(sqlSelectQuery, (rs, rowNum) -> makeUser(rs), user.getId());

            if (email != null) selectedUser.setEmail(email);
            if (login != null) selectedUser.setLogin(login);
            if (name != null) selectedUser.setName(name);
            if (birthday != null) selectedUser.setBirthday(birthday);

            int count = jdbcTemplate.update(sqlQuery, selectedUser.getEmail(), selectedUser.getLogin(), selectedUser.getName(),
                    selectedUser.getBirthday(), selectedUser.getId());
            if (count == 0) {
                throw new NotFoundException(String.format("User with id=%d not found", user.getId()));
            }
        } catch (DuplicateKeyException e) {
            throw new UserAlreadyRegisteredException("User with such email already registered.");
        } catch (DataAccessException e) {
            throw new NotFoundException(String.format("User with id=%d not found", user.getId()));
        }
        return selectedUser;
    }

    @Override
    public List<User> getUsers() {
        String sqlQuery = "SELECT * FROM users";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User getUserById(Integer userId) {
        String sqlQuery = "SELECT * FROM users WHERE id_user = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> makeUser(rs), userId);
        } catch (DataAccessException e) {
            throw new NotFoundException(String.format("User with id=%d not found", userId));
        }
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        String sqlInsertQuery = "INSERT INTO friends (id_user, id_friend, friendship_status) VALUES (?, ?, FALSE)";
        String sqlUpdateQuery = "UPDATE friends SET friendship_status = TRUE WHERE id_user = ? AND id_friend = ?";

        int count = jdbcTemplate.update(sqlUpdateQuery, friendId, userId);

        if (count == 0) {
            try {
                jdbcTemplate.update(sqlInsertQuery, userId, friendId);
            } catch (DataAccessException e) {
                throw new NotFoundException("User with such id does not exist");
            }
        }
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        String sqlSelectQuery = "SELECT COUNT(*) FROM users WHERE id_user = ?";
        String sqlDeleteQuery = "DELETE FROM friends WHERE id_user = ? AND id_friend = ?";

        int count1 = jdbcTemplate.queryForObject(sqlSelectQuery, Integer.class, userId);
        int count2 = jdbcTemplate.queryForObject(sqlSelectQuery, Integer.class, friendId);

        if (count1 == 0) {
            throw new NotFoundException(String.format("User with id=%d does not exist", userId));
        } else if (count2 == 0) {
            throw new NotFoundException(String.format("User with id=%d does not exist", friendId));
        }

        jdbcTemplate.update(sqlDeleteQuery, userId, friendId);
    }

    @Override
    public Set<User> getUserFriends(Integer userId) {
        String sqlSelectQuery = "SELECT COUNT(*) FROM users WHERE id_user = ?";
        String sqlQuery = "SELECT * FROM users u WHERE u.id_user " +
                          "IN (SELECT f.id_friend FROM friends f WHERE f.id_user = ?) OR u.id_user " +
                          "IN (SELECT f.id_user FROM friends f WHERE f.id_friend = ? AND friendship_status = TRUE)";

        int count = jdbcTemplate.queryForObject(sqlSelectQuery, Integer.class, userId);

        if (count == 0) {
            throw new NotFoundException(String.format("User with id=%d does not exist", userId));
        }
        return new HashSet<>(jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeUser(rs), userId, userId));
    }

    @Override
    public Set<User> getCommonFriendsList(Integer userId, Integer otherId) {
        Set<User> userFriendList = getUserFriends(userId);
        userFriendList.retainAll(getUserFriends(otherId));
        return userFriendList;
    }

    private Set<Integer> getUserFriendsIdList(Integer userId) {
        String sqlQuery1 = "SELECT id_friend FROM friends WHERE id_user = ?";
        String sqlQuery2 = "SELECT id_user FROM friends WHERE id_friend = ? AND friendship_status = ?";

        Set<Integer> friendsIdList = new HashSet<>();

        friendsIdList.addAll(jdbcTemplate.query(sqlQuery1, (rs, rowNum) -> rs.getInt("id_friend"), userId));
        friendsIdList.addAll(jdbcTemplate.query(sqlQuery2, (rs, rowNum) -> rs.getInt("id_user"), userId, true));

        return friendsIdList;
    }

    private List<Integer> getUserLikesList(Integer userId) {
        String sqlQuery = "SELECT id_film FROM likes WHERE id_user = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getInt("id_film"), userId);
    }

    private User makeUser(ResultSet rs) throws SQLException {
        Integer userId = rs.getInt("id_user");
        return User.builder()
                .id(userId)
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(LocalDate.parse(rs.getString("birthday")))
                .friendsId(getUserFriendsIdList(userId))
                .likedFilmsId(getUserLikesList(userId))
                .build();
    }
}
