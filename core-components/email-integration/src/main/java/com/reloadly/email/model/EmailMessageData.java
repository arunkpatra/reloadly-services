package com.reloadly.email.model;

public class EmailMessageData {

    private String[] toList = new String[]{};
    private String from;
    private String subject;
    private String text;
    private boolean html;

    public boolean isHtml() {
        return html;
    }

    public void setHtml(boolean html) {
        this.html = html;
    }

    public String[] getToList() {
        return toList;
    }

    public void setToList(String[] toList) {
        this.toList = toList;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
