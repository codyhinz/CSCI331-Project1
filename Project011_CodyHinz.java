import java.util.ArrayList;
import java.util.List;

class PCB {
    int index;
    PCB parent;
    List<PCB> children;

    public PCB(int index, PCB parent) {
        this.index = index;
        this.parent = parent;
        this.children = new ArrayList<>();
    }
}

class ProcessManagement {
    private List<PCB> pcbList;

    public ProcessManagement() {
        pcbList = new ArrayList<>();
    }

    public void create(PCB parent) {
        int index = pcbList.size();
        PCB newPCB = new PCB(index, parent);
        pcbList.add(newPCB);

        if (parent != null) {
            parent.children.add(newPCB);
        }
    }

    public void destroy(PCB pcb) {
        // Create a copy of the children list to avoid ConcurrentModificationException
        List<PCB> childrenCopy = new ArrayList<>(pcb.children);

        // Recursively destroy all descendant processes
        for (PCB child : childrenCopy) {
            destroy(child);
        }

        // Remove PCB from the linked list of children of its parent
        if (pcb.parent != null) {
            pcb.parent.children.remove(pcb);
        }

        // Deallocate the element from the linked list
        pcbList.set(pcb.index, null);
    }

    public PCB getPCB(int index) {
        return pcbList.get(index);
    }

    public List<PCB> getPCBList() {
        return pcbList;
    }
}

public class Project011 {
    public static void main(String[] args) {
        ProcessManagement pm = new ProcessManagement();

        // Create processes
        pm.create(null); // Create process 0
        pm.create(pm.getPCB(0)); // Create process 1 as a child of process 0
        pm.create(pm.getPCB(1)); // Create process 2 as a child of process 1
        pm.create(pm.getPCB(2)); // Create process 3 as a child of process 2
        pm.create(pm.getPCB(0)); // Create process 4 as a child of process 0
        pm.create(pm.getPCB(4)); // Create process 5 as a child of process 4

        System.out.println("Initial process hierarchy:");
        printProcessHierarchy(pm.getPCB(0), 0);

        // Destroy process 1
        System.out.println("\nDestroying process 1...");
        pm.destroy(pm.getPCB(1));

        System.out.println("Process hierarchy after destroying process 1:");
        printProcessHierarchy(pm.getPCB(0), 0);

        // Destroy process 4
        System.out.println("\nDestroying process 4...");
        pm.destroy(pm.getPCB(4));

        System.out.println("Process hierarchy after destroying process 4:");
        printProcessHierarchy(pm.getPCB(0), 0);

        // Destroy process 0
        System.out.println("\nDestroying process 0...");
        pm.destroy(pm.getPCB(0));

        System.out.println("Process hierarchy after destroying process 0:");
        printProcessHierarchy(null, 0);
    }

    private static void printProcessHierarchy(PCB pcb, int level) {
        if (pcb == null) {
            return;
        }

        for (int i = 0; i < level; i++) {
            System.out.print("  ");
        }
        System.out.println("Process " + pcb.index);

        for (PCB child : pcb.children) {
            printProcessHierarchy(child, level + 1);
        }
    }
}