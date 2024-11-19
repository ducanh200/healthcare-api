package com.example.healthcare_api.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Entity
@Table(name = "doctors")
@Getter
public class Doctor implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private String thumbnail;
    private String phonenumber;
    private String role;


        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "department_id ")
        @JsonIgnoreProperties("doctors")
        private Department department;

    @JsonIgnore
    @OneToMany(mappedBy = "doctor")
    private List<Test> tests;

    @JsonIgnore
    @OneToMany(mappedBy = "doctor")
    private List<Result> results;


    public Doctor setId(Long id) {
        this.id = id;
        return this;
    }

    public Doctor setName(String name) {
        this.name = name;
        return this;
    }

    public Doctor setEmail(String email) {
        this.email = email;
        return this;
    }

    public Doctor setPassword(String password) {
        this.password = password;
        return this;
    }

    public Doctor setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    public Doctor setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
        return this;
    }

    public Doctor setDepartment(Department department) {
        this.department = department;
        return this;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Doctor setTests(List<Test> tests) {
        this.tests = tests;
        return this;
    }

    public Doctor setResults(List<Result> results) {
        this.results = results;
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
