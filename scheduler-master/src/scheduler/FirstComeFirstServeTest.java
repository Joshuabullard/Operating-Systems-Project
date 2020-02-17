package scheduler;

/**
 * Created by po917265 on 2/12/20.
 */
public class FirstComeFirstServeTest {

    public static void main(String[] args) {
        ProcessControlBlock a = new ProcessControlBlock(20, 5, 3);
        ProcessControlBlock b = new ProcessControlBlock(20, 10, 1);
        FirstComeFirstServe sched = new FirstComeFirstServe(0);
        sched.add(a);
        sched.add(b);
        sched.execute();
    }
}
