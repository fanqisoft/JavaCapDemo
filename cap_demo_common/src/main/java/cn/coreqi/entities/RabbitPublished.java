package cn.coreqi.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_rabbitpublished")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RabbitPublished implements Serializable {
    @javax.persistence.Id
    @Column(name = "Id")
    private String id;
    @Column(name = "Name")
    private String name;
    @Column(name = "Content")
    private String content;
    @Column(name = "Retries")
    private int retries;
    @Column(name = "Added")
    private LocalDateTime added;
    @Column(name = "LastModified")
    private LocalDateTime lastModified;
    @Column(name = "ExpiresAt")
    private LocalDateTime expiresAt;
    @Column(name = "StatusName")
    private String statusName;
}
