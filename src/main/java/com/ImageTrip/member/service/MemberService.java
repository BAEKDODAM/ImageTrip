package com.ImageTrip.member.service;


import com.ImageTrip.auth.filter.JwtVerificationFilter;
import com.ImageTrip.auth.jwt.JwtTokenizer;
import com.ImageTrip.auth.utils.CustomAuthorityUtils;
import com.ImageTrip.exception.BusinessLogicException;
import com.ImageTrip.exception.ExceptionCode;
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


    public Member findMemberByToken(String token){
        long memberId = getMemberIdFromToken(token);
        Optional<Member> optionalMember = memberRepository.findById(memberId);

        Member findMember = optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        return findMember;
    }


    public void createMember(Member member){
        //이메일 중복확인
        verifyExistsEmail(member.getEmail());

        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setRoles(authorityUtils.createRoles(member.getEmail()));


        memberRepository.save(member);

    }


    public void updateName(String token, String newName){
        verifyExistsName(newName);

        Member member = findMemberByToken(token);
        member.setName(newName);

        memberRepository.save(member);

    }


    public void updatePassword(String token, String currentPassword, String newPassword){

        if(!currentPassword.equals(newPassword))
            new BusinessLogicException(ExceptionCode.SAME_PASSWORD);

        Member member = findMemberByToken(token);
        member.setPassword(passwordEncoder.encode(newPassword));

        memberRepository.save(member);
    }


    public void checkUserPassword(String token, String password) {
        Member member = findMemberByToken(token);
        
        if (!passwordEncoder.matches(password, member.getPassword()))
            throw new BusinessLogicException(ExceptionCode.UNMATCHED_PASSWORD);


    }



    public void verifyExistsEmail(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        System.out.println(optionalMember);
        if(optionalMember.isPresent()) throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);

    }

    public void verifyExistsName(String name) {
        Optional<Member> optionalMember = memberRepository.findByName(name);
        if(optionalMember.isPresent()) throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);

    }


    public void deleteMember(String token, String pw) {
        checkUserPassword(token, pw);

        long memberId =  getMemberIdFromToken(token);

        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member findMember = optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        memberRepository.delete(findMember);

    }

}
