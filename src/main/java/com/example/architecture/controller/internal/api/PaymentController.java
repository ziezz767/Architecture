package com.example.architecture.controller.internal.api;

import com.example.architecture.application.PaymentApplication;
import com.example.architecture.controller.internal.api.dto.PaymentCreateRequestDto;
import com.example.architecture.controller.internal.api.dto.PaymentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/*
    < 실제 고객의 구매와 구매 취소 기능 / 버튼에 대한 API 제공 >
    - 고객이 어떤 상품들을 구매할지 보내면 그 고객에게 해당 상품의 구매 정보를 생성
    - 고객이 기존에 구매했던 구매 건을 취소하는 경우 - Hard Delete 가 아닌 Soft Delete 상태로 변경
*/

@RestController
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentApplication paymentApplication;

    // 결제
    @RequestMapping(method = RequestMethod.POST, value = "/internal/api/payments")
    public PaymentResponseDto payment(@RequestBody PaymentCreateRequestDto request) {
        Integer requestedUserId = request.getRequestUserId();
        List<Integer> productIds = request.getProductIds();

        return paymentApplication.payment(productIds, requestedUserId);
    }

    // 결제 취소
    @RequestMapping(method = RequestMethod.PATCH, value = "/internal/api/payments/{id}/cancel")
    public PaymentResponseDto cancel(@PathVariable Integer id, @RequestBody PaymentCreateRequestDto request) {
        Integer requestedUserId = request.getRequestUserId();

        return paymentApplication.cancel(id, requestedUserId);
    }

}
