package scheduler;
import java.util.Iterator;
import java.util.LinkedList;


public class RoundRobin extends Scheduler {

    private LinkedList<ProcessControlBlock> readyQueue;
    private LinkedList<ProcessControlBlock> waitQueue;
    private LinkedList<ProcessControlBlock> terminated;
    private int quantum;       //How long before the next context switch

    //default constructor
    public RoundRobin(int contextSwitchTime, int quantum) {
        super(contextSwitchTime);
        readyQueue = new LinkedList<ProcessControlBlock>();
        waitQueue = new LinkedList<ProcessControlBlock>();
        terminated = new LinkedList<ProcessControlBlock>();
        this.quantum = quantum;
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
        for(ProcessControlBlock pcb : waitQueue) {
            if(pcb.state().equals(ProcessControlBlock.READY)) {
                readyQueue.add(pcb);
                waitQueue.remove(pcb);
            }
        }
        if(! readyQueue.isEmpty()) return readyQueue.remove();
        return null;
    }

    @Override
    public boolean isEmpty() {return readyQueue.isEmpty() && waitQueue.isEmpty();}

    @Override
    public void execute(ProcessControlBlock pcb) {
        if(pcb.state().equals(ProcessControlBlock.READY)) {
            pcb.execute(quantum, clock);


            if(pcb.state().equals(ProcessControlBlock.READY)) {
                pcb.state().equals(ProcessControlBlock.WAITING); //Move process to WAITING to process next in READY
                tick();
            }
        }
    }

    @Override
    public Iterator<ProcessControlBlock> iterator() {
        LinkedList<ProcessControlBlock> everything = new LinkedList<ProcessControlBlock>();
        everything.addAll(readyQueue);
        everything.addAll(waitQueue);
        return everything.iterator();
    }
}
