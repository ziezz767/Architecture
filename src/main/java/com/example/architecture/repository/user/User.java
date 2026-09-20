package com.example.architecture.repository.user;

import com.example.architecture.repository.BaseEntity;
import lombok.Getter;
import lombok.ToString;

@ToString(callSuper = true)
@Getter
public class User extends BaseEntity {
    private static int USER_CURRENT_ID = 0;
    private static int idGenerator() {
        return ++USER_CURRENT_ID;
    }

    private String name;

    private User(Integer id, String name, Integer userId) {
        super(id, userId);
        this.name = name;
    }

    // userId => 누가 유저를 생성했는지
    public static User create(String name, Integer userId) {
        int generatedId = idGenerator();
        return new User(
                generatedId,
                name,
                userId
        );
    }

}
