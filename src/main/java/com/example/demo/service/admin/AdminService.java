package com.example.demo.service.admin;

import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.member.AdminAddRequestDto;
import com.example.demo.enums.ExceptionEnum;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.admin.AdminRepository;
import com.example.demo.util.responseUtil.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService implements IAdminService{

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<ResponseDto<Long>> addAdmin(AdminAddRequestDto adminAddRequestDto) {
        if(adminRepository.existsByEmail(adminAddRequestDto.getEmail())){
            throw new ApiException(ExceptionEnum.CONFLICT_EMAIL);
        }
        return ResponseUtil.successResponse("회원가입이 완료 되었습니다.",adminInsert(adminAddRequestDto));
    }

    private Long adminInsert(AdminAddRequestDto dto){
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        return adminRepository.save(dto.toAdmin()).getId();
    }
}
