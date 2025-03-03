package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtaskIds = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic() {
        super();
        this.endTime = null;
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void updateEpicTime(List<Subtask> subtasks) {
        if (subtasks.isEmpty()) {
            setStartTime(null);
            setEndTime(null);
            setDuration(Duration.ZERO);
            return;
        }

        LocalDateTime earlyStartTime = null;
        LocalDateTime lateEndTime = null;
        Duration allDuration = Duration.ZERO;

        for (Subtask subtask : subtasks) {
            if (getStartTime() != null && getEndTime() != null) {
                if (earlyStartTime == null || subtask.getStartTime().isBefore(earlyStartTime)) {
                    earlyStartTime = subtask.getStartTime();
                }
                if (lateEndTime == null || subtask.getEndTime().isAfter(lateEndTime)) {
                    lateEndTime = subtask.getEndTime();
                }
                allDuration = allDuration.plus(subtask.getDuration());
            }
        }
        setStartTime(earlyStartTime);
        setEndTime(lateEndTime);
        setDuration(allDuration);
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Название: '" + getTitle() + "', Описание: '" + getDescription() + "', Статус: " + getStatus();
    }
}
