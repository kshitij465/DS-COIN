package DSCoinPackage;

import HelperClasses.CRF;

public class BlockChain_Honest {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock lastBlock;

  public void InsertBlock_Honest (TransactionBlock newBlock) {
    CRF en=new CRF(64);
    long x=1000000001L;
    newBlock.nonce="1000000001";
    String predgst;
    if(lastBlock!=null){predgst=lastBlock.dgst;}
    else{predgst=start_string;}
    newBlock.dgst=en.Fn(predgst+"#"+ newBlock.trsummary+ "#"+ newBlock.nonce);
    //checking for existence for such a nonce
    while(!newBlock.dgst.startsWith("0000")){
      x=1+x;
      newBlock.nonce=""+x;
      newBlock.dgst=en.Fn(predgst+"#"+ newBlock.trsummary+ "#"+ newBlock.nonce);
    }
    newBlock.previous=lastBlock;
    lastBlock=newBlock;
    

  }
}
