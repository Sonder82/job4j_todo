package ru.job4j.todo;

import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;

import javax.servlet.http.HttpSession;
import java.time.ZoneId;
import java.util.TimeZone;

public final class UserTimeZone {

    private UserTimeZone() {
    }

    public static void setUserTimeZone(Task task, User user) {
        if (user.getUserzone() == null) {
            user.setUserzone(TimeZone.getDefault().getID());
        }
        var time = task.getCreated()
                .atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.of(user.getUserzone())).toLocalDateTime();
        task.setCreated(time);
    }
}
