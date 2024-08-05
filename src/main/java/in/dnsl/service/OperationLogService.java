package in.dnsl.service;

import in.dnsl.model.entity.OperationLog;
import in.dnsl.repository.OperationLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class OperationLogService {

    private final OperationLogRepository repository;


    @Async
    @Transactional(rollbackFor = Exception.class)
    public CompletableFuture<OperationLog> saveLog(OperationLog log) {
       return CompletableFuture.completedFuture(repository.save(log));
    }
}
