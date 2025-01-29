package alen.blockchain.noobchain;


import alen.blockchain.StringUtil;
import org.springframework.util.StringUtils;

import java.security.*;
import java.util.ArrayList;

public class Transaction {
    public String transactionId;
    public PublicKey sender;
    public PublicKey recepient;
    public float value;
    //to secure the funds so nobady else can use them
    public byte[] signature;

    public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    //to count how meany transaqctions have been
    public static int sequence =0;


    //constructor
    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs)
    {
        this.sender = from;
        this.recepient = to;
        this.value = value;
        this.inputs = inputs;
    }

    private String calculateHash()
    {
        // increse the sequence to avoid 2 identical transactions
        sequence++;
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(sender) +
                        StringUtil.getStringFromKey(recepient) +
                        Float.toString(value) + sequence
        );

    }

    //Signs all the data we dont wish to be tampered with.
    public void generateSignature(PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recepient) + Float.toString(value)	;
        signature = StringUtil.applyECDSASig(privateKey,data);
    }
    //Verifies the data we signed hasnt been tampered with
    public boolean verifiySignature() {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recepient) + Float.toString(value)	;
        return StringUtil.verifyECDSASig(sender, data, signature);
    }

}

