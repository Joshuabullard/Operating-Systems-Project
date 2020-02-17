package scheduler;
import java.util.Iterator;
import java.util.LinkedList;


public class RoundRobin extends Scheduler {


    private LinkedList<ProcessControlBlock> readyQueue;
    private LinkedList<ProcessControlBlock> waitQueue;
    private LinkedList<ProcessControlBlock> terminated;

    //default constructor
    public RoundRobin(int contextSwitchTime) {
        super(contextSwitchTime);
        readyQueue = new LinkedList<ProcessControlBlock>();
        waitQueue = new LinkedList<ProcessControlBlock>();
        terminated = new LinkedList<ProcessControlBlock>();
    }

    @Override
    public void add(ProcessControlBlock pcb) {

    }

    @Override
    public ProcessControlBlock next() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void execute(ProcessControlBlock pcb) {

    }

    @Override
    public Iterator<ProcessControlBlock> iterator() {
        return null;
    }
}
