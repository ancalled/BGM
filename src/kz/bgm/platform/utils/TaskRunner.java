package kz.bgm.platform.utils;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class TaskRunner {

    private final ExecutorService executorService;
    private final Map<String, Task> taskMap = new HashMap<>();

    public TaskRunner() {
        executorService = Executors.newSingleThreadExecutor();
//        executorService = Executors.newFixedThreadPool(3);
    }

    public <S extends Enum> void submit(String id, Task<S> task) {
        taskMap.put(id, task);
        executorService.submit(task);
    }

    public Task getTask(String id) {
        return taskMap.get(id);
    }

    public static abstract class Task<S extends Enum> implements Callable {

        private final AtomicReference<S> status = new AtomicReference<>();

        protected void changeStatus(S newStatus) {
            status.lazySet(newStatus);
//            while (status.compareAndSet(status.get(), newStatus)); // ;-)
        }

        public S getStatus() {
            return status.get();
        }

    }


    private static final TaskRunner INSTANCE = new TaskRunner();

    public static TaskRunner getInstance() {
        return INSTANCE;
    }
}
