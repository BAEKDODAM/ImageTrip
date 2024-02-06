package com.ImageTrip.member.mapper;
import com.ImageTrip.member.dto.MemberDto;
import com.ImageTrip.member.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member createMemberDtoToMember(MemberDto.CreateMemberDto createMemberDto);

    MemberDto.GetAccountResponseDto memberToGetAccountResponseDto(Member member);
}
