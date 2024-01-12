package com.ImageTrip.member.controller;


import com.ImageTrip.member.dto.*;
import com.ImageTrip.member.entity.Member;
import com.ImageTrip.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Api(tags = "MemberController", description = "로그인, 로그아웃, 예외처리 필요")
@RestController
@RequestMapping(value = "/user")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService){
        this.memberService = memberService;

    }

    @ApiOperation(value = "이메일 중복검사_회원가입")
    @PostMapping("/checkEmail")
    public ResponseEntity checkEmail(@RequestBody @Valid CheckEmailDto checkEmailDto){
        //유효성검증, 중복검사

        //findEmail(email);

        CheckResponseDto checkResponeDto = new CheckResponseDto();
        checkResponeDto.setCheck(true);//findEmail(email);

        return new ResponseEntity<>(checkResponeDto, HttpStatus.OK);
    }

    @ApiOperation(value = "이름 중복검사_회원가입(, 이름수정)")
    @PostMapping("/checkName")
    public ResponseEntity checkName(@RequestBody @Valid CheckNameDto checkNameDto){
        // 유효성검증, 중복검사

        //findName(name);

        CheckResponseDto checkResponseDto = new CheckResponseDto();
        checkResponseDto.setCheck(true);//findName(name);
        return new ResponseEntity<>(checkResponseDto, HttpStatus.OK);
    }

    //pw는 따로 요청없이 입력받는대로 프론트에서 유효성검증, 통과시 회원가입 요청

    @ApiOperation(value = "회원가입")
    @PostMapping("/joinIn")
    public ResponseEntity joinIn(@RequestBody @Valid CreateMemberDto createMemberDto) throws Exception{
        //email, name, pw, 유효성검증 필요
        Member member = new Member(createMemberDto.getEmail(), createMemberDto.getName(), createMemberDto.getPassword());

        memberService.createMember(member);

        return new ResponseEntity<>(HttpStatus.OK);
    }
//
//    @ApiOperation(value = "비밀번호 확인")
//    @PostMapping("/checkPassword")
//    public ResponseEntity checkPassword(@RequestHeader(value = "Authorization") String token,
//                                        @RequestBody CheckPasswordDto checkPasswordDto){
//
//        CheckResponseDto checkResponseDto = new CheckResponseDto();
//        checkResponseDto.setCheck(true); //checkPassword();
//        return new ResponseEntity(checkResponseDto, HttpStatus.OK);
//    }

    @ApiOperation(value = "회원탈퇴")
    @PostMapping("/deleteAccount")
    public ResponseEntity deleteAccount(@RequestHeader(value = "Authorization") String token,
                                        @RequestBody CheckPasswordDto checkPasswordDto){

        //비밀번호 같으면
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "회원정보 확인")
    @GetMapping("/getAccount")
    public ResponseEntity getAccount(@RequestHeader(value = "Authorization") String token){

        GetAccountResponseDto getAccountResponseDto = new GetAccountResponseDto();

        getAccountResponseDto.setEmail("exampleEmail");
        getAccountResponseDto.setName("exampleName_but_memberIdTest : " + memberService.getMemberIdFromToken(token));
        return new ResponseEntity(getAccountResponseDto, HttpStatus.OK);
    }

    @ApiOperation(value = "이름 수정")
    @PatchMapping("/updateName")
    public ResponseEntity updateName(@RequestHeader(value = "Authorization") String token,
                                     @RequestBody UpdateNameDto updateNameDto){

        //중복검사 후 사용가능이면
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "비밀번호 수정")
    @PatchMapping("/updatePassword")
    public ResponseEntity updatePassword(@RequestBody @Valid UpdatePasswordDto updatePasswordDto){

        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "프로필 사진 수정")
    @PatchMapping(value = "updateImage", consumes = "multipart/form-data")
    public ResponseEntity updateImage(@RequestHeader(value = "Authorization") String token,
                                      @RequestPart MultipartFile image){

        return new ResponseEntity(HttpStatus.OK);
    }

}
