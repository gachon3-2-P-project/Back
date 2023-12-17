package moguBackend.domain.mapper;

import moguBackend.domain.entity.ArticleEntity;
import moguBackend.domain.entity.MessageEntity;
import moguBackend.domain.entity.UserEntity;
import moguBackend.domain.dto.ArticleDto;
import moguBackend.domain.dto.MessageDto;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

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
    @Mapping(source = "user.nickName", target = "nickName")
    @Mapping(source = "messages", target = "messages")
    ArticleDto.ArticleResponseDto toResponseDto(ArticleEntity articleEntity);

    @Mapping(source = "user.id", target = "userId")
    //@Mapping(source = "user.nickName", target = "nickName")
    @Mapping(source = "article.id", target = "articleId")
    MessageDto.MessageResponseDto toMessageResponseDto(MessageEntity messageEntity);


    //toResponseDto 메서드에서 messages 필드를 처리할 때 사용
    default List<MessageDto.MessageResponseDto> mapMessageEntitiesToDto(List<MessageEntity> messages) {
        if (messages == null) {
            return null;
        }
        return messages.stream()
                .map(this::toMessageResponseDto)
                .collect(Collectors.toList());
    }




    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "userEntity")
    @Mapping(target = "messages", ignore = true)
    @Mapping(target="transactionStatus", ignore = true)
//    @Mapping(target="depositNumber", ignore = true)
//    @Mapping(target = "transactionNumber", ignore = true)
    ArticleEntity toReqeustEntity(ArticleDto.ArticleRequestDto articleRequestDto, UserEntity userEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "user", ignore = true),
            @Mapping(target = "messages", ignore = true)

    })
    public void updateFromPatchDto(ArticleDto.ArticlePatchDto articlePatchDto, @MappingTarget ArticleEntity articleEntity);



}
