package alen.blockchain;

import java.util.ArrayList;
import java.util.Date;

import static alen.blockchain.BlockChainApplication.difficulty;

public class Block {
    public static ArrayList<Block> blockchain = new ArrayList<Block>();


    public String hash;
    public String previousHash;
    private String data;
    private long timeStamp;
    private int nonce;


    //block constructor
    public Block(String data, String previousHash){
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();

        this.hash = calculateHash();
    }

    public String calculateHash(){
        String calculatedhash = StringUtil.applySha256(
                previousHash + Long.toString(timeStamp) + Integer.toString(nonce) + data
        );
        return calculatedhash;
    }

    public void mineBlock(int difficulty)
    {
        //Create a string with difficulty '0'
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target))
        {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block Mined!!!: " + hash);
    }


    public static boolean isChainValid(){
        Block currentBlock;
        Block previusBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');


        //loop trough block chaing to check the hashes:
        for(int i=1;i<blockchain.size();i++)
        {
            currentBlock = blockchain.get(i);
            previusBlock = blockchain.get(i-1);

            //compare hash and calculated hash

            if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
                System.out.println("Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if(!previusBlock.hash.equals(currentBlock.previousHash) ) {
                System.out.println("Previous Hashes not equal");
                return false;
            }
            //check if hash is solved
            if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) {
                System.out.println("This block hasn't been mined");
                return false;
            }
        }
        return true;
    }


}
