package scheduler;
import java.util.Iterator;
import java.util.LinkedList;


public class RoundRobin extends Scheduler {

    private LinkedList<ProcessControlBlock> readyQueue;
    private LinkedList<ProcessControlBlock> waitQueue;
    private LinkedList<ProcessControlBlock> terminated;
    private int quantum;                //How long before the next context switch
    private int totalWaitTime = 0;
    private int cpuIdleTime = 0;


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


    /*
        Round Robin execution.
        A process will run based on a specified quantum then will be inserted into the correct queue.
        - If a process has a duration <= the quantum, it will execute for the remaining duration then will be placed
          in the terminated queue.
        - If a process has a duration > the quantum, it will execute for the specified quantum then will be placed
          in the waiting queue.
     */
    @Override
    public void execute(ProcessControlBlock pcb) {
        if (readyQueue.isEmpty() && !waitQueue.isEmpty()){
            cpuIdleTime++;
        }

        int startTime = clock;      //Current process start time
        for(int i=0; i<quantum; i++) {
            if (pcb.state().equals(ProcessControlBlock.READY)) {
                pcb.execute(1, clock);

                if(!readyQueue.isEmpty()) {
                    totalWaitTime++;        //Process is waiting on the Ready queue
                }

                tick();  //update clock
            }
        }
        int finTime = clock;        //Current process end time
        System.out.println("Process " + pcb.pid() + " has run from " + startTime + " to " + finTime);
        System.out.println(totalWaitTime);
        System.out.print(cpuIdleTime);
    }

    @Override
    public Iterator<ProcessControlBlock> iterator() {
        LinkedList<ProcessControlBlock> everything = new LinkedList<ProcessControlBlock>();
        everything.addAll(readyQueue);
        everything.addAll(waitQueue);
        return everything.iterator();
    }
}