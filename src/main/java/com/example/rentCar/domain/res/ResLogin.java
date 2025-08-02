package com.example.rentCar.domain.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResLogin {
    private String accessToken;
    private ResUserLogin user;

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResUserLogin {
        private long id;
        private String email;
        private String fullName;
    }
}
