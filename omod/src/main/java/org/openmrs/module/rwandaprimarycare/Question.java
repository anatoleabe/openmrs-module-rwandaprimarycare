package org.openmrs.module.rwandaprimarycare;

import org.openmrs.Concept;

public class Question {

        private String label;
        private Concept concept;
        private boolean required;
        
        public Question() { }

        public Question(String label, Concept concept, boolean required) {
            this.label = label;
            this.concept = concept;
            this.required = required;
        }
        
        public String getLabel() {
            return label;
        }
        
        public void setLabel(String label) {
            this.label = label;
        }
        
        public Concept getConcept() {
            return concept;
        }
        
        public void setConcept(Concept concept) {
            this.concept = concept;
        }
        
        public boolean isRequired() {
            return required;
        }
        
        public void setRequired(boolean required) {
            this.required = required;
        }
    
}
