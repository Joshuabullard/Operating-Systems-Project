package scheduler;


public class MLFQtest {

    public static void main(String[] args) {
        ProcessControlBlock a = new ProcessControlBlock(20, 20, 0);
        ProcessControlBlock b = new ProcessControlBlock(10, 10, 0);
        ProcessControlBlock c = new ProcessControlBlock(30, 30, 10);

        MultiLevelFeedbackQueue sched = new MultiLevelFeedbackQueue(0, 2);
        sched.add(a);
        sched.add(b);
        sched.add(c);
        sched.execute();
    }
}