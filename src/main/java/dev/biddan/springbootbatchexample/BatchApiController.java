package dev.biddan.springbootbatchexample;

import java.util.HashMap;
import java.util.Optional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch")
@RequiredArgsConstructor
public class BatchApiController {

    private final UserRepoMappingRepository userRepoMappingRepository;
    private final JobLauncher jobLauncher;
    private final Job commitAnalysisJob;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> start(@RequestBody StartRequest request) {
        Optional<UserRepoMapping> optionalUserRepoMapping = userRepoMappingRepository.findByUsername(request.username);
        if (optionalUserRepoMapping.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("사용자를 찾을 수 없습니다");
        }

        UserRepoMapping userRepoMapping = optionalUserRepoMapping.get();
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("username", userRepoMapping.username())
                .addString("repositoryName", userRepoMapping.repositoryName())
                .toJobParameters();

        try {
            jobLauncher.run(commitAnalysisJob, jobParameters);
        } catch (JobExecutionAlreadyRunningException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("이미 실행 중인 작업이 있습니다");
        } catch (JobInstanceAlreadyCompleteException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("이미 완료된 작업입니다");
        } catch (JobParametersInvalidException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("잘못된 작업 파라미터입니다: " + e.getMessage());
        } catch (JobRestartException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("작업을 재시작할 수 없습니다");
        }

        return ResponseEntity.ok("배치 작업이 성공적으로 시작되었습니다");
    }

    @Builder
    public record StartRequest(
            String username
    ) {

    }
}
