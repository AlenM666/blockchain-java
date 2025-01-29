package alen.blockchain.noobchain;


import alen.blockchain.BlockChainApplication;
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
    public static int sequence = 0;


    //constructor
    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.recepient = to;
        this.value = value;
        this.inputs = inputs;
    }

    private String calculateHash() {
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
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recepient) + Float.toString(value);
        signature = StringUtil.applyECDSASig(privateKey, data);
    }

    //Verifies the data we signed hasnt been tampered with
    public boolean verifiySignature() {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recepient) + Float.toString(value);
        return StringUtil.verifyECDSASig(sender, data, signature);
    }



    //true if transaction could be created
    public boolean processTransaction() {

        if(verifiySignature() == false) {
            System.out.println("#Transaction Signature failed to verify");
            return false;
        }

        //gather transaction inputs (Make sure they are unspent):
        for(TransactionInput i : inputs) {
            i.UTXO = BlockChainApplication.UTXOs.get(i.transactionOutputId);
        }

        //check if transaction is valid:
        if(getInputsValue() < BlockChainApplication.minimumTransaction) {
            System.out.println("#Transaction Inputs to small: " + getInputsValue());
            return false;
        }

        //generate transaction outputs:
        float leftOver = getInputsValue() - value; //get value of inputs then the left over change:
        transactionId = calculateHash();
        outputs.add(new TransactionOutput( this.recepient, value,transactionId)); //send value to recipient
        outputs.add(new TransactionOutput( this.sender, leftOver,transactionId)); //send the left over 'change' back to sender

        //add outputs to Unspent list
        for(TransactionOutput o : outputs) {
            BlockChainApplication.UTXOs.put(o.id , o);
        }

        //remove transaction inputs from UTXO lists as spent:
        for(TransactionInput i : inputs) {
            if(i.UTXO == null) continue; //if Transaction can't be found skip it
            BlockChainApplication.UTXOs.remove(i.UTXO.id);
        }

        return true;
    }

    //returns sum of inputs(UTXOs) values
    public float getInputsValue() {
        float total = 0;
        for(TransactionInput i : inputs) {
            if(i.UTXO == null) continue; //if Transaction can't be found skip it
            total += i.UTXO.value;
        }
        return total;
    }

    //returns sum of outputs:
    public float getOutputsValue() {
        float total = 0;
        for(TransactionOutput o : outputs) {
            total += o.value;
        }
        return total;
    }


}

