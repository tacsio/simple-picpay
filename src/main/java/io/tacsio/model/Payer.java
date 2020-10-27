package io.tacsio.model;

public interface Payer {
    boolean canPay();

    Wallet getWallet();
}
