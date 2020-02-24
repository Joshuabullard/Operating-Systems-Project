package scheduler;


public class RoundRobinTest {

    public static void main(String[] args) {
        ProcessControlBlock a = new ProcessControlBlock(20, 20, 0);
        ProcessControlBlock b = new ProcessControlBlock(10, 20, 0);
        ProcessControlBlock c = new ProcessControlBlock(30, 20, 0);

        RoundRobin sched = new RoundRobin(0, 4);
        sched.add(a);
        sched.add(b);
        sched.add(c);
        sched.execute();
    }
}