package com.workers.wsusermanagement.bussines.service.confirmotp.service;

import com.workers.wsusermanagement.bussines.service.confirmotp.context.ConfirmationOtpContext;
import com.workers.wsusermanagement.bussines.service.confirmotp.interfaces.ConfirmationOtpService;
import com.workers.wsusermanagement.bussines.service.resetpass.model.ResetPasswordResponse;
import com.workers.wsusermanagement.persistence.entity.OtpEntity;
import com.workers.wsusermanagement.persistence.enums.StatusOtp;
import com.workers.wsusermanagement.persistence.repository.OtpEntityRepository;
import com.workers.wsusermanagement.persistence.repository.UserProfileRepository;
import com.workers.wsusermanagement.rest.outbound.process.reset.interfaces.ResetPasswordAfterConfirmOtpProcessClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;

import static com.workers.wsusermanagement.util.CommonConstant.UNEXPECTED_ERROR_MESSAGE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfirmationOtpServiceImpl implements ConfirmationOtpService {

    private final ResetPasswordAfterConfirmOtpProcessClient resetPasswordAfterConfirmOtpProcessClient;
    private final UserProfileRepository userProfileRepository;
    private final OtpEntityRepository otpEntityRepository;
    private static final Integer TTL_OTP_MINS = 3;

    @Override
    public ResetPasswordResponse doProcess(ConfirmationOtpContext ctx) {
        return Optional.of(ctx)
                .map(this::findOtpByUuid)
                .map(this::validateDeclineStatusOtp)
                .map(this::validateTTL)
                .map(this::findUserProfile)
                .map(this::compareOtp)

                .map(resetPasswordAfterConfirmOtpProcessClient::requestToExecuteByService)

                .map(this::updateVisitDate)
                .map(this::createResetPasswordResponse)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, UNEXPECTED_ERROR_MESSAGE));
    }

    private ConfirmationOtpContext findOtpByUuid(ConfirmationOtpContext ctx) {
        var otpEntity = otpEntityRepository.findAllByUuid(ctx.getRequest().uuid())
                .stream()
                .max(Comparator.comparing(OtpEntity::getCreatedAt))
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Для вашего запроса мы не нашли одноразовый пароль"));

        ctx.setOtpEntity(otpEntity);
        return ctx;
    }

    private ConfirmationOtpContext validateDeclineStatusOtp(ConfirmationOtpContext ctx) {
        if (StatusOtp.DECLINED.equals(ctx.getOtpEntity().getStatusOtp())) {
            throw new ResponseStatusException(BAD_REQUEST, "Одноразовый пароль отклонен");
        }
        return ctx;
    }

    private ConfirmationOtpContext validateTTL(ConfirmationOtpContext ctx) {
        LocalDateTime createdAt = ctx.getOtpEntity().getCreatedAt();
        LocalDateTime expiresAt = createdAt.plusMinutes(TTL_OTP_MINS);

        if (StatusOtp.EXPIRED.equals(ctx.getOtpEntity().getStatusOtp())
                || LocalDateTime.now().isAfter(expiresAt)) {
            ctx.getOtpEntity().setStatusOtp(StatusOtp.EXPIRED);
            throw new ResponseStatusException(BAD_REQUEST, "Одноразовый пароль устарел");
        }
        return ctx;
    }

    private ConfirmationOtpContext findUserProfile(ConfirmationOtpContext ctx) {
        var userProfile = userProfileRepository.findByUsername(ctx.getOtpEntity().getUsername())
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Пользователь в системе не существует"));
        ctx.setUserProfile(userProfile);
        return ctx;
    }

    private ConfirmationOtpContext compareOtp(ConfirmationOtpContext ctx) {
        String actualOtp = ctx.getOtpEntity().getOtp();
        String enteredOtp = ctx.getRequest().otp();

        if (actualOtp.equals(enteredOtp)) {
            ctx.getOtpEntity().setStatusOtp(StatusOtp.APPROVED);
            return ctx;
        }

        throw new ResponseStatusException(BAD_REQUEST, "Отп не верный");
    }

    private ConfirmationOtpContext updateVisitDate(ConfirmationOtpContext ctx) {
        var userProfile = ctx.getUserProfile();
        userProfile.setLastVisitAt(LocalDateTime.now());
        userProfileRepository.save(userProfile);
        return ctx;
    }

    private ResetPasswordResponse createResetPasswordResponse(ConfirmationOtpContext ctx) {
        return new ResetPasswordResponse(ctx.getOtpEntity().getUuid());
    }
}
