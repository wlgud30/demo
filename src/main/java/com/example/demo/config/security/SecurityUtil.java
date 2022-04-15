package com.example.demo.config.security;

import com.example.demo.enums.ExceptionEnum;
import com.example.demo.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtil {

    public static CustomMemberDetails getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new ApiException(ExceptionEnum.NO_AUTHENTICATION_INFORMATION);
        }
        return (CustomMemberDetails) authentication.getPrincipal();
    }

//    public static Long getCurrentUserId(){
//        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if(authentication == null || authentication.getName() == null){
//            throw new ApiException(ExceptionEnum.NO_AUTHENTICATION_INFORMATION);
//        }
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        CustomMemberDetails memberDetails = (CustomMemberDetails) principal;
//        return memberDetails.getUserId();
//    }
}
