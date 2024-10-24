package app.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreationUserRequest {
    private String name;
    private String document;
    private String cellphone;
    private String username;
    private String password;

}
