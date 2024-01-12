package com.ImageTrip.member.mapper;

import com.ImageTrip.member.dto.CreateMemberDto;
import com.ImageTrip.member.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member createMemberDtoToMember(CreateMemberDto createMemberDto);
}
