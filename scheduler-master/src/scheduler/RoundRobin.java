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
        int quantum = 5;
    }

    @Override
    public void add(ProcessControlBlock pcb) {
        if(pcb.state().equals(ProcessControlBlock.READY)) readyQueue.add(pcb);
        else if(pcb.state().equals(ProcessControlBlock.WAITING)) waitQueue.add(pcb);
        else if(pcb.state().equals(ProcessControlBlock.TERMINATED)) terminated.add(pcb);
        else throw new RuntimeException("Process " + pcb.pid() + " in illegal state: " + pcb.state());
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
