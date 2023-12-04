package moguBackend.mapper.user;

import moguBackend.domain.user.ArticleEntity;
import moguBackend.domain.user.UserEntity;
import moguBackend.dto.user.ArticleDto;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ArticleMapper {

    /**
     * Entity -> Dto
     */
    @Mapping(source = "user.id", target = "userId")
    ArticleDto.ArticleResponseDto toResponseDto(ArticleEntity articleEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "userEntity")
    ArticleEntity toReqeustEntity(ArticleDto.ArticleRequestDto articleRequestDto, UserEntity userEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "user", ignore = true)
    })
    public void updateFromPatchDto(ArticleDto.ArticlePatchDto articlePatchDto, @MappingTarget ArticleEntity articleEntity);


}
