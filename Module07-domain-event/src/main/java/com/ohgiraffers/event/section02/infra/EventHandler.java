package com.ohgiraffers.event.section02.infra;

// 💡 Subscriber(Handler)의 표준 규격(인터페이스)입니다.
// 제네릭을 사용하여 어떤 타입의 이벤트든 처리할 수 있도록 확장성 있게 설계합니다.
public interface EventHandler<T> {
    void handle(T event);
}
