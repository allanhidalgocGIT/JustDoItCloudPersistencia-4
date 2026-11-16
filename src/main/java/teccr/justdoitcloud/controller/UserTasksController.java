package teccr.justdoitcloud.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import teccr.justdoitcloud.data.Task;
import teccr.justdoitcloud.data.User;
import teccr.justdoitcloud.service.TaskService;

import java.time.LocalDateTime;

@Slf4j
@Controller
@RequestMapping("/user/tasks")
@SessionAttributes("user")
public class UserTasksController {

    private final TaskService taskService;

    public UserTasksController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public String showUserTasks(Model model) {

        User user = (User) model.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        user.setTasks(taskService.getTasksForUser(user));

        model.addAttribute(
                "newTask",
                new Task(null, "", LocalDateTime.now(), null,
                        Task.Status.PENDING)
        );

        return "usertasks";
    }

    @PostMapping
    public String addTask(
            @Valid @ModelAttribute("newTask") Task newTask,
            Errors errors,
            @ModelAttribute("user") User user,
            Model model) {

        if (errors.hasErrors()) {
            user.setTasks(taskService.getTasksForUser(user));
            return "usertasks";
        }

        taskService.addTaskToUser(user, newTask);

        return "redirect:/user/tasks";
    }

    @PostMapping("/{taskId}/advance")
    public String advanceTask(
            @PathVariable Long taskId,
            @ModelAttribute("user") User user) {

        taskService.advanceTask(taskId, user);

        return "redirect:/user/tasks";
    }
}