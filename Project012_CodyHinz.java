import java.util.ArrayList;
import java.util.List;

class PCB {
    int parent;
    int first_child;
    int younger_sibling;
    int older_sibling;

    public PCB(int parent) {
        this.parent = parent;
        this.first_child = -1;
        this.younger_sibling = -1;
        this.older_sibling = -1;
    }
}

class ProcessManagement {
    private List<PCB> pcbList;

    public ProcessManagement() {
        pcbList = new ArrayList<>();
    }

    public void create(int parent) {
        int index = pcbList.size();
        PCB newPCB = new PCB(parent);
        pcbList.add(newPCB);

        if (parent != -1) {
            PCB parentPCB = pcbList.get(parent);
            if (parentPCB.first_child == -1) {
                parentPCB.first_child = index;
            } else {
                int lastChild = parentPCB.first_child;
                while (pcbList.get(lastChild).younger_sibling != -1) {
                    lastChild = pcbList.get(lastChild).younger_sibling;
                }
                pcbList.get(lastChild).younger_sibling = index;
                newPCB.older_sibling = lastChild;
            }
        }
    }

    public void destroy(int index) {
        PCB pcb = pcbList.get(index);

        // Recursively destroy all descendant processes
        int child = pcb.first_child;
        while (child != -1) {
            destroy(child);
            child = pcbList.get(child).younger_sibling;
        }

        // Remove PCB from the sibling list of its parent
        if (pcb.parent != -1) {
            PCB parentPCB = pcbList.get(pcb.parent);
            if (parentPCB.first_child == index) {
                parentPCB.first_child = pcb.younger_sibling;
            } else {
                int sibling = parentPCB.first_child;
                while (pcbList.get(sibling).younger_sibling != index) {
                    sibling = pcbList.get(sibling).younger_sibling;
                }
                pcbList.get(sibling).younger_sibling = pcb.younger_sibling;
            }
        }

        // Update the older_sibling of the younger sibling
        if (pcb.younger_sibling != -1) {
            pcbList.get(pcb.younger_sibling).older_sibling = pcb.older_sibling;
        }
    }

    public List<PCB> getPCBList() {
        return pcbList;
    }
}

public class Project012 {
    public static void main(String[] args) {
        ProcessManagement pm = new ProcessManagement();

        // Perform process creations and destructions
        pm.create(-1); // creates 1st child of PCB[0] at PCB[1]
        pm.create(0);  // creates 2nd child of PCB[0] at PCB[2]
        pm.create(1);  // creates 1st child of PCB[1] at PCB[3]
        pm.create(0);  // creates 3rd child of PCB[0] at PCB[4]
        pm.destroy(0); // destroys all descendents of PCB[0], which includes processes PCB[1] through PCB[4]

        // Print the remaining processes
        for (PCB pcb : pm.getPCBList()) {
            System.out.println("Process: parent=" + pcb.parent +
                    ", first_child=" + pcb.first_child +
                    ", younger_sibling=" + pcb.younger_sibling +
                    ", older_sibling=" + pcb.older_sibling);
        }
    }
}