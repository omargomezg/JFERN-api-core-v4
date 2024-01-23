package com.southpurity.apicore.persistence.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import com.southpurity.apicore.persistence.model.constant.RoleEnum;
import com.southpurity.apicore.persistence.model.constant.UserStatusEnum;
import com.southpurity.apicore.persistence.model.saleorder.SaleOrderDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Document("user")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDocument implements UserDetails {

    @Builder.Default
    private List<AddressDocument> addresses = new ArrayList<>();

    @Id
    @JsonView({View.Customer.class, View.Stocker.class})
    private String id;

    @JsonView({View.Customer.class, View.Stocker.class})
    private String telephone;

    @JsonView({View.Customer.class, View.Stocker.class})
    private RoleEnum role;

    @Indexed(unique = true)
    @JsonView({View.Customer.class, View.Stocker.class})
    private String rut;

    @Email(message = "Email should be valid")
    @JsonView({View.Customer.class, View.Stocker.class})
    @Indexed(unique = true)
    private String email;

    @JsonView({View.Customer.class, View.Stocker.class})
    private String city;

    @JsonView({View.Customer.class, View.Stocker.class})
    private String address;

    private String password;

    @JsonView({View.Customer.class, View.Stocker.class})
    private String fullName;

    @Builder.Default
    @JsonView({View.Administrator.class})
    private UserStatusEnum status = UserStatusEnum.ACTIVE;

    @CreatedDate
    private Date createdDate;

    @JsonView({View.Customer.class, View.Stocker.class})
    @LastModifiedDate
    private Date updatedDate;

    @JsonView(View.Anonymous.class)
    private PasswordReset passwordReset;

    private String placeId;

    @DocumentReference(lazy = true)
    private transient List<SaleOrderDocument> saleOrders;

    @Transient
    @JsonView(View.Customer.class)
    private PlaceDocument place;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(role.name()));
        return roles;
    }

    @Override
    public String getUsername() {
        return this.email;
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
