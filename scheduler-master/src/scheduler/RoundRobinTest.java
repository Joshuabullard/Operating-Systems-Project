package scheduler;


public class RoundRobinTest {

    public static void main(String[] args) {
        ProcessControlBlock a = new ProcessControlBlock(5, 5, 3);
        ProcessControlBlock b = new ProcessControlBlock(20, 20, 1);
        ProcessControlBlock c = new ProcessControlBlock(50, 50, 0);

        RoundRobin sched = new RoundRobin(0, 4);
        sched.add(a);
        sched.add(b);
        sched.add(c);
        sched.execute();
    }
}