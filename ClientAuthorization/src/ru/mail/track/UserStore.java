package ru.mail.track;


public class UserStore {


    // Вам нужно выбрать, как вы будете хранить ваших пользователей, например в массиве User users[] = new User[100];
    User users[] = new User[100];
    int usersCount = 0;

    // проверить, есть ли пользователь с таким именем
    // если есть, вернуть true
    boolean isUserExist(String name) {
        for (int i = 0; i < usersCount; i++) {
            User arg = users[i];
            if (name != null && name.equals(arg.getName()))
                return true;
        }
        return false;
    }

    // Добавить пользователя в хранилище
    void addUser(User user) {
        users[usersCount] = new User(user.getName(), user.getPass());
        usersCount++;
    }

    // Получить пользователя по имени и паролю
    User getUser(String name, String pass) {
        for (int i = 0; i < usersCount; i++) {
            User arg = users[i];
            if (name != null && pass != null && name.equals(arg.getName()) && pass.equals(arg.getPass()))
                return arg;
        }
        return null;
    }
}
