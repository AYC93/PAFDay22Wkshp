package prac2.app.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class RSVP {
    private int id;
    private String name;
    private String email;
    private String phone;
    private DateTime Date;
    private String comments;

    public RSVP(int id, String name, String email, String phone, DateTime date, String comments) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        Date = date;
        this.comments = comments;
    }

    public RSVP() {
    }
    
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    
    public String getPhone() {return phone;}
    public void setPhone(String phone) {this.phone = phone;}
    
    public DateTime getDate() {return Date;}
    public void setDate(DateTime date) {Date = date;}
    
    public String getComments() {return comments;}
    public void setComments(String comments) {this.comments = comments;}

    @Override
    public String toString() {
        return "RSVP [id=" + id + ", name=" + name + ", email=" + email + ", phone=" + phone + ", Date=" + Date
                + ", comments=" + comments + "]";
    }

    public JsonObject toJson(){
        return Json.createObjectBuilder()
                    .add("ID", getId())
                    .add("name", getName())
                    .add("email", getEmail())
                    .add("phone", getPhone())
                    .add("confirmation_date", getDate().toString(DateTimeFormat.forPattern("dd-MM-yyyy")))
                    .add("comments", this.getComments())
                    .build();
    }

    public static RSVP create(SqlRowSet rs){
        // method to read line by line column in MySQL
        RSVP rsvp = new RSVP();
        rsvp.setId(rs.getInt("id")); 
        rsvp.setName(rs.getString("name")); 
        rsvp.setEmail(rs.getString("email"));
        rsvp.setPhone(rs.getString("phone")); 
        rsvp.setDate(new DateTime(Instant.parse(rs.getString("confirmation_date")))); 
        rsvp.setComments(rs.getString("comments")); 

        return rsvp;
    }

    public static RSVP create(String json){
        // method to read line by line column in MySQL
        RSVP rsvp = new RSVP();
        InputStream  is = new ByteArrayInputStream(json.getBytes());
        JsonReader r = Json.createReader(is);
        JsonObject o = r.readObject();
        rsvp.setName(o.getString("name")); 
        rsvp.setEmail(o.getString("email"));
        rsvp.setPhone(o.getString("phone")); 
        rsvp.setDate(getDateTime(o.getString("confirmation_date"))); 
        rsvp.setComments(o.getString("comments")); 

        return rsvp;
    }

    public static DateTime getDateTime(String date) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");
        DateTime dateTime = formatter.parseDateTime(date);
        return dateTime;
    }
}