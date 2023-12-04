package moguBackend.mapper.admin;

import moguBackend.domain.admin.AdminEntity;
import moguBackend.dto.admin.AdminDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface AdminMapper {

    /**
     * Entity -> Dto
     */
    AdminDto.AdminResponseDto toResponseDto(AdminEntity adminEntity);
}
