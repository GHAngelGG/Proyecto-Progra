package login.proyecto;

import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private static UserManager instance;
    private final List<User> users = new ArrayList<>();

    public static class User {
        public String username;
        public String password;
        public String role;
        public String status;

        public User(String username, String password, String role, String status) {
            this.username = username;
            this.password = password;
            this.role = role;
            this.status = status;
        }
    }

    private UserManager() {
        // Credentials follow password policy: 13+ chars, 1 uppercase, 1 special char
        users.add(new User("sysadmin",   "Syst3m@Admin2025!",  "Admin",   "Active"));
        users.add(new User("opsmanager", "Ops@M4nager2025!",   "Manager", "Active"));
        users.add(new User("jdoe",       "Jd0e#Secure2025!",   "User",    "Active"));
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public boolean authenticate(String username, String password) {
        for (User u : users) {
            if (u.username.equals(username) && u.password.equals(password)
                    && "Active".equals(u.status)) {
                return true;
            }
        }
        return false;
    }

    public List<User> getUsers() {
        return new ArrayList<>(users);
    }

    public User findByUsername(String username) {
        for (User u : users) {
            if (u.username.equalsIgnoreCase(username)) {
                return u;
            }
        }
        return null;
    }

    /** Returns null on success, or an error message. */
    public String addUser(String username, String password, String role) {
        if (username == null || username.trim().isEmpty()) {
            return "Username cannot be empty.";
        }
        if (findByUsername(username) != null) {
            return "Username already exists.";
        }
        users.add(new User(username.trim(), password, role, "Active"));
        return null;
    }

    public boolean updateUser(String username, String role, String status) {
        User u = findByUsername(username);
        if (u == null) return false;
        u.role = role;
        u.status = status;
        return true;
    }

    public boolean updatePassword(String username, String newPassword) {
        User u = findByUsername(username);
        if (u == null) return false;
        u.password = newPassword;
        return true;
    }
}
