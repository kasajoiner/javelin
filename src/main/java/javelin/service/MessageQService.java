package javelin.service;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Queue;

@Service
public class MessageQService {
    private Queue<Pair<Long, String>> msgs = new ArrayDeque<>();

    public void push(Long id, String txt) {
        msgs.add(Pair.of(id, txt));
    }

    public Pair<Long, String> poll() {
        return msgs.poll();
    }
}
