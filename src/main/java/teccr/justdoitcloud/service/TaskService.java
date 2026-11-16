package teccr.justdoitcloud.service;

import org.springframework.stereotype.Service;
import teccr.justdoitcloud.data.Task;
import teccr.justdoitcloud.data.User;
import teccr.justdoitcloud.repository.TaskRepository;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getTasksForUser(User user) {
        return taskRepository.findByUserId(user.getId());
    }

    public void addTaskToUser(User user, Task task) {
        task.setUserId(user.getId());
        taskRepository.save(task);
    }

    public boolean advanceTask(Long taskId, User user) {

        Task task = taskRepository.findById(taskId).orElse(null);

        if (task == null || task.getUserId() == null ||
                !task.getUserId().equals(user.getId())) {
            return false;
        }

        Task.Status nextStatus;

        if (task.getStatus() == Task.Status.PENDING) {
            nextStatus = Task.Status.INPROGRESS;
        } else if (task.getStatus() == Task.Status.INPROGRESS) {
            nextStatus = Task.Status.DONE;
        } else {
            return false;
        }

        Task updatedTask = new Task(
                task.getId(),
                task.getDescription(),
                task.getCreatedAt(),
                task.getDeadline(),
                nextStatus
        );

        updatedTask.setUserId(user.getId());
        taskRepository.save(updatedTask);

        return true;
    }
}