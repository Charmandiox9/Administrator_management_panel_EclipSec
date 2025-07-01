package model;

import java.sql.Date;

public class Hiring {
    private int id;
    private int user_id;
    private int service_id;
    private Date application_date;
    private String commentary;
    private String status;

    public Hiring(int id, int user_id, int service_id, Date application_date, String commentary, String status) {
        this.id = id;
        this.user_id = user_id;
        this.service_id = service_id;
        this.application_date = application_date;
        this.commentary = commentary;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getService_id() {
        return service_id;
    }

    public Date getApplication_date() {
        return application_date;
    }

    public String getCommentary() {
        return commentary;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
