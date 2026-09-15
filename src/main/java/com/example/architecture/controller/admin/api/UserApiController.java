package com.example.architecture.controller.admin.api;

import com.example.architecture.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/*
    < 쿠팡 내부 직원들이나 개발자 등이 상품이나 유저를 등록하고 삭제하기 위함 = 어드민 기능 >
    - 그 중에서 실제 작동하는 웹 페이지에서 관리자 계정으로 로그인 시, "User" 를 관리하기 위해서 만든 컨트롤러
*/

@RestController
@RequiredArgsConstructor
public class UserApiController {
    private final UserRepository userRepository;
}
