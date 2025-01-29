package alen.blockchain;

import alen.blockchain.noobchain.Transaction;
import alen.blockchain.noobchain.Wallet;
import com.google.gson.GsonBuilder;
import java.util.Base64;
import java.security.Security;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class BlockChainApplication {

    public static int difficulty = 5;
    public static Wallet walletA;
    public static Wallet walletB;


    public static void main(String[] args) {
        SpringApplication.run(BlockChainApplication.class, args);
        System.out.println("\n");
        System.out.println("\n");

        //Setup Bouncey castle as a Security Provider
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        //Create the new wallets
        walletA = new Wallet();
        walletB = new Wallet();
        //Test public and private keys
        System.out.println("Private and public keys:");
        System.out.println(StringUtil.getStringFromKey(walletA.privateKey));
        System.out.println(StringUtil.getStringFromKey(walletA.publicKey));
        //Create a test transaction from WalletA to walletB
        Transaction transaction = new Transaction(walletA.publicKey, walletB.publicKey, 5, null);
        transaction.generateSignature(walletA.privateKey);
        //Verify the signature works and verify it from the public key
        System.out.println("Is signature verified");
        System.out.println(transaction.verifiySignature());



    }

}
