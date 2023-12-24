package com.hyperion.train_preserve_ticket.service;

import com.hyperion.train_preserve_ticket.model.Users;

import java.util.List;

public interface UserService {

    public void addUsers(Users user);

    public List<Users> getUsersById(String id);



}
