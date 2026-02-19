package com.arij.project.role;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.arij.project.common.BaseEntity;
import com.arij.project.user.User;

import javax.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "ROLES")
@EntityListeners(AuditingEntityListener.class)
public class Role extends BaseEntity {
    private String name ;
    @ManyToMany(mappedBy = "roles")
    private List<User> users;
    
}
