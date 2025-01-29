package alen.blockchain;

import com.google.gson.GsonBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import static alen.blockchain.Block.blockchain;
import static alen.blockchain.Block.isChainValid;

@SpringBootApplication
public class BlockChainApplication {

    public static int difficulty = 5;


    public static void main(String[] args) {
        SpringApplication.run(BlockChainApplication.class, args);
        System.out.println("\n");
        System.out.println("\n");

        blockchain.add(new Block("Hi im the first block", "0"));
        System.out.println("Trying to Mine block 1... ");
        blockchain.get(0).mineBlock(difficulty);

        blockchain.add(new Block("Yo im the second block",blockchain.get(blockchain.size()-1).hash));
        System.out.println("Trying to Mine block 2... ");
        blockchain.get(1).mineBlock(difficulty);

        blockchain.add(new Block("Hey im the third block",blockchain.get(blockchain.size()-1).hash));
        System.out.println("Trying to Mine block 3... ");
        blockchain.get(2).mineBlock(difficulty);

        System.out.println("\nBlockchain is Valid: " + isChainValid());

        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println("\nThe block chain: ");
        System.out.println(blockchainJson);


    }

}
