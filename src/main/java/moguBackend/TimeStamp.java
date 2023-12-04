package moguBackend;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) //자동 시간 매핑
public abstract class TimeStamp {


    @CreatedDate
    private LocalTime createdAt;


    @PrePersist
    public void prePersist() {
        LocalTime now = LocalTime.now();
        createdAt = now;
    }



}