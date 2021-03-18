package io.yesbit.sato.rebase.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
public class Transaction {
    BigDecimal fee;
    String txHash = null;
    Long height;
    String expiration;
    BigInteger nonce;
}
