package com.ImageTrip.member.service;


import com.ImageTrip.auth.filter.JwtVerificationFilter;
import com.ImageTrip.auth.jwt.JwtTokenizer;
import com.ImageTrip.auth.utils.CustomAuthorityUtils;
import com.ImageTrip.member.entity.Member;
import com.ImageTrip.member.repository.MemberRepository;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Transactional
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
     private final CustomAuthorityUtils authorityUtils;

     private final JwtTokenizer jwtTokenizer;

    public MemberService(MemberRepository memberRepository,
                         PasswordEncoder passwordEncoder,
                         CustomAuthorityUtils authorityUtils,
                         JwtTokenizer jwtTokenizer
                         ) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityUtils = authorityUtils;
        this.jwtTokenizer = jwtTokenizer;
    }

    public long getMemberIdFromToken(String token) {
        String jws = token.replace("Bearer ", "");
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        Map<String, Object> claims = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody();

        //Object memberIdObject = claims.get("memberId");

        return ((Integer) claims.get("memberId")).longValue();
    }


    public void createMember(Member member) throws Exception{
        //이메일 중복확인
        verifyExistsEmail(member.getEmail());

        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setRoles(authorityUtils.createRoles(member.getEmail()));


        memberRepository.save(member);

    }

    public void verifyExistsEmail(String email) throws Exception{
        Optional<Member> member = memberRepository.findByEmail(email);
        if(member.isPresent()){
            throw new Exception("Member Exists");
        }

    }


}
