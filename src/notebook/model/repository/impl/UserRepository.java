package notebook.model.repository.impl;

import notebook.model.dao.impl.FileOperation;
import notebook.util.UserValidator;
import notebook.util.mapper.impl.UserMapper;
import notebook.model.User;
import notebook.model.repository.GBRepository;

import java.util.*;

public class UserRepository implements GBRepository {
    private final UserMapper mapper;
    private final FileOperation operation;
    private java.util.Collections Collections;

    public UserRepository(FileOperation operation) {
        this.mapper = new UserMapper();
        this.operation = operation;
    }

    @Override
    public List<User> findAll() {
        List<String> lines = operation.readAll();
        List<User> users = new ArrayList<>();
        for (String line : lines) {
            users.add(mapper.toOutput(line));
        }
        return users;
    }

    public void userValid(User user) {
        UserValidator validator = new UserValidator();

        user.setFirstName(validator.isNameValid(user.getFirstName()));
        user.setLastName(validator.isNameValid(user.getLastName()));
    }

    @Override
    public User create(User user) {
        userValid(user);
        List<User> users = findAll();
        long max = 0L;
        for (User u : users) {
            long id = u.getId();
            if (max < id) {
                max = id;
            }
        }
        long next = max + 1;
        user.setId(next);
        users.add(user);
        write(users);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> update(Long userId, User update) {
        List<User> users = findAll();
        User editUser = users.stream()
                .filter(u -> u.getId()
                        .equals(userId))
                .findFirst().orElseThrow(() -> new RuntimeException("User not found"));
        if (!update.getFirstName().isEmpty()) {
            editUser.setFirstName(update.getFirstName());
        }
        if (!update.getLastName().isEmpty()) {
            editUser.setLastName(update.getLastName());
        }
        if (!update.getPhone().isEmpty()) {
            editUser.setPhone(update.getPhone());
        }

        write(users);
        return Optional.of(update);
    }
    public void delete(Long userId) {
        List<User> users = findAll();
        User DeleteUser = users.stream()
                .filter(u -> u.getId()
                        .equals(userId))
                .findFirst().orElseThrow(() -> new RuntimeException("User not found"));
        users.remove(DeleteUser);
        write(users);
    }


    private void write(List<User> users) {
        List<String> lines = new ArrayList<>();
        for (User u : users) {
            lines.add(mapper.toInput(u));
        }
        operation.saveAll(lines);
    }

}
