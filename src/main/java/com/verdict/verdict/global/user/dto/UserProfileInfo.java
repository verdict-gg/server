package com.verdict.verdict.global.user.dto;


import com.verdict.verdict.entity.User;

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
