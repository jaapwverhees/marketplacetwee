package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Visitor {

    @Id
    @Column(name = "EMAIL_ID",nullable = false,unique=true,columnDefinition="VARCHAR(64)")
    private String emailadress;

    private String firstname;

    private String lastname;

    private String password;

    public boolean valid() {
        return validEmail() &&
                validFirstname() &&
                validLastname() &&
                validPassword();
    }
    private boolean validFirstname(){
        return this.firstname != null && (this.firstname.length() >= 2 && this.firstname.length() <= 20);
    }
    private boolean validLastname(){
        return this.lastname != null && (this.lastname.length() >= 2 && this.lastname.length() <= 20);
    }

    private boolean validEmail(){
        return this.emailadress != null &&
                (this.emailadress.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$"));
    }

    private boolean validPassword(){
        return this.password != null && (this.password.length() >= 12 && this.password.length() <= 30);
    }
}
