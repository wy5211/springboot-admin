package com.example.demo.convert;

import com.example.demo.dto.RoleCreateDTO;
import com.example.demo.dto.RoleUpdateDTO;
import com.example.demo.entity.SysRole;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-29T11:35:59+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.18 (Homebrew)"
)
public class RoleConvertImpl implements RoleConvert {

    @Override
    public SysRole toEntity(RoleCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        SysRole sysRole = new SysRole();

        sysRole.setRoleName( dto.getRoleName() );
        sysRole.setRoleCode( dto.getRoleCode() );
        sysRole.setSortOrder( dto.getSortOrder() );
        sysRole.setStatus( dto.getStatus() );
        sysRole.setRemark( dto.getRemark() );

        return sysRole;
    }

    @Override
    public void updateEntity(RoleUpdateDTO dto, SysRole role) {
        if ( dto == null ) {
            return;
        }

        role.setRoleName( dto.getRoleName() );
        role.setRoleCode( dto.getRoleCode() );
        role.setSortOrder( dto.getSortOrder() );
        role.setStatus( dto.getStatus() );
        role.setRemark( dto.getRemark() );
    }
}
