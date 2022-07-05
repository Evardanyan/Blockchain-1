package blockchain;

import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
/*
need to check add message then get magic number
if its already genereated then we cant add new message if not we can send message
*
* */
class Block {
    Integer id;
    String minerId = "";
    String regulateN = "";
    long timeStamp;
    String previousHash;
    String hash;
    String magicNumber;
    long blockGenTime = 0;


    List<String> messages = new ArrayList<>();

    public void setMessages(List<String> messages) {
        this.messages = messages.stream().collect(Collectors.toList());
    }



    public void setMinerId() {
        this.minerId = minerId;
    }

    public void setRegulateN() {
        this.regulateN = regulateN;
    }

    public void setMessages(String msg) {
        messages.add(msg);
    }

    public List<String> getMessages() {
        return messages;
    }

    public long getBlockGenTime() {
        return blockGenTime;
    }
    public void setBlockGenTime(long blockGenTime) {
        this.blockGenTime = blockGenTime;
    }

    public String getMagicNumber() {
        return magicNumber;
    }
    public void setMagicNumber(String magicNumber) {
        this.magicNumber = magicNumber;
    }
    public Block() {}
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public long getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
    public String getPreviousHash() {
        return previousHash;
    }
    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }
    public String getHash() {
        return hash;
    }
    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public String toString() {
        return "Block{" +
                "id=" + id +
                ", minerId='" + minerId + '\'' +
                ", regulateN='" + regulateN + '\'' +
                ", timeStamp=" + timeStamp +
                ", previousHash='" + previousHash + '\'' +
                ", hash='" + hash + '\'' +
                ", magicNumber='" + magicNumber + '\'' +
                ", blockGenTime=" + blockGenTime +
                ", messages=" + messages +
                '}';
    }
}

class   Blockchain {
    static List<Block> blocks = new ArrayList<>();

    static int howManyZero = 0;
    static String tmpHowManyZero = "0".repeat(howManyZero);
    static String hashFromFields;
    static String prevHash = "0";
    static int tmpId = 0;
    static int tmpRandom = 0;
    static String tmpHash = "";
    static long tmpGenTime = 0;
    public static  List<String> tempMessages  = new ArrayList<>();

    public static synchronized List<String> getTempMessages() {
        return tempMessages;
    }

    public static  void addMessage(String message) {
        tempMessages.add(message);
    }

    public static synchronized  Block genBlockChain() {

        if (blocks.size() == 0) {
            long start = System.nanoTime();
            Block block = new Block();
            do {
                tmpRandom = new Random().nextInt(999999999);
                hashFromFields = new Date().getTime() + prevHash + tmpRandom + tmpRandom;
                tmpHash = StringUtil.applySha256(hashFromFields);
            } while (!tmpHash.startsWith(tmpHowManyZero));
            block.setId(tmpId);
            block.setMagicNumber(String.valueOf(tmpRandom));
            tmpId++;
            block.setTimeStamp(new Date().getTime());
            block.setPreviousHash(prevHash);
            block.setHash(tmpHash);
            prevHash = tmpHash;
            long end = System.nanoTime();
            tmpGenTime = (end - start) / 1_000_000_000;
            block.setBlockGenTime(tmpGenTime);
            return block;
        } else {
            Block block = new Block();
            long start = System.nanoTime();
            do {
                tmpRandom = new Random().nextInt(999999);
                hashFromFields = new Date().getTime() + prevHash + tmpRandom + tmpRandom;
                tmpHash = StringUtil.applySha256(hashFromFields);
            } while (!tmpHash.startsWith(tmpHowManyZero));

            block.setId(tmpId);
            block.setMagicNumber(String.valueOf(tmpRandom));
            tmpId++;
            block.setTimeStamp(new Date().getTime());
            block.setPreviousHash(prevHash);
            block.setHash(tmpHash);
            prevHash = tmpHash;
            long end = System.nanoTime();
            tmpGenTime = (end - start) / 1_000_000_000;
            block.setBlockGenTime(tmpGenTime);
            return block;
        }
    }


    public  boolean validateBlockChain(List<Block> blockList) {
        for (int i = 1; i < blockList.size(); i++) {
            if (blockList.get(i).previousHash == blockList.get(i - 1).hash) {
                return  true;
            }
        }
        return false;
    }
}



class StringUtil {
    /* Applies Sha256 to a string and returns a hash. */
    public static String applySha256(String input){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            /* Applies sha256 to our input */
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte elem: hash) {
                String hex = Integer.toHexString(0xff & elem);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {

        List<String> dialog = List.of(
                "Mike: Hey, Nick.",
                "Nick: Hey, Mike.",
                "Mike: How are you doing in Belarus?",
                "Nick: To be honest, not very good.",
                "Nick: President Lukashenko proved to be a hard nut to crack.",
                "Nick: They are professional and well organised.",
                "Mike: Yes. Sure."
        );


        Callable blockchainGen1 = () -> {
            long threadId = Thread.currentThread().getId();
            Block block = new Blockchain().genBlockChain();
            block.minerId = "Created by miner # " + threadId;


            if (block.blockGenTime < 5) {
                ++Blockchain.howManyZero;
                block.regulateN = "N was increased to " + Blockchain.howManyZero;
            } else if (block.blockGenTime > 60) {
                --Blockchain.howManyZero;
                block.regulateN = "N was decreased by " + Blockchain.howManyZero;
            } else if (block.blockGenTime > 5 && block.blockGenTime < 60){
                block.regulateN = "N stays the same";
            }

            return block;
        };


        Callable blockchainGen2 = () -> {
            long threadId = Thread.currentThread().getId();
            Block block = new Blockchain().genBlockChain();

            block.minerId = "Created by miner # " + threadId;
            if (block.blockGenTime < 5) {
                ++Blockchain.howManyZero;
                block.regulateN = "N was increased to " + Blockchain.howManyZero;
            } else if (block.blockGenTime > 60) {
                --Blockchain.howManyZero;
                block.regulateN = "N was decreased by " + Blockchain.howManyZero;
            } else if (block.blockGenTime > 5 && block.blockGenTime < 60){
                block.regulateN = "N stays the same";
            }

            return block;
        };

        Callable blockchainGen3 = () -> {
            long threadId = Thread.currentThread().getId();
            Block block = new Blockchain().genBlockChain();

            block.minerId = "Created by miner # " + threadId;
            if (block.blockGenTime < 5) {
                ++Blockchain.howManyZero;
                block.regulateN = "N was increased to " + Blockchain.howManyZero;
            } else if (block.blockGenTime > 60) {
                --Blockchain.howManyZero;
                block.regulateN = "N was decreased by " + Blockchain.howManyZero;
            } else if (block.blockGenTime > 5 && block.blockGenTime < 60){
                block.regulateN = "N stays the same";
            }

            return block;
        };

        Callable blockchainGen4 = () -> {
            long threadId = Thread.currentThread().getId();
            Block block = new Blockchain().genBlockChain();


            block.minerId = "Created by miner # " + threadId;
            if (block.blockGenTime < 5) {
                ++Blockchain.howManyZero;
                block.regulateN = "N was increased to " + Blockchain.howManyZero;
            } else if (block.blockGenTime > 60) {
                --Blockchain.howManyZero;
                block.regulateN = "N was decreased by " + Blockchain.howManyZero;
            } else if (block.blockGenTime > 5 && block.blockGenTime < 60){
                block.regulateN = "N stays the same";
            }
            return block;
        };


        Callable blockchainGen5 = () -> {
            long threadId = Thread.currentThread().getId();
            Block block = new Blockchain().genBlockChain();
//            System.out.println("is done 5");

            block.minerId = "Created by miner # " + threadId;
            if (block.blockGenTime < 5) {
                ++Blockchain.howManyZero;
                block.regulateN = "N was increased to " + Blockchain.howManyZero;
            } else if (block.blockGenTime > 60) {
                --Blockchain.howManyZero;
                block.regulateN = "N was decreased by " + Blockchain.howManyZero;
            } else if (block.blockGenTime > 5 && block.blockGenTime < 60){
                block.regulateN = "N stays the same";
            }
//            System.out.println("<><>< return 5");
            return block;
        };


        ExecutorService executorService = Executors.newFixedThreadPool(3);



        Future<Block> blockGen1 = executorService.submit(blockchainGen1);

        Future<Block> blockGen2 = executorService.submit(blockchainGen2);

        Future<Block> blockGen3 = executorService.submit(blockchainGen3);

        Future<Block> blockGen4 = executorService.submit(blockchainGen4);

        Future<Block> blockGen5 = executorService.submit(blockchainGen5);


        executorService.submit(() -> {

            for (String msg : dialog) {

                try {
                    Thread.sleep(1);
                    Blockchain.addMessage(msg);
//                    Thread.sleep(2);

                    if (blockGen2.get().messages.size() != 1) {
//                        System.out.println("><><><><>< " + blockGen2.get().getMagicNumber());
//                        System.out.println(Blockchain.getTempMessages());
//                        System.out.println("Block 2 before >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + blockGen2.get().messages.size());
                        blockGen2.get().setMessages(Blockchain.tempMessages);
//                        System.out.println("<<<<< 2" + blockGen2.get().getMessages());
                        Blockchain.tempMessages.removeAll(Blockchain.tempMessages);
//                        System.out.println(Blockchain.getTempMessages());
//                        System.out.println("block message is " + blockGen2.get().getMessages());
//                        System.out.println("Block 2 after >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + blockGen2.get().messages.size());
                    }

                    if (blockGen3.get().messages.size() != 1) {

//                        System.out.println("><><><><>< " + blockGen3.get().getMagicNumber());
                        blockGen3.get().setMessages(Blockchain.tempMessages);
//                        System.out.println("<<<<< 3" + blockGen3.get().getMessages());
                        Blockchain.tempMessages.removeAll(Blockchain.tempMessages);
//                        System.out.println(Blockchain.getTempMessages());
//                        System.out.println("block message is " + blockGen3.get().getMessages());
                    }

                    if (blockGen4.get().messages.size() != 1) {
                        blockGen4.get().setMessages(Blockchain.tempMessages);
//                        System.out.println(Blockchain.getTempMessages());
//                        System.out.println("<<<<< 4" + blockGen4.get().getMessages());
                        Blockchain.tempMessages.removeAll(Blockchain.tempMessages);
//                        System.out.println(Blockchain.getTempMessages());
//                        System.out.println("block message is " + blockGen4.get().getMessages());
                    }
                    if (blockGen5.get().messages.size() != 1) {
                        blockGen5.get().setMessages(Blockchain.tempMessages);;
//                        System.out.println(Blockchain.getTempMessages());
//                        System.out.println("<<<<< 5" + blockGen5.get().getMessages());
                        Blockchain.tempMessages.removeAll(Blockchain.tempMessages);
//                        System.out.println(Blockchain.getTempMessages());
//                        System.out.println("block message is " + blockGen5.get().getMessages());
                    }
                } catch (InterruptedException | ExecutionException ignored) {}
            }
        });

        executorService.shutdown();



        List<Block> blockList = new ArrayList<>();

        blockList.add(blockGen1.get());
        blockList.add(blockGen2.get());
        blockList.add(blockGen3.get());
        blockList.add(blockGen4.get());
        blockList.add(blockGen5.get());


        List<Block> sortedBlocks = blockList.stream().sorted(Comparator.comparing(Block::getId)).collect(Collectors.toList());


        for (int i = 0; i < sortedBlocks.size(); i++) {
            System.out.println("Block:");
            System.out.println(sortedBlocks.get(i).minerId);
            System.out.println("Id:" + "  " + sortedBlocks.get(i).getId());
            System.out.println("Timestamp:"  + sortedBlocks.get(i).getTimeStamp());
            System.out.println("Magic number:"  + sortedBlocks.get(i).getMagicNumber());
            System.out.println("Hash of the previous block:");
            System.out.println(sortedBlocks.get(i).getPreviousHash());
            System.out.println("Hash of the block:");
            System.out.println(sortedBlocks.get(i).getHash());
            if (sortedBlocks.get(i).getId() == 0) {
                System.out.println("Block data: no messages");
            } else {
                System.out.println("Block data:");
                sortedBlocks.get(i).messages.stream()
                        .forEach(System.out::println);
            }
            System.out.println("Block was generating for " + sortedBlocks.get(i).blockGenTime + " seconds");
            System.out.println(sortedBlocks.get(i).regulateN);
            System.out.println("\n");
        }
    }
}