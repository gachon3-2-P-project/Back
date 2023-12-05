package moguBackend.mapper.user;

import moguBackend.domain.user.UserEntity;
import moguBackend.dto.user.UserDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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

    /**
     * Dto -> Entity
     */
    @Mapping(target="id", ignore = true)
    @Mapping(target="role", ignore = true)
    UserEntity toRequestEntity(UserDto.UserRequestDto userRequestDto);



}
