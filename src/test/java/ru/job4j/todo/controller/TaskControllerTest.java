package ru.job4j.todo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TaskControllerTest {
    private TaskService taskService;

    private PriorityService priorityService;

    private CategoryService categoryService;

    private TaskController taskController;

    private HttpSession httpSession;

    private List<Integer> categories;

    private User user;

    @BeforeEach
    public void initServices() {
        taskService = mock(TaskService.class);
        priorityService = mock(PriorityService.class);
        categoryService = mock(CategoryService.class);
        httpSession = mock(HttpSession.class);
        taskController = new TaskController(taskService, priorityService, categoryService);

        categories = new ArrayList<>();
        categories.add(1);
        categories.add(2);

        user = new User(1, "user", "user", "123", "UTC");
    }

//    @Test
//    void whenRequestTaskListPageThenGetPageWithTasks() {
//        var task1 = new Task(1, "test1", now(), false, user, new Priority(), List.of());
//        var task2 = new Task(2, "test2", now(), false, user, new Priority(), List.of());
//        List<Task> expectedTasks = List.of(task1, task2);
//        when(taskService.findAll()).thenReturn(expectedTasks);
//
//        var model = new ConcurrentModel();
//        String view = taskController.getAll(model, httpSession);
//        var actualTasks = model.getAttribute("tasks");
//
//        assertThat(view).isEqualTo("tasks/list");
//        assertThat(actualTasks).isEqualTo(expectedTasks);
//    }

    @Test
    public void whenRequestIdThenGetPageWithTasks() {
        int searchId = 1;
        var task1 = new Task(1, "test1", now(), false, new User(), new Priority(), List.of());
        when(taskService.findById(searchId)).thenReturn(Optional.of(task1));

        var model = new ConcurrentModel();
        String view = taskController.getById(model, searchId);
        var actualTask = model.getAttribute("task");

        assertThat(view).isEqualTo("tasks/info");
        assertThat(actualTask).isEqualTo(task1);
    }

    @Test
    public void whenRequestIdThenGetPageWithError() {
        int notExistID = 2;
        var expectedMessage = "Задача с указанным идентификатором не найдена";
        when(taskService.findById(notExistID)).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        String view = taskController.getById(model, notExistID);
        var actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void whenPostTaskThenUpdateAndRedirectToTaskPage() throws Exception {
        var taskUpdate = new Task(1, "test1", now(), false, new User(), new Priority(), List.of());

        ArgumentCaptor<Task> taskArgumentCaptor = ArgumentCaptor.forClass(Task.class);
        when(taskService.update(taskArgumentCaptor.capture())).thenReturn(true);

        var model = new ConcurrentModel();
        String view = taskController.update(taskUpdate, categories, model, httpSession);
        Task actualTask = taskArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/tasks");
        assertThat(actualTask).isEqualTo(taskUpdate);
    }

    @Test
    public void whenPostTaskThenTryUpdateAndRedirectToErrorPage() {
        var expectedMessage = "Задача с указанным идентификатором не найдена";
        var taskUpdate = new Task(1, "test1", now(), false, new User(), new Priority(), List.of());
        when(taskService.update(any(Task.class))).thenReturn(false);

        var model = new ConcurrentModel();
        String view = taskController.update(taskUpdate, categories, model, httpSession);
        var actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo(expectedMessage);
    }

    @Test
    void whenDeleteTaskByIdThenGetPageWithTasks() {
        when(taskService.deleteById(any(Integer.class))).thenReturn(true);

        var model = new ConcurrentModel();
        String view = taskController.delete(model, 1);

        assertThat(view).isEqualTo("redirect:/tasks");
    }

    @Test
    void whenDeleteTaskByIdThenGetPageWithError() {
        var expectedMessage = "Задача с указанным идентификатором не найдена";
        when(taskService.deleteById(any(Integer.class))).thenReturn(false);

        var model = new ConcurrentModel();
        String view = taskController.delete(model, 1);
        var actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    void whenRequestTaskListDonePageThenGetPageWithDoneTasks() {
        var task1 = new Task(1, "test1", now(), true, new User(), new Priority(), List.of());
        var task2 = new Task(2, "test2", now(), false, new User(), new Priority(), List.of());
        List<Task> expectedDoneTasks = List.of(task1);
        when(taskService.findByDone(true)).thenReturn(expectedDoneTasks);

        var model = new ConcurrentModel();
        String view = taskController.doneTask(model);
        var actualTasks = model.getAttribute("tasks");

        assertThat(view).isEqualTo("tasks/list");
        assertThat(actualTasks).isEqualTo(expectedDoneTasks);
    }

    @Test
    void whenRequestNewTaskListPageThenGetPageWithInfo() {
        var expectedMessage = "Выполненных задач не найдено";
        var task1 = new Task(1, "test1", now(), true, new User(), new Priority(), List.of());
        var task2 = new Task(2, "test2", now(), true, new User(), new Priority(), List.of());
        List<Task> expectedNewTasks = Collections.emptyList();
        when(taskService.findByDone(false)).thenReturn(expectedNewTasks);

        var model = new ConcurrentModel();
        String view = taskController.doneTask(model);
        var actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("info/info");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }
}
