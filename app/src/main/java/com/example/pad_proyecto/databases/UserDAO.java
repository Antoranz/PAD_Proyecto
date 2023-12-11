package com.example.pad_proyecto.databases;

import com.example.pad_proyecto.data.User;
import java.util.List;

public interface UserDAO {
    public void addUser(User user);
    public List<User> getAllUser();
    public void updateUserBudget(long id, Double budget);
}
