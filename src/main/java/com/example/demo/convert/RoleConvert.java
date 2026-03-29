package com.example.demo.convert;

import com.example.demo.dto.RoleCreateDTO;
import com.example.demo.dto.RoleUpdateDTO;
import com.example.demo.entity.SysRole;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleConvert {

    RoleConvert INSTANCE = Mappers.getMapper(RoleConvert.class);

    SysRole toEntity(RoleCreateDTO dto);

    void updateEntity(RoleUpdateDTO dto, @MappingTarget SysRole role);
}
