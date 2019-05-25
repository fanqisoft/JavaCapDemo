package cn.coreqi.entities;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CoreqiMessage implements Serializable {
    private String messageId;
    private int code;
    private Object message;
    private String status;
}
