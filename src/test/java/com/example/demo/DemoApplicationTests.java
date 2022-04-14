package com.example.demo;

import com.example.demo.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
@RequiredArgsConstructor
class DemoApplicationTests {

	private final MemberService memberService;

	@Test
	void contextLoads() {
		String a = "Test!!";

		log.info(a);
		System.out.println(a);

	}

}
