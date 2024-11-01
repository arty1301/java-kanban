import manager.InMemoryTaskManager;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();

        Task task1 = new Task();
        task1.setTitle("Переезд");
        task1.setDescription("Организовать переезд в новый дом");
        manager.addTask(task1);

        Task task2 = new Task();
        task2.setTitle("Купить мебель");
        task2.setDescription("Купить мебель для нового дома");
        manager.addTask(task2);

        Epic epic1 = new Epic();
        epic1.setTitle("Организовать вечеринку в новом доме");
        epic1.setDescription("Убраться в доме, расставить столы для гостей");
        manager.addEpic(epic1);

        Subtask subtask1 = new Subtask();
        subtask1.setTitle("Пригласить гостей");
        subtask1.setDescription("Составить список гостей и оповестить их");
        subtask1.setEpicId(epic1.getId());
        manager.addSubtask(subtask1);

        Subtask subtask2 = new Subtask();
        subtask2.setTitle("Купить продукты");
        subtask2.setDescription("Купить продукты для вечеринки");
        subtask2.setEpicId(epic1.getId());
        manager.addSubtask(subtask2);

        Epic epic2 = new Epic();
        epic2.setTitle("Поездка в Санкт-Петербург");
        epic2.setDescription("Подготовка к поездке в Санкт-Петербург");
        manager.addEpic(epic2);

        Subtask subtask3 = new Subtask();
        subtask3.setTitle("Покупка билетов");
        subtask3.setDescription("Купить билеты на поездку");
        subtask3.setEpicId(epic2.getId());
        manager.addSubtask(subtask3);

        System.out.println("Задачи: ");
        System.out.println(manager.getAllTasks());

        System.out.println("Эпики: ");
        System.out.println(manager.getAllEpics());

        System.out.println("Подзадачи: ");
        System.out.println(manager.getAllSubtasks());

        task1.setStatus(TaskStatus.IN_PROGRESS);
        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        subtask3.setStatus(TaskStatus.DONE);

        manager.updateTask(task1);
        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask2);
        manager.updateSubtask(subtask3);

        System.out.println("Статусы задач и подзадач после обновления: ");
        System.out.println("Задача 1: " + manager.getTask(task1.getId()).getStatus());
        System.out.println("Задача 2: " + manager.getTask(task2.getId()).getStatus());
        System.out.println("Эпик 1: " + manager.getEpic(epic1.getId()).getStatus());
        System.out.println("Эпик 2: " + manager.getEpic(epic2.getId()).getStatus());

        manager.removeTask(task2.getId());
        manager.removeEpic(epic1.getId());

        System.out.println("После удаления задачи и эпика: ");
        System.out.println("Все задачи: " + manager.getAllTasks());
        System.out.println("Все эпики: " + manager.getAllEpics());
    }
}
