package scheduler;
import java.util.Iterator;
import java.util.LinkedList;

public class MultiLevelFeedbackQueue extends Scheduler{

    private LinkedList<ProcessControlBlock> readyQueueLow;
    private LinkedList<ProcessControlBlock> readyQueueMid;
    private LinkedList<ProcessControlBlock> readyQueueHigh;
    private LinkedList<ProcessControlBlock> waitQueue;
    private LinkedList<ProcessControlBlock> terminated;
    private int quantum;
    private int cpuIdleTime = 0;        //How long the CPU is idle waiting for a process
    private int totalWaitTime = 0;      //Total time of all process spent waiting on READY queues

    //default constructor
    public MultiLevelFeedbackQueue(int contextSwitchTime, int quantum) {
        super(contextSwitchTime);
        readyQueueLow = new LinkedList<ProcessControlBlock>();
        readyQueueMid = new LinkedList<ProcessControlBlock>();
        readyQueueHigh = new LinkedList<ProcessControlBlock>();
        waitQueue = new LinkedList<ProcessControlBlock>();
        terminated = new LinkedList<ProcessControlBlock>();
        this.quantum = quantum;
    }

    @Override
    public void add(ProcessControlBlock pcb) {
        if(pcb.state().equals(ProcessControlBlock.READY)){
            if(pcb.level().equals(ProcessControlBlock.LOW))
                readyQueueLow.add(pcb);
            else if(pcb.level().equals(ProcessControlBlock.MIDDLE))
                readyQueueMid.add(pcb);
            else
                readyQueueHigh.add(pcb);
        }
        else if(pcb.state().equals(ProcessControlBlock.WAITING)) waitQueue.add(pcb);
        else if(pcb.state().equals(ProcessControlBlock.TERMINATED)) terminated.add(pcb);
        else throw new RuntimeException("Process " + pcb.pid() + " in illegal state: " + pcb.state());
    }

    @Override
    public ProcessControlBlock next() {
        for(ProcessControlBlock pcb : waitQueue) {
            if(pcb.state().equals(ProcessControlBlock.READY)) {
                if(pcb.level().equals(ProcessControlBlock.LOW))
                    readyQueueLow.add(pcb);
                else if(pcb.level().equals(ProcessControlBlock.MIDDLE))
                    readyQueueMid.add(pcb);
                else
                    readyQueueHigh.add(pcb);

                waitQueue.remove(pcb);
            }

            if(readyQueueLow.isEmpty() && readyQueueMid.isEmpty() && readyQueueHigh.isEmpty()){
                cpuIdleTime++;          //CPU is idle waiting for a process
            }
        }

        if(!readyQueueLow.isEmpty()) return readyQueueLow.remove();
        if(!readyQueueMid.isEmpty()) return readyQueueMid.remove();
        if(!readyQueueHigh.isEmpty()) return readyQueueHigh.remove();
        return null;
    }

    @Override
    public boolean isEmpty() {return readyQueueLow.isEmpty() && readyQueueMid.isEmpty() &&
                                     readyQueueHigh.isEmpty() && waitQueue.isEmpty();}

    @Override
    public void execute(ProcessControlBlock pcb) {
        int startTime = clock;      //Current process start time

        //Determine quantum based on priority level:
        int iterations = 0;
        switch(pcb.level()){
            case ProcessControlBlock.LOW: iterations = quantum; break;
            case ProcessControlBlock.MIDDLE: iterations = (int)Math.pow(quantum,2); break;
            case ProcessControlBlock.HIGH: iterations = (int)Math.pow(quantum,3); break;
        }

        for(int i=0; i<iterations; i++) {
            if (pcb.state().equals(ProcessControlBlock.READY)) {
                pcb.execute(1, clock);

                if(!readyQueueLow.isEmpty() || !readyQueueMid.isEmpty() || !readyQueueHigh.isEmpty()) {
                    int waitingProc = readyQueueLow.size() + readyQueueMid.size() + readyQueueHigh.size();
                    totalWaitTime += waitingProc;         //Processes waiting on the Ready queue
                }

                tick();  //update clock
            }
        }
        int finTime = clock-1;        //Current process end time
        System.out.println("Process " + pcb.pid() + " has run from " + startTime + " to " + finTime + " on " + pcb.level() + " Queue");

        //Update Priority level
        if(pcb.level().equals(ProcessControlBlock.MIDDLE))
            pcb.chLevel(ProcessControlBlock.HIGH);
        else if(pcb.level().equals(ProcessControlBlock.LOW))
            pcb.chLevel(ProcessControlBlock.MIDDLE);
    }

    @Override
    public Iterator<ProcessControlBlock> iterator() {
        LinkedList<ProcessControlBlock> everything = new LinkedList<ProcessControlBlock>();
        everything.addAll(readyQueueLow);
        everything.addAll(waitQueue);
        return everything.iterator();
    }
}