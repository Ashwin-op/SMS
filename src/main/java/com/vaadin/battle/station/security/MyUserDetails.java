package com.vaadin.battle.station.security;

import com.vaadin.flow.component.notification.Notification;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MyUserDetails implements UserDetails {
    String url = "jdbc:mysql://localhost:3306/dbmsendsem";
    String user = "dbmsendsem";
    String pwd = "Password_123";

    private String userName;
    private String password;
    private boolean active;
    private int id;
    private int eid;
    private List<GrantedAuthority> authorities;

    public MyUserDetails(String userName){
        this.userName = userName;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            String sql = "select id,password,active,roles,eid from user where userName = '"+userName+"';";
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            this.id = rs.getInt("id");
            this.password = rs.getString("password");
            this.active = rs.getBoolean("active");
            this.eid = rs.getInt("eid");
            this.authorities = Arrays.stream(rs.getString("roles").split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
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
        return active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }
}
