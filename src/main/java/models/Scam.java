package models;

public class Scam {
    private String reference;
    private boolean scam;
    private String[] rules;

    public Scam(String reference, boolean scam, String[] rules) {
        this.reference = reference;
        this.scam = scam;
        this.rules = rules;
    }

    public Scam() {
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public boolean isScam() {
        return scam;
    }

    public void setScam(boolean scam) {
        this.scam = scam;
    }

    public String[] getRules() {
        return rules;
    }

    public void setRules(String[] rules) {
        this.rules = rules;
    }
}
