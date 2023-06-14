package com.example.demo.service.admin;

import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.member.AdminAddRequestDto;
import org.springframework.http.ResponseEntity;

public interface IAdminService {
    ResponseEntity<ResponseDto<Long>> addAdmin(AdminAddRequestDto adminAddRequestDto);
}
