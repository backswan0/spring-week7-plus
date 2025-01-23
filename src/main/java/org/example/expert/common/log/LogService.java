package org.example.expert.common.log;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveLog(String message) {

        // todo: 휴먼 에러가 줄어들도록 "매니저 등록 실패" 메시지 입력을 여기서 처리하자
        Log log = new Log(message);

        logRepository.save(log);
    }
}