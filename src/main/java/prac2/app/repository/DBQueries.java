package prac2.app.repository;

public class DBQueries {
    public static final String LIST_ALL_RSVP="select * from rsvp";

    public static final String FIND_RSVP_OF_NAME="select * from rsvp where name like ?";

    public static final String FIND_RSVP_OF_EMAIL="select * from rsvp where email like ?";

    public static final String INSERT_NEW_RSVP="INSERT INTO rsvp (name, email, phone, confirmation_date, comments) VALUES (?, ?, ?, ?, ?)";

    public static final String UPDATE_EXISTING_RSVP="update rsvp set name = ?, phone = ?, confirmation_date = ?, comments = ? where email = ?";

    public static final String COUNT_ALL_RSVP="select count(*) as total_count from rsvp";
}
