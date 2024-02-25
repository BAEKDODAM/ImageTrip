package com.ImageTrip.member.controller;


import com.ImageTrip.member.dto.*;
import com.ImageTrip.member.entity.Member;
import com.ImageTrip.member.mapper.MemberMapper;
import com.ImageTrip.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
@Slf4j
@Api(tags = "MemberController")
@RestController
@RequestMapping("/user")
public class MemberController {
//회원 탈퇴, 프로필 이미지 수정
    private final MemberService memberService;
    private final MemberMapper memberMapper;

    public MemberController(MemberService memberService,
                            MemberMapper memberMapper){
        this.memberService = memberService;
        this.memberMapper = memberMapper;

    }

    @ApiOperation(value = "이메일 중복검사_회원가입_o")
    @PostMapping("/checkUseableEmail")
    public ResponseEntity checkUseableEmail(@RequestBody @Valid MemberDto.CheckEmailDto checkEmailDto){

        memberService.verifyExistsName(checkEmailDto.getEmail());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "이름 중복검사_회원가입(, 이름수정)_o")
    @PostMapping("/checkName")
    public ResponseEntity checUseablekName(@RequestBody @Valid MemberDto.CheckNameDto checkNameDto){
        // 유효성검증
//        CheckResponseDto checkResponseDto = new CheckResponseDto();
//        checkResponseDto.setCheck(memberService.checkName(checkNameDto.getName()));

        memberService.verifyExistsName(checkNameDto.getName());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //pw는 따로 요청없이 입력받는대로 프론트에서 유효성검증, 통과시 회원가입 요청

    @ApiOperation(value = "회원가입_o")
    @PostMapping("/joinIn")
    public ResponseEntity joinIn(@RequestBody @Valid MemberDto.CreateMemberDto createMemberDto){
        Member member = memberMapper.createMemberDtoToMember(createMemberDto);
        memberService.createMember(member);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation(value = "비밀번호 확인")
    @PostMapping("/checkPassword")
    public ResponseEntity checkPassword(@RequestHeader(value = "Authorization") String token,
                                        @RequestBody MemberDto.CheckPasswordDto checkPasswordDto){

        memberService.checkUserPassword(token, checkPasswordDto.getPassword());

        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "회원탈퇴")
    @PostMapping("/deleteAccount")
    public ResponseEntity deleteAccount(@RequestHeader(value = "Authorization") String token,
                                        @RequestBody MemberDto.CheckPasswordDto checkPasswordDto){

        //비밀번호 같으면
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "회원정보 확인")
    @GetMapping("/getAccount")
    public ResponseEntity getAccount(@RequestHeader(value = "Authorization") String token){

        Member findMember = memberService.findMemberByToken(token);
        MemberDto.GetAccountResponseDto getAccountResponseDto = memberMapper.memberToGetAccountResponseDto(findMember);
        return new ResponseEntity(getAccountResponseDto, HttpStatus.OK);
    }

    @ApiOperation(value = "이름 수정_o")
    @PatchMapping("/updateName")
    public ResponseEntity updateName(@RequestHeader(value = "Authorization") String token,
                                     @RequestBody MemberDto.UpdateNameDto updateNameDto){
        memberService.verifyExistsName(updateNameDto.getNewName());
        memberService.updateName(token, updateNameDto.getNewName());
        //중복검사 후 사용가능이면
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "비밀번호 수정_o")
    @PatchMapping("/updatePassword")
    public ResponseEntity updatePassword(@RequestHeader(value = "Authorization") String token,
                                         @RequestBody @Valid MemberDto.UpdatePasswordDto updatePasswordDto){

        memberService.checkUserPassword(token, updatePasswordDto.getCurrentPassword());
        memberService.updatePassword(token, updatePasswordDto.getCurrentPassword(), updatePasswordDto.getNewPassword());

        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "프로필 사진 수정")
    @PatchMapping(value = "updateImage", consumes = "multipart/form-data")
    public ResponseEntity updateImage(@RequestHeader(value = "Authorization") String token,
                                      @RequestPart MultipartFile image){

        return new ResponseEntity(HttpStatus.OK);
    }

}
