package org.HEI.kdot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import static org.HEI.kdot.UserStatus.*;

@SuperBuilder
@Data
@AllArgsConstructor
public abstract class User {
    private String id;
    private String ref;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private UserStatus status;

    public void enable() {
        setStatus(ENABLED);
    }

    public void disable() {
        setStatus(DISABLED);
    }
}
