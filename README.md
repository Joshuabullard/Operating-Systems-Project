# Operating-Systems-Project

- Progamming lanuage:  JAVA
- Description:  Simulation of CPU scheduling algorithms. Calculating CPU utilization and average wait times.

## 1) FirstComeFirstServe    
Each process fully completes in the order they are recieved.
## 2) MultiLevelFeedbackQueue  
Three levels of READY queues, LOW, MIDDLE, and HIGH. All processes will start on the LOW ready queue and given a quantum specified by the user. If the process uses the entire quantum and has not terminated then it will be moved up one level. MIDDLE queue is given quantum ^2 and HIGH queue given quantum ^3.
## 3) RoundRobin
A process will run based on a specified quantum then will be inserted into the correct queue.
- If a process has a duration <= the quantum, it will execute for the remaining duration then will be placed in the terminated queue.
- If a process has a duration > the quantum, it will execute for the specified quantum then will be placed in the waiting queue.
