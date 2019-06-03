package com.example.messageapp;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    private UserService userService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    CloudinaryConfig cloudc;



    @RequestMapping("/")
    public String index(Model model) {

        model.addAttribute("messages", messageRepository.findAll());
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("users", userRepository.findAll());
        return "index";
    }

    @RequestMapping("/basehtml")
    public String base(Model model){

        model.addAttribute("messages", messageRepository.findAll());
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("users", userRepository.findAll());
        return "base";
    }

    //Security

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("users", userRepository.findAll());
        return "registration";
    }

    @PostMapping("/register")
    public String processRegistrationPage(@Valid @ModelAttribute("user") User user, @RequestParam("role") String role, BindingResult result, Model model) {
        model.addAttribute("user", user);

        if(result.hasErrors()) {
            return "registration";
        }
        else {
            userService.saveUser(user, role);
            model.addAttribute("message", "User Account Successfully Created");
        }
        return "redirect:/";
    }

    @RequestMapping("/secure")
    public String secure(Model model) {
        // Gets the currently logged in user and maps it to "user" in the Thymeleaf template
        model.addAttribute("user", userService.getCurrentUser());

        return "redirect:/";
    }


    //Message

    @GetMapping("/add")
    public String messageForm(Model model){
        model.addAttribute("message", new Message());
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("userFullName", userService.getUserFullName());
        model.addAttribute("users", userRepository.findAll());
        return "messageform";
    }

    @PostMapping("/process")
    public String processForm(@Valid Message message, BindingResult result, @RequestParam("file")MultipartFile file) {


        if (result.hasErrors()) {
            return "messageform";
        }
        if (file.isEmpty()) {
            message.setImage(" ");
        } else {
            try {
                Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
                message.setImage(uploadResult.get("url").toString());
            } catch (IOException e) {
                e.printStackTrace();
                return "redirect:/add";
            }

        }
        message.setPostedBy(userService.getCurrentUser().getUsername());
        message.setUser(userService.getCurrentUser());
        messageRepository.save(message);
        return "redirect:/";
    }

    @RequestMapping("/detail/{id}")
    public String showMessage(@PathVariable("id") long id, Model model){
        model.addAttribute("message", messageRepository.findById(id).get());
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("users", userRepository.findAll());

        return"show";
    }

    @RequestMapping("/update/{id}")
    public String updateMessage(@PathVariable("id") long id, Model model){
        model.addAttribute("message", messageRepository.findById(id).get());
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("userFullName", userService.getUserFullName());
        model.addAttribute("users", userRepository.findAll());

        return"messageform";
    }

    @RequestMapping("/delete/{id}")
    public String delMessage(@PathVariable("id") long id){
        messageRepository.deleteById(id);
        return"redirect:/";
    }


    //user pages
    @RequestMapping("/user")
    public String userPage(Model model){
        model.addAttribute("messages", messageRepository.findAll());
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("users", userRepository.findAll());


        return "userpage";
    }

    @RequestMapping("/follow/{id}")
    public String followUser(@PathVariable("id") long id, Model model){
//        model.addAttribute("message", messageRepository.findById(id).get());
//        model.addAttribute("user", userService.getCurrentUser());
//        model.addAttribute("userFullName", userService.getUserFullName());
//        model.addAttribute("users", userRepository.findAll());

        userService.getCurrentUser().setFollowed(userRepository.findById(id));
        userRepository.save(userService.getCurrentUser());


        return"redirect:/";
    }
}
