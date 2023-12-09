package moguBackend.domain.mapper;

import moguBackend.domain.entity.TransactionEntity;
import moguBackend.domain.dto.TransactionDto;
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
