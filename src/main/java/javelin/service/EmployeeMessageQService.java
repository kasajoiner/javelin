package javelin.service;

import javelin.entity.Communication;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;

@Service
public class EmployeeMessageQService {
    private LinkedList<Communication> communications = new LinkedList<>();

    public void pushCommunication(Communication communication) {
        communications.push(communication);
    }

    public Communication pollCommunication() {
        return communications.poll();
    }


}
