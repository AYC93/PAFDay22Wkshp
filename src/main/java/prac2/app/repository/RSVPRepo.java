package prac2.app.repository;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import prac2.app.model.RSVP;
import static prac2.app.repository.DBQueries.*; // needs to write static here

@Repository
public class RSVPRepo {
    
    @Autowired
    JdbcTemplate template;

    public List<RSVP> listAllRSVP(){

        List<RSVP> result = new ArrayList<>();
        
        SqlRowSet rs = template.queryForRowSet(LIST_ALL_RSVP);

        while (rs.next())
            result.add(RSVP.create(rs));

        return result;
        
    }

    public List<RSVP> checkNameInRSVP(String name){

        List<RSVP> result = new ArrayList<>();
        
        SqlRowSet rs = template.queryForRowSet(FIND_RSVP_OF_NAME, "%" + name + "%");

        while (rs.next())
            result.add(RSVP.create(rs));
        return result;
        
    }

    public RSVP checkEmailInRSVP(String email){
        RSVP result = null;
        SqlRowSet rs = template.queryForRowSet(FIND_RSVP_OF_EMAIL, "%" + email + "%");
        while (rs.next())
           result = RSVP.create(rs);
        return result;
    }

    public RSVP newRSVP(RSVP rsvp){
        KeyHolder keyHolder = new GeneratedKeyHolder();
        RSVP checkRSVP = checkEmailInRSVP(rsvp.getEmail());
        if (checkRSVP ==null){
            // insert record
            template.update(conn -> {
            PreparedStatement pStatement = conn.prepareStatement(INSERT_NEW_RSVP, Statement.RETURN_GENERATED_KEYS);
                pStatement.setString(1, rsvp.getName());
                pStatement.setString(2, rsvp.getEmail());
                pStatement.setString(3, rsvp.getPhone());
                pStatement.setTimestamp(4, new Timestamp(rsvp.getDate().toDateTime().getMillis()));
                pStatement.setString(5, rsvp.getComments());
                
                return pStatement;
            }, keyHolder);
            BigInteger primaryKey = (BigInteger) keyHolder.getKey();

            rsvp.setId(primaryKey.intValue());     
        }else {
            checkRSVP.setName(rsvp.getName());
            checkRSVP.setEmail(rsvp.getEmail());
            checkRSVP.setPhone(rsvp.getPhone());
            checkRSVP.setDate(rsvp.getDate());
            checkRSVP.setComments(rsvp.getComments());
            template.update(UPDATE_EXISTING_RSVP, 
                        checkRSVP.getName(),
                        checkRSVP.getEmail(),
                        checkRSVP.getPhone(),
                        new Timestamp(rsvp.getDate().toDateTime().getMillis()),
                        checkRSVP.getComments()                  
                        );
            checkRSVP.setId(checkRSVP.getId());
        }
        return rsvp;
    }

    public RSVP updateRSVP(RSVP rsvp){
        RSVP updateRSVP = checkEmailInRSVP(rsvp.getEmail());
        if(updateRSVP!=null){
            updateRSVP.setName(rsvp.getName());
            updateRSVP.setPhone(rsvp.getPhone());
            updateRSVP.setDate(rsvp.getDate());
            updateRSVP.setComments(rsvp.getComments());
       template.update(UPDATE_EXISTING_RSVP, 
                                updateRSVP.getName(),
                                updateRSVP.getPhone(),
                                new Timestamp(rsvp.getDate().toDateTime().getMillis()),
                                updateRSVP.getComments(),
                                updateRSVP.getEmail());
        }
        return updateRSVP;
    }

    public int countRSVP(RSVP rsvp){
        int num;
        num = template.queryForObject(COUNT_ALL_RSVP, Integer.class);
        return num;
    }
}