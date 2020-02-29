package scheduler;


public class MLFQtest {

    public static void main(String[] args) {
        ProcessControlBlock a = new ProcessControlBlock(20, 5, 3);
        ProcessControlBlock b = new ProcessControlBlock(20, 10, 1);
        ProcessControlBlock c = new ProcessControlBlock(10, 10, 0);

        MultiLevelFeedbackQueue sched = new MultiLevelFeedbackQueue(0, 2);
        sched.add(a);
        sched.add(b);
        sched.add(c);
        sched.execute();
    }
}