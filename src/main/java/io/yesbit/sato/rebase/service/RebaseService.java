package io.yesbit.sato.rebase.service;

import io.yesbit.sato.rebase.vo.Transaction;

import java.math.BigDecimal;
import java.math.BigInteger;

public class RebaseService {

    public Transaction pushReport(String asset, String fromAddress, BigInteger nonce, String password, String keyfile, String toAddress, BigDecimal amount) throws Exception {
        Transaction transaction = new Transaction();
        return transaction;
    }

    public Transaction rebase(String asset, String fromAddress, BigInteger nonce, String password, String keyfile, String toAddress, BigDecimal amount) throws Exception {
        Transaction transaction = new Transaction();
        return transaction;
    }

}
