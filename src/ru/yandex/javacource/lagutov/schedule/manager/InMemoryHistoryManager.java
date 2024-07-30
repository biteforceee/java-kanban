package ru.yandex.javacource.lagutov.schedule.manager;
import ru.yandex.javacource.lagutov.schedule.task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList list = new CustomLinkedList();

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        remove(task.getId());
        list.linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return list.getTasks();
    }

    @Override
    public void remove(int id) {
        Node removedNode = list.table.remove(id);
        if (removedNode == null) {
            return;
        }
        list.removeNode(removedNode);
    }

    private class CustomLinkedList {

        private Map<Integer,Node> table = new HashMap<>();

        private Node head;

        private Node tail;

        public void linkLast(Task task) {
            Node node = new Node(task, tail, null);
            if (head == null) {
                head = node;
            } else {
                tail.setNext(node);
            }
            tail = node;
            table.put(task.getId(),node);
        }

        private void removeNode(Node node) {
            if (node != null) {
                Node prev = node.getPrev();
                Node next = node.getNext();
                if (head == node) {
                    head = node.getNext();
                }
                if (tail == node) {
                    tail = node.getPrev();
                }
                if (next != null) {
                    next.setPrev(prev);
                }
                if (prev != null) {
                    prev.setNext(next);
                }
            }
        }

        private List<Task> getTasks() {
            List<Task> list = new ArrayList<>();
            Node headCopy = head;
            while (headCopy != null) {
                list.add(headCopy.getData());
                headCopy = headCopy.getNext();
            }
            return list;
        }
    }
}
