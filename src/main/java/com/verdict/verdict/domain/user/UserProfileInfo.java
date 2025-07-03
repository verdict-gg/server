package com.verdict.verdict.domain.user;


import com.verdict.verdict.domain.user.entity.User;

public record UserProfileInfo(

        Long userId
//        String role
//        String email,
//        String nickname,
//        String information,
//        String imageUrl

) {
    public static UserProfileInfo createByEntity(User user) {
        return new UserProfileInfo(user.getId());
    }
}
