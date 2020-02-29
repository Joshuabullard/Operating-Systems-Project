package scheduler;


public class RoundRobinTest {

    public static void main(String[] args) {
        ProcessControlBlock a = new ProcessControlBlock(20, 5, 3);
        ProcessControlBlock b = new ProcessControlBlock(20, 10, 1);
        ProcessControlBlock c = new ProcessControlBlock(40, 10, 10);

        RoundRobin sched = new RoundRobin(0, 4);
        sched.add(a);
        sched.add(b);
        sched.add(c);
        sched.execute();
    }
}