package scheduler;
import java.util.Iterator;
import java.util.LinkedList;


public class RoundRobin extends Scheduler {

    private LinkedList<ProcessControlBlock> readyQueue;
    private LinkedList<ProcessControlBlock> waitQueue;
    private LinkedList<ProcessControlBlock> terminated;
    private int quantum;
    private int totalWaitTime = 0;
    private int cpuIdleTime = 0;        //How long the CPU is idle waiting for a process


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

        //print results to screen when all processes are complete:
        if(isEmpty()){
            int avgWaitTime = totalWaitTime / terminated.size();
            int procRunning = (clock-1) - cpuIdleTime;
            float cpuUtil = ((float)procRunning / ((float)clock-1)) *(float)100;

            System.out.println("\n====================================");
            System.out.println("ROUND ROBIN...");
            System.out.print("The average wait time was: ");
            System.out.print(avgWaitTime);

            System.out.print("\nThe CPU utilization was: ");
            System.out.printf("%.2f", cpuUtil);
            System.out.print("%");
            System.out.println("\n====================================");
        }
    }

    @Override
    public ProcessControlBlock next() {
        for(ProcessControlBlock pcb : waitQueue) {
            if(pcb.state().equals(ProcessControlBlock.READY)) {
                readyQueue.add(pcb);
                waitQueue.remove(pcb);
            }

            if(readyQueue.isEmpty()){
                cpuIdleTime++;          //CPU is idle waiting for a process
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
        int startTime = clock;      //Current process start time
        for(int i=0; i<quantum; i++) {
            if (pcb.state().equals(ProcessControlBlock.READY)) {
                pcb.execute(1, clock);

                if(!readyQueue.isEmpty()) {
                    int waitingProc = readyQueue.size();
                    totalWaitTime += waitingProc;         //Processes waiting on the Ready queue
                }

                tick();  //update clock
            }
        }
        int finTime = clock-1;        //Current process end time
        System.out.println("Process " + pcb.pid() + " has run from " + startTime + " to " + finTime);
    }

    @Override
    public Iterator<ProcessControlBlock> iterator() {
        LinkedList<ProcessControlBlock> everything = new LinkedList<ProcessControlBlock>();
        everything.addAll(readyQueue);
        everything.addAll(waitQueue);
        return everything.iterator();
    }
}