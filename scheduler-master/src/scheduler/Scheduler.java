package scheduler;

/**
 * Created by po917265 on 2/12/20.
 */
public abstract class Scheduler implements Iterable<ProcessControlBlock> {

    protected int contextSwitchTime;  //how long a context switch takes.
    protected int clock;  //the number of cycles since the system was "started" (the current time).

    public Scheduler(int contextSwitchTime) {
        this.contextSwitchTime = contextSwitchTime;
        clock = 0;
    }

    public abstract void add(ProcessControlBlock pcb);
    public abstract ProcessControlBlock next();
    public abstract boolean isEmpty();
    public abstract void execute(ProcessControlBlock pcb);

    public void execute() {
        System.out.println("Executing Processes");
        while(!isEmpty() && clock < 100) {
            updateProcessControlBlocks();
            System.out.println("---------Current Processes----------(CLOCK: " + clock + ")");
            for(ProcessControlBlock pcb : this) {
                System.out.println(pcb);
            }
            ProcessControlBlock pcb = next();
            clock += contextSwitchTime;  //a context switch is happening...
            if(pcb == null) {
                tick();
                continue;
            }
            //System.out.println(pcb + "@" + clock);
            execute(pcb);
            add(pcb);
        }
    }

    public void tick() {
        clock++;
    }

    public void tick(int time) {
        clock += time;
    }

    public void updateProcessControlBlocks() {
        //System.out.println("Updating Processes (CLOCK: " + clock + ")");
        for(ProcessControlBlock pcb : this) {
            pcb.update(clock);
        }
    }

}
