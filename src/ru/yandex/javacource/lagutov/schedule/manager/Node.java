package ru.yandex.javacource.lagutov.schedule.manager;

import ru.yandex.javacource.lagutov.schedule.task.Task;

public class Node {
    public Task data;
    public Node next;
    public Node prev;

    public Node(Task data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }

    public Task getData() {
        return data;
    }

    public Node getNext() { return next; }

    public Node getPrev() {
        return prev;
    }

    public void setData(Task data) {this.data = data;}

    public void setNext(Node next) {
        this.next = next;
    }

    public void setPrev(Node prev) { this.prev = prev; }
}