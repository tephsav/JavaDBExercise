package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        userService.createUsersTable();

        User user1 = new User("Alexandr", "Klimuk", (byte) 35);
        User user2 = new User("Vasilyi", "Bodrov", (byte) 17);
        User user3 = new User("Kirill", "Pashkov", (byte) 24);
        User user4 = new User("Dmitryi", "Voskresnyi", (byte) 50);

        User[] users = {user1, user2, user3, user4};
        for (User user : users) {
            userService.saveUser(user.getName(), user.getLastName(), user.getAge());
            System.out.println("User с именем – " + user.getName() + " добавлен в базу данных");
        }

        int startAge = 20;
        int finishAge = 40;

        List<User> usersList = userService.getUsersWhoseAgeBetween(startAge, finishAge);
        for (User user : usersList) {
            System.out.println(user);
        }

        userService.cleanUsersTable();

        userService.dropUsersTable();
    }
}
