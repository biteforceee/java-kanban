package ru.yandex.javacource.lagutov.schedule.manager;
import ru.yandex.javacource.lagutov.schedule.task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final myLinkedList list= new myLinkedList();

    @Override
    public void add(Task task) {
        list.linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return list.getTasks();
    }

    @Override
    public void remove(int id) { list.removeNode(list.getNode(id));}

    private class myLinkedList {
        private Map<Integer,Node> table=new HashMap<>();
        private Node head;
        private Node tail;
        public void linkLast(Task task){
            Node node = new Node(task);

            if(table.containsKey(task.getId())){
                removeNode(node);
            }
            if(head== null){
                head=node;
                tail=node;
                node.setNext(null);
                node.setPrev(null);
            }
            else{
                node.setPrev(tail);
                node.setNext(null);
                tail.setNext(node);
                tail=node;
            }
            table.put(task.getId(),node);
        }
        private void removeNode(Node node){
            if(node!=null){
                Node tmp=table.get(node.getData().getId());
                table.remove(node.getData().getId());
                Node prev = tmp.getPrev();
                Node next = tmp.getNext();
                if(head == tmp){
                    head = tmp.getNext();
                }
                if(tail == tmp){
                    tail = tmp.getPrev();
                }
                if(next!=null){
                    next.setPrev(prev);
                }
                if(prev!=null){
                    prev.setNext(next);
                }
            }
        }
        private Node getNode(int id){
            return table.get(id);
        }
        private List<Task> getTasks(){
            List<Task> list =new ArrayList<>();
            Node h=head;
            while (h!=null){
                list.add(h.getData());
                h=h.getNext();
            }
            return list;
        }
    }
}
