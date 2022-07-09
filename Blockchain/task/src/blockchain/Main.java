package blockchain;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
/*
need to check add message then get magic number
if its already genereated then we cant add new message if not we can send message
*
* */

class Block {
    Integer id;
    String minerId = "";
    String minerIdCoin = "";
    int minerCoin = 0;
    String regulateN = "";
    long timeStamp;
    String previousHash;
    String hash;
    String magicNumber;
    long blockGenTime = 0;

    List<String> messages = new ArrayList<>();

    List<byte[]> signedMsg = new ArrayList<>();

    void setSignedMsg(List<byte[]> signedMsg) {
        this.signedMsg = signedMsg.stream().collect(Collectors.toList());
    }

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

    public Block() {
    }

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
                ", minerId='" + minerIdCoin + '\'' +
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

class Blockchain {
    static List<Block> blocks = new ArrayList<>();

    static int howManyZero = 0;
    static String tmpHowManyZero = "0".repeat(howManyZero);
    static String hashFromFields;
    static String prevHash = "0";
    static int tmpId = 0;
    static int tmpRandom = 0;
    static String tmpHash = "";
    static long tmpGenTime = 0;
    public static List<String> tempMessages = new ArrayList<>();

    public static List<byte[]> signedMessages = new ArrayList<>();


    public static synchronized List<String> getTempMessages() {
        return tempMessages;
    }

    public static void addMessage(String message) {
        tempMessages.add(message);
    }

    public static void addMessageSign(byte[] msg) {
        signedMessages.add(msg);
    }

    public static synchronized Block genBlockChain() {

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


    public boolean validateBlockChain(List<Block> blockList) {
        for (int i = 1; i < blockList.size(); i++) {
            if (blockList.get(i).previousHash == blockList.get(i - 1).hash) {
                return true;
            }
        }
        return false;
    }
}


class StringUtil {
    /* Applies Sha256 to a string and returns a hash. */
    public static String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            /* Applies sha256 to our input */
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte elem : hash) {
                String hex = Integer.toHexString(0xff & elem);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

public class Main {
    public static void main(String[] args) throws Exception {

        List<String> dialog = List.of(
                "CarShop sent 10 VC to Worker1",
                "CarShop sent 10 VC to Worker2",
                "CarShop sent 10 VC to Worker3",
                "CarShop sent 30 VC to Director1",
                "CarShop sent 45 VC to CarPartsShop",
                "Bob sent 5 VC to GamingShop",
                "Nick sent 1 VC to ShoesShop",
                "Nick sent 2 VC to FastFood",
                "Nick sent 15 VC to CarShop",
                "miner7 sent 90 VC to CarShop",
                "miner9 sent 30 VC to Nick",
                "miner9 sent 30 VC to miner2",
                "miner9 sent 30 VC to miner1"
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
            } else if (block.blockGenTime > 5 && block.blockGenTime < 60) {
                block.regulateN = "N stays the same";
            }
            block.minerCoin = 100;
            block.minerIdCoin = "miner" + threadId + " gets " + block.minerCoin + " VC";
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
            } else if (block.blockGenTime > 5 && block.blockGenTime < 60) {
                block.regulateN = "N stays the same";
            }
            block.minerCoin = 100;
            block.minerIdCoin = "miner" + threadId + " gets " + block.minerCoin + " VC";

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
            } else if (block.blockGenTime > 5 && block.blockGenTime < 60) {
                block.regulateN = "N stays the same";
            }
            block.minerCoin = 100;
            block.minerIdCoin = "miner" + threadId + " gets " + block.minerCoin + " VC";

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
            } else if (block.blockGenTime > 5 && block.blockGenTime < 60) {
                block.regulateN = "N stays the same";
            }
            block.minerCoin = 100;
            block.minerIdCoin = "miner" + threadId + " gets " + block.minerCoin + " VC";
            return block;
        };


        Callable blockchainGen5 = () -> {
            long threadId = Thread.currentThread().getId();
            Block block = new Blockchain().genBlockChain();
            block.minerId = "Created by miner # " + threadId;
            if (block.blockGenTime < 5) {
                ++Blockchain.howManyZero;
                block.regulateN = "N was increased to " + Blockchain.howManyZero;
            } else if (block.blockGenTime > 60) {
                --Blockchain.howManyZero;
                block.regulateN = "N was decreased by " + Blockchain.howManyZero;
            } else if (block.blockGenTime > 5 && block.blockGenTime < 60) {
                block.regulateN = "N stays the same";
            }
            block.minerCoin = 100;
            block.minerIdCoin = "miner" + threadId + " gets " + block.minerCoin + " VC";
            return block;
        };

        Callable blockchainGen6 = () -> {
            long threadId = Thread.currentThread().getId();
            Block block = new Blockchain().genBlockChain();
            block.minerId = "Created by miner # " + threadId;
            if (block.blockGenTime < 5) {
                ++Blockchain.howManyZero;
                block.regulateN = "N was increased to " + Blockchain.howManyZero;
            } else if (block.blockGenTime > 60) {
                --Blockchain.howManyZero;
                block.regulateN = "N was decreased by " + Blockchain.howManyZero;
            } else if (block.blockGenTime > 5 && block.blockGenTime < 60) {
                block.regulateN = "N stays the same";
            }
            block.minerCoin = 100;
            block.minerIdCoin = "miner" + threadId + " gets " + block.minerCoin + " VC";
            return block;
        };

        Callable blockchainGen7 = () -> {
            long threadId = Thread.currentThread().getId();
            Block block = new Blockchain().genBlockChain();
            block.minerId = "Created by miner # " + threadId;
            if (block.blockGenTime < 5) {
                ++Blockchain.howManyZero;
                block.regulateN = "N was increased to " + Blockchain.howManyZero;
            } else if (block.blockGenTime > 60) {
                --Blockchain.howManyZero;
                block.regulateN = "N was decreased by " + Blockchain.howManyZero;
            } else if (block.blockGenTime > 5 && block.blockGenTime < 60) {
                block.regulateN = "N stays the same";
            }
            block.minerCoin = 100;
            block.minerIdCoin = "miner" + threadId + " gets " + block.minerCoin + " VC";
            return block;
        };

        Callable blockchainGen8 = () -> {
            long threadId = Thread.currentThread().getId();
            Block block = new Blockchain().genBlockChain();
            block.minerId = "Created by miner # " + threadId;
            if (block.blockGenTime < 5) {
                ++Blockchain.howManyZero;
                block.regulateN = "N was increased to " + Blockchain.howManyZero;
            } else if (block.blockGenTime > 60) {
                --Blockchain.howManyZero;
                block.regulateN = "N was decreased by " + Blockchain.howManyZero;
            } else if (block.blockGenTime > 5 && block.blockGenTime < 60) {
                block.regulateN = "N stays the same";
            }
            block.minerCoin = 100;
            block.minerIdCoin = "miner" + threadId + " gets " + block.minerCoin + " VC";
            return block;
        };

        Callable blockchainGen9 = () -> {
            long threadId = Thread.currentThread().getId();
            Block block = new Blockchain().genBlockChain();
            block.minerId = "Created by miner # " + threadId;
            if (block.blockGenTime < 5) {
                ++Blockchain.howManyZero;
                block.regulateN = "N was increased to " + Blockchain.howManyZero;
            } else if (block.blockGenTime > 60) {
                --Blockchain.howManyZero;
                block.regulateN = "N was decreased by " + Blockchain.howManyZero;
            } else if (block.blockGenTime > 5 && block.blockGenTime < 60) {
                block.regulateN = "N stays the same";
            }
            block.minerCoin = 100;
            block.minerIdCoin = "miner" + threadId + " gets " + block.minerCoin + " VC";
            return block;
        };

        Callable blockchainGen10 = () -> {
            long threadId = Thread.currentThread().getId();
            Block block = new Blockchain().genBlockChain();
            block.minerId = "Created by miner # " + threadId;
            if (block.blockGenTime < 5) {
                ++Blockchain.howManyZero;
                block.regulateN = "N was increased to " + Blockchain.howManyZero;
            } else if (block.blockGenTime > 60) {
                --Blockchain.howManyZero;
                block.regulateN = "N was decreased by " + Blockchain.howManyZero;
            } else if (block.blockGenTime > 5 && block.blockGenTime < 60) {
                block.regulateN = "N stays the same";
            }
            block.minerCoin = 100;
            block.minerIdCoin = "miner" + threadId + " gets " + block.minerCoin + " VC";
            return block;
        };

        Callable blockchainGen11 = () -> {
            long threadId = Thread.currentThread().getId();
            Block block = new Blockchain().genBlockChain();
            block.minerId = "Created by miner # " + threadId;
            if (block.blockGenTime < 5) {
                ++Blockchain.howManyZero;
                block.regulateN = "N was increased to " + Blockchain.howManyZero;
            } else if (block.blockGenTime > 60) {
                --Blockchain.howManyZero;
                block.regulateN = "N was decreased by " + Blockchain.howManyZero;
            } else if (block.blockGenTime > 5 && block.blockGenTime < 60) {
                block.regulateN = "N stays the same";
            }
            block.minerCoin = 100;
            block.minerIdCoin = "miner" + threadId + " gets " + block.minerCoin + " VC";
            return block;
        };

        Callable blockchainGen12 = () -> {
            long threadId = Thread.currentThread().getId();
            Block block = new Blockchain().genBlockChain();
            block.minerId = "Created by miner # " + threadId;
            if (block.blockGenTime < 5) {
                ++Blockchain.howManyZero;
                block.regulateN = "N was increased to " + Blockchain.howManyZero;
            } else if (block.blockGenTime > 60) {
                --Blockchain.howManyZero;
                block.regulateN = "N was decreased by " + Blockchain.howManyZero;
            } else if (block.blockGenTime > 5 && block.blockGenTime < 60) {
                block.regulateN = "N stays the same";
            }
            block.minerCoin = 100;
            block.minerIdCoin = "miner" + threadId + " gets " + block.minerCoin + " VC";
            return block;
        };

        Callable blockchainGen13 = () -> {
            long threadId = Thread.currentThread().getId();
            Block block = new Blockchain().genBlockChain();
            block.minerId = "Created by miner # " + threadId;
            if (block.blockGenTime < 5) {
                ++Blockchain.howManyZero;
                block.regulateN = "N was increased to " + Blockchain.howManyZero;
            } else if (block.blockGenTime > 60) {
                --Blockchain.howManyZero;
                block.regulateN = "N was decreased by " + Blockchain.howManyZero;
            } else if (block.blockGenTime > 5 && block.blockGenTime < 60) {
                block.regulateN = "N stays the same";
            }
            block.minerCoin = 100;
            block.minerIdCoin = "miner" + threadId + " gets " + block.minerCoin + " VC";
            return block;
        };

        Callable blockchainGen14 = () -> {
            long threadId = Thread.currentThread().getId();
            Block block = new Blockchain().genBlockChain();
            block.minerId = "Created by miner # " + threadId;
            if (block.blockGenTime < 5) {
                ++Blockchain.howManyZero;
                block.regulateN = "N was increased to " + Blockchain.howManyZero;
            } else if (block.blockGenTime > 60) {
                --Blockchain.howManyZero;
                block.regulateN = "N was decreased by " + Blockchain.howManyZero;
            } else if (block.blockGenTime > 5 && block.blockGenTime < 60) {
                block.regulateN = "N stays the same";
            }
            block.minerCoin = 100;
            block.minerIdCoin = "miner" + threadId + " gets " + block.minerCoin + " VC";
            return block;
        };

        Callable blockchainGen15 = () -> {
            long threadId = Thread.currentThread().getId();
            Block block = new Blockchain().genBlockChain();
            block.minerId = "Created by miner # " + threadId;
            if (block.blockGenTime < 5) {
                ++Blockchain.howManyZero;
                block.regulateN = "N was increased to " + Blockchain.howManyZero;
            } else if (block.blockGenTime > 60) {
                --Blockchain.howManyZero;
                block.regulateN = "N was decreased by " + Blockchain.howManyZero;
            } else if (block.blockGenTime > 5 && block.blockGenTime < 60) {
                block.regulateN = "N stays the same";
            }
            block.minerCoin = 100;
            block.minerIdCoin = "miner" + threadId + " gets " + block.minerCoin + " VC";
            return block;
        };


        ExecutorService executorService = Executors.newFixedThreadPool(4);

        Future<Block> blockGen1 = executorService.submit(blockchainGen1);

        Future<Block> blockGen2 = executorService.submit(blockchainGen2);

        Future<Block> blockGen3 = executorService.submit(blockchainGen3);

        Future<Block> blockGen4 = executorService.submit(blockchainGen4);

        Future<Block> blockGen5 = executorService.submit(blockchainGen5);
        Future<Block> blockGen6 = executorService.submit(blockchainGen6);
        Future<Block> blockGen7 = executorService.submit(blockchainGen7);
        Future<Block> blockGen8 = executorService.submit(blockchainGen8);
        Future<Block> blockGen9 = executorService.submit(blockchainGen9);
        Future<Block> blockGen10 = executorService.submit(blockchainGen10);
        Future<Block> blockGen11 = executorService.submit(blockchainGen11);
        Future<Block> blockGen12 = executorService.submit(blockchainGen12);
        Future<Block> blockGen13 = executorService.submit(blockchainGen13);
        Future<Block> blockGen14 = executorService.submit(blockchainGen14);
        Future<Block> blockGen15 = executorService.submit(blockchainGen15);


//        executorService.submit(() -> {
//        executorServiceClient.submit(() -> {
        for (String msg : dialog) {
            try {
                Thread.sleep(1);
                Blockchain.addMessage(msg);
                Blockchain.addMessageSign(msg.getBytes());
                Blockchain.addMessageSign(Message.sign(msg, "KeyPair/privateKey"));
                Thread.sleep(2);
//                    if (blockGen2.get().messages.size() != 1) {
//                    if (blockGen2.get().signedMsg.size() < 2) {
                if (blockGen2.get().getMagicNumber() != null && blockGen2.get().signedMsg.size() < 1) {
//
                    blockGen2.get().setMessages(Blockchain.tempMessages);
                    blockGen2.get().setSignedMsg(Blockchain.signedMessages);

                    Blockchain.tempMessages.removeAll(Blockchain.tempMessages);
                    Blockchain.signedMessages.clear();
                }

//                    if (blockGen3.get().messages.size() != 1) {
//                      if (blockGen3.get().signedMsg.size() < 2) {
                if (blockGen3.get().getMagicNumber() != null && blockGen3.get().signedMsg.size() < 1) {

                    blockGen3.get().setMessages(Blockchain.tempMessages);
                    blockGen3.get().setSignedMsg(Blockchain.signedMessages);
                    Blockchain.tempMessages.removeAll(Blockchain.tempMessages);
                    Blockchain.signedMessages.clear();
                }

//                    if (blockGen4.get().messages.size() != 1) {
//                      if (blockGen4.get().signedMsg.size() < 2) {
                if (blockGen4.get().getMagicNumber() != null && blockGen4.get().signedMsg.size() < 1) {
                    blockGen4.get().setMessages(Blockchain.tempMessages);
                    blockGen4.get().setSignedMsg(Blockchain.signedMessages);

                    Blockchain.tempMessages.removeAll(Blockchain.tempMessages);
                    Blockchain.signedMessages.clear();

                }
//                    if (blockGen5.get().messages.size() != 1) {
//                      if (blockGen5.get().signedMsg.size() < 2) {
                if (blockGen5.get().getMagicNumber() != null && blockGen5.get().signedMsg.size() < 1) {
                    blockGen5.get().setMessages(Blockchain.tempMessages);
                    blockGen5.get().setSignedMsg(Blockchain.signedMessages);
                    Blockchain.tempMessages.removeAll(Blockchain.tempMessages);
                    Blockchain.signedMessages.clear();

                }
                if (blockGen5.get().getMagicNumber() != null && blockGen5.get().signedMsg.size() < 1) {
                    blockGen5.get().setMessages(Blockchain.tempMessages);
                    blockGen5.get().setSignedMsg(Blockchain.signedMessages);
                    Blockchain.tempMessages.removeAll(Blockchain.tempMessages);
                    Blockchain.signedMessages.clear();

                }
                if (blockGen6.get().getMagicNumber() != null && blockGen6.get().signedMsg.size() < 1) {
                    blockGen6.get().setMessages(Blockchain.tempMessages);
                    blockGen6.get().setSignedMsg(Blockchain.signedMessages);
                    Blockchain.tempMessages.removeAll(Blockchain.tempMessages);
                    Blockchain.signedMessages.clear();

                }
                if (blockGen7.get().getMagicNumber() != null && blockGen7.get().signedMsg.size() < 1) {
                    blockGen7.get().setMessages(Blockchain.tempMessages);
                    blockGen7.get().setSignedMsg(Blockchain.signedMessages);
                    Blockchain.tempMessages.removeAll(Blockchain.tempMessages);
                    Blockchain.signedMessages.clear();

                }
                if (blockGen8.get().getMagicNumber() != null && blockGen8.get().signedMsg.size() < 1) {
                    blockGen8.get().setMessages(Blockchain.tempMessages);
                    blockGen8.get().setSignedMsg(Blockchain.signedMessages);
                    Blockchain.tempMessages.removeAll(Blockchain.tempMessages);
                    Blockchain.signedMessages.clear();

                }
                if (blockGen9.get().getMagicNumber() != null && blockGen9.get().signedMsg.size() < 1) {
                    blockGen9.get().setMessages(Blockchain.tempMessages);
                    blockGen9.get().setSignedMsg(Blockchain.signedMessages);
                    Blockchain.tempMessages.removeAll(Blockchain.tempMessages);
                    Blockchain.signedMessages.clear();

                }
                if (blockGen10.get().getMagicNumber() != null && blockGen10.get().signedMsg.size() < 1) {
                    blockGen10.get().setMessages(Blockchain.tempMessages);
                    blockGen10.get().setSignedMsg(Blockchain.signedMessages);
                    Blockchain.tempMessages.removeAll(Blockchain.tempMessages);
                    Blockchain.signedMessages.clear();

                }
                if (blockGen11.get().getMagicNumber() != null && blockGen11.get().signedMsg.size() < 1) {
                    blockGen11.get().setMessages(Blockchain.tempMessages);
                    blockGen11.get().setSignedMsg(Blockchain.signedMessages);
                    Blockchain.tempMessages.removeAll(Blockchain.tempMessages);
                    Blockchain.signedMessages.clear();

                }
                if (blockGen12.get().getMagicNumber() != null && blockGen12.get().signedMsg.size() < 1) {
                    blockGen12.get().setMessages(Blockchain.tempMessages);
                    blockGen12.get().setSignedMsg(Blockchain.signedMessages);
                    Blockchain.tempMessages.removeAll(Blockchain.tempMessages);
                    Blockchain.signedMessages.clear();

                }
                if (blockGen13.get().getMagicNumber() != null && blockGen13.get().signedMsg.size() < 1) {
                    blockGen13.get().setMessages(Blockchain.tempMessages);
                    blockGen13.get().setSignedMsg(Blockchain.signedMessages);
                    Blockchain.tempMessages.removeAll(Blockchain.tempMessages);
                    Blockchain.signedMessages.clear();

                }
                if (blockGen14.get().getMagicNumber() != null && blockGen14.get().signedMsg.size() < 1) {
                    blockGen14.get().setMessages(Blockchain.tempMessages);
                    blockGen14.get().setSignedMsg(Blockchain.signedMessages);
                    Blockchain.tempMessages.removeAll(Blockchain.tempMessages);
                    Blockchain.signedMessages.clear();

                }

                if (blockGen15.get().getMagicNumber() != null && blockGen15.get().signedMsg.size() < 1) {
                    blockGen15.get().setMessages(Blockchain.tempMessages);
                    blockGen15.get().setSignedMsg(Blockchain.signedMessages);
                    Blockchain.tempMessages.removeAll(Blockchain.tempMessages);
                    Blockchain.signedMessages.clear();

                }

            } catch (InterruptedException | ExecutionException ignored) {
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
//        });


//        System.out.println("After Submit >>>>>>> " + Arrays.deepToString(blockGen5.get().signedMsg.stream().toArray()));

        List<Block> blockList = new ArrayList<>();

        blockList.add(blockGen1.get());
        blockList.add(blockGen2.get());
        blockList.add(blockGen3.get());
        blockList.add(blockGen4.get());
        blockList.add(blockGen5.get());
        blockList.add(blockGen6.get());
        blockList.add(blockGen7.get());
        blockList.add(blockGen8.get());
        blockList.add(blockGen9.get());
        blockList.add(blockGen10.get());
        blockList.add(blockGen11.get());
        blockList.add(blockGen12.get());
        blockList.add(blockGen13.get());
        blockList.add(blockGen14.get());
        blockList.add(blockGen15.get());


        List<Block> sortedBlocks = blockList.stream().sorted(Comparator.comparing(Block::getId)).collect(Collectors.toList());

        for (int i = 0; i < sortedBlocks.size(); i++) {
            System.out.println("Block:");
            System.out.println(sortedBlocks.get(i).minerId);
            System.out.println(sortedBlocks.get(i).minerIdCoin);
            System.out.println("Id:" + "  " + sortedBlocks.get(i).getId());
            System.out.println("Timestamp:" + sortedBlocks.get(i).getTimeStamp());
            System.out.println("Magic number:" + sortedBlocks.get(i).getMagicNumber());
            System.out.println("Hash of the previous block:");
            System.out.println(sortedBlocks.get(i).getPreviousHash());
            System.out.println("Hash of the block:");
            System.out.println(sortedBlocks.get(i).getHash());
            if (sortedBlocks.get(i).getId() == 0) {
//                System.out.println("Block data: no messages");
                System.out.println("Block data: \nNo transactions");
            } else {
                System.out.println("Block data:");
                sortedBlocks.get(i).messages.stream()
                        .forEach(System.out::println);
                sortedBlocks.get(i).signedMsg.stream()
                        .forEach(System.out::println);
                if (sortedBlocks.get(i).signedMsg.size() != 0) {
                    System.out.println(VerifyMessage.verifySignature(sortedBlocks.get(i).signedMsg.get(0), sortedBlocks.get(i).signedMsg.get(1), "KeyPair/publicKey") ? "VERIFIED MESSAGE" +
                            "\n----------------\n" + new String(sortedBlocks.get(i).signedMsg.get(0)) : "Could not verify the signature.");
                }
            }
            System.out.println("Block was generating for " + sortedBlocks.get(i).blockGenTime + " seconds");
            System.out.println(sortedBlocks.get(i).regulateN);
            System.out.println("\n");
        }
        executorService.shutdown();
//        executorServiceClient.shutdown();
    }
}