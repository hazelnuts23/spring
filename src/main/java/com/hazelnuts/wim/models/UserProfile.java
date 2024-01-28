package com.hazelnuts.wim.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import org.hibernate.annotations.ColumnTransformer;

import java.util.Date;

@Entity
@Table(name="user_profile")
public class UserProfile {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "id_no")
    private String idNo;

    @ColumnTransformer(
            read =  "pgp_sym_decrypt( ssn::bytea, current_setting('my.pii_secret'))",
            write = "pgp_sym_encrypt( ?, current_setting('my.pii_secret'))"
    )
    @Column(name = "ssn")
    private byte[] ssn;


//    @Column(name = "ssn_list", columnDefinition = "jsonb")
//    private String ssnList;

    @Column(name = "email")
    private String email;

    @Column(name = "dob")
    private Date dob;

    public Integer getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getIdNo() {
        return idNo;
    }
    public byte[] getSsn() {
        return ssn;
    }
    public String getEmail() {
        return email;
    }
    public Date getDob() {
        return dob;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name=name;
    }
    public void setPhoneNumber(String phone_number) {
        this.phoneNumber=phone_number;
    }
    public void setIdNo(String id_no) {
        this.idNo=id_no;
    }
    public void setSsn(byte[] ssn) {
        this.ssn=ssn;
    }
    public void setEmail(String email) {
        this.email=email;
    }
    public void setDob(Date dob) {
        this.dob=dob;
    }
}
