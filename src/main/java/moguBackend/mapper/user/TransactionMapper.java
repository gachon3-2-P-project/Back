package moguBackend.mapper.user;

import moguBackend.domain.user.ArticleEntity;
import moguBackend.domain.user.TransactionEntity;
import moguBackend.dto.user.ArticleDto;
import moguBackend.dto.user.TransactionDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface TransactionMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "article.id", target = "articleId")
    TransactionDto.TransactionResponseDto toResponseDto(TransactionEntity transactionEntity);
}
