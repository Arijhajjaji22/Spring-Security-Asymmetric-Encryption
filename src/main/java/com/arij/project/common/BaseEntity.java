package com.arij.project.common;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private UUID id;

    @CreatedDate
    @Column(name = "CREATED_DATE", updatable =false , nullable =false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DATE" , insertable = false)
    private LocalDateTime lastModifiedDate;
    
    @CreatedBy
    @Column(name = "CREATED_BY" , nullable = false ,updatable = false)
    private String createdBy;

    @LastModifiedDate
    @Column(name= "LAST_MODIFIED_BY", nullable = false)
    private String lastModifiedBy;
    
}
 