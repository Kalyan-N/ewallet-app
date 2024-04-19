package com.pavan;

import lombok.*;
import org.hibernate.annotations.SecondaryRow;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionCreateService {
    private Integer senderUserId;
    private Integer receiverUserId;
    private Double amount;
    private String purpose;
    Transaction toTransaction(){
        return Transaction.builder()
                .senderId(senderUserId)
                .receiverId(receiverUserId)
                .amount(amount)
                .purpose(purpose)
                .build();
    }
}
