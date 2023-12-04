package moguBackend.mapper.user;

import moguBackend.domain.user.UserEntity;
import moguBackend.dto.user.UserDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface UserMapper {

    /**
     * Entity -> Dto
     */
    UserDto.UserResponseDto toResponseDto(UserEntity userEntity);

}
