package org.openmrs.module.rwandaprimarycare;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.openmrs.Patient;

public class RecentlyViewedPatients {

    private int capacity;    
    private LinkedList<Patient> stack;
    
    public RecentlyViewedPatients(int capacity) {
        if (capacity <= 0)
            throw new IllegalArgumentException("capacity must be > 0");
        this.capacity = capacity;
        stack = new LinkedList<Patient>();
    }
    
    /**
     * Puts this patient is at the top of the stack. (If the patient is already in the stack
     * they are moved to the top. If not, and the stack is at capacity, the bottom element is
     * dropped 
     * 
     * @param patient
     */
    public void nowViewingPatient(Patient patient) {
        if (stack.size() > 0 && stack.getFirst().equals(patient)) {
            return;
        }
        for (Iterator<Patient> iter = stack.iterator(); iter.hasNext(); ) {
            Patient p = iter.next();
            if (p.equals(patient))
                iter.remove();
        }
        stack.addFirst(patient);
        while (stack.size() > capacity)
            stack.removeLast();
    }
    
    public List<Patient> getList() {
        return Collections.unmodifiableList(stack);
    }
    
}
