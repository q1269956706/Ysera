package com.example.evilsay.wechatson.Model;

public class LessonResult {
    private String status;
    private String user;
    private String name;
    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

    public void setUser(String user) {
        this.user = user;
    }
    public String getUser() {
        return user;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    @Override
    public String toString() {
        return "LessonResult{" +
                "status='" + status + '\'' +
                ", user='" + user + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
