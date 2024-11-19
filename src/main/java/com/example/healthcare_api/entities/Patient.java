package com.example.healthcare_api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "patients")
@Getter
public class Patient implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private String gender;
    private Date birthday;
    private String phonenumber;
    private String address;
    private String city;
    private String role;

    @OneToMany(mappedBy = "patient")
    @JsonIgnore
    private List<Booking> bookings;


    public Patient setId(Long id) {
        this.id = id;
        return this;
    }

    public Patient setName(String name) {
        this.name = name;
        return this;
    }

    public Patient setEmail(String email) {
        this.email = email;
        return this;
    }

    public Patient setPassword(String password) {
        this.password = password;
        return this;
    }

    public Patient setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public Patient setBirthday(Date birthday) {
        this.birthday = birthday;
        return this;
    }

    public Patient setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
        return this;
    }

    public Patient setAddress(String address) {
        this.address = address;
        return this;
    }

    public Patient setCity(String city) {
        this.city = city;
        return this;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Patient setBookings(List<Booking> bookings) {
        this.bookings = bookings;
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        GrantedAuthority authority = new SimpleGrantedAuthority(getRole());
        List<GrantedAuthority> list = List.of(authority);
        return list;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
