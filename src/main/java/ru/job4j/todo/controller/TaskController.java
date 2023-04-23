package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    private final PriorityService priorityService;

    private final CategoryService categoryService;


    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "tasks/list";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model) {
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "tasks/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Task task,
                         @RequestParam("category.id") List<Integer> categoryListId, HttpSession session) {
        var user = (User) session.getAttribute("user");
        task.setUser(user);
        var categoriesID = categoryService.findByIdList(categoryListId);
        task.setCategories(categoriesID);
        taskService.add(task);
        return "redirect:/tasks";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        Optional<Task> taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "Задача с указанным идентификатором не найдена");
            return "errors/404";
        }
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("task", taskOptional.get());
        return "tasks/info";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        boolean isDeleted = taskService.deleteById(id);
        if (!isDeleted) {
            model.addAttribute("message", "Задача с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/tasks";
    }

    @GetMapping("/update/{id}")
    public String getUpdatePage(Model model, @PathVariable int id) {
        Optional<Task> taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "Задача с указанным идентификатором не найдена");
            return "errors/404";
        }
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("task", taskOptional.get());
        return "tasks/edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Task task, @RequestParam("category.id") List<Integer> categoryListId,
                         Model model, HttpSession httpSession) {
        var user = (User) httpSession.getAttribute("user");
        task.setUser(user);
        var categoriesID = categoryService.findByIdList(categoryListId);
        task.setCategories(categoriesID);
        boolean isUpdated = taskService.update(task);
        if (!isUpdated) {
            model.addAttribute("message", "Задача с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/tasks";
    }


    @GetMapping("/perform/{id}")
    public String perform(Model model, @PathVariable int id) {
        var updateField = taskService.updateFieldDone(id);
        if (!updateField) {
            model.addAttribute("message", "Задача с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/tasks";
    }

    @GetMapping("/done")
    public String doneTask(Model model) {
        var taskCollection = taskService.findByDone(true);
        if (taskCollection.isEmpty()) {
            model.addAttribute("message", "Выполненных задач не найдено");
            return "info/info";
        }
        model.addAttribute("tasks", taskCollection);
        return "tasks/list";
    }

    @GetMapping("/new")
    public String newTask(Model model) {
        var taskCollection = taskService.findByDone(false);
        if (taskCollection.isEmpty()) {
            model.addAttribute("message", "Новых задач не найдено");
            return "info/info";
        }
        model.addAttribute("tasks", taskCollection);
        return "tasks/list";
    }
}
