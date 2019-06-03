package com.example.messageapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner{
    // Instantiate both the user and role repository to invoke constructor methods
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /*
        Run method will be executed after the application context is
        loaded and right before the Spring Application run method is
        completed.
     */
    @Override
    public void run(String... strings) throws Exception{
        System.out.println("Loading data...");

        roleRepository.save(new Role("USER"));
        roleRepository.save(new Role("ADMIN"));

        Role userRole = roleRepository.findByRole("USER");
        Role adminRole = roleRepository.findByRole("ADMIN");

//        User user = new User("bob@bob.com",passwordEncoder.encode("password"),"Bob","Bobberson",true,"bob");
//        user.setRoles(Arrays.asList(userRole));
//        userRepository.save(user);
//
//        user = new User("admin@adm.com",passwordEncoder.encode("password"),"Admin","User",true,"admin");
//        user.setRoles(Arrays.asList(adminRole));
//        userRepository.save(user);

//        Message message = new Message("Dogs", "This post is about dogs. I like dogs and they are fluffy.", "5/31/2019");
//        messageRepository.save(message);
//
//        message = new Message("Cats", "This post is about cats. I like cats and they are grumpy.", "5/1/2019");
//        messageRepository.save(message);
//
//        message = new Message("Fish", "This post is about fish. I like fish and they like to swim.", "4/3/2019");
//        messageRepository.save(message);
    }
}
