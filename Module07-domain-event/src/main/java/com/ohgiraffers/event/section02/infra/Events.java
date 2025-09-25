package com.ohgiraffers.event.section02.infra;


import com.ohgiraffers.event.section02.aggregate.CourseCompletedEvent;


import java.util.ArrayList;
import java.util.List;

// 💡 Publisher 역할을 하는 클래스입니다. 이벤트가 발생하면 등록된 Handler들에게 이벤트를 전파합니다.
public class Events {
    private static List<EventHandler<CourseCompletedEvent>> handlers = new ArrayList<>();

    public static void addHandler(EventHandler<CourseCompletedEvent> handler) {
        handlers.add(handler);
    }
    public static void raise(CourseCompletedEvent event) {
        for (EventHandler<CourseCompletedEvent> handler : handlers) {
            handler.handle(event);
        }
    }
    public static void clearHandlers() { handlers.clear(); }
}