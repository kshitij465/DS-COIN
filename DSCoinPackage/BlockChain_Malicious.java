package DSCoinPackage;

import HelperClasses.*;

public class BlockChain_Malicious {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock[] lastBlocksList;

  public static boolean checkTransactionBlock (TransactionBlock tB) {
    CRF en=new CRF(64);
    if(tB.dgst.substring(0,4).equals("0000")!=true){return false;}
    if(tB.previous==null){
      if(!tB.dgst.equals(en.Fn(start_string+"#"+tB.trsummary+"#"+tB.nonce))){return false;}
      MerkleTree qq=new MerkleTree();
      if(tB.trsummary.equals(qq.Build(tB.trarray))==false){return false;}
      return true;
    }
    else{
      if(!tB.dgst.equals(en.Fn(tB.previous.dgst+"#"+tB.trsummary+"#"+tB.nonce))){return false;}
      MerkleTree qq=new MerkleTree();
      if(tB.trsummary.equals(qq.Build(tB.trarray))==false){return false;}
      for(int i=0; i<tB.trarray.length ; i++){
        if(tB.previous.checkTransaction(tB.trarray[i])==false){return false;}
      }
      return true;
    }

  }

  public TransactionBlock FindLongestValidChain () {
    TransactionBlock k=null;
    int max=0;
    int count=0;
    TransactionBlock aa;
    TransactionBlock kk;
    for(int i=0;i<lastBlocksList.length;i++){
      kk=lastBlocksList[i];
      aa=null;
      while(kk!=null){
        if(checkTransactionBlock(kk)){
          if(count==0){
            aa=kk;
            count+=1;
          }
          else{
            count+=1;
          }

        }
        else{
          count=0;
          aa=null;
        }
        kk=kk.previous;

      }
      if(count>max){k=aa;max=count;}
      count=0;
    }
    return k;
  }

  public void InsertBlock_Malicious (TransactionBlock newBlock) {
    TransactionBlock lastBlock=this.FindLongestValidChain();
    CRF en=new CRF(64);
    long x=1000000001L;
    newBlock.nonce="1000000001";
    String predgst;
    if(lastBlock!=null){predgst=lastBlock.dgst;}
    else{predgst=start_string;}
    newBlock.dgst=en.Fn(predgst+"#"+ newBlock.trsummary+ "#"+ newBlock.nonce);
    while(!newBlock.dgst.substring(0,4).equals("0000")){
      x=1+x;
      newBlock.nonce=""+x;
      newBlock.dgst=en.Fn(predgst+"#"+ newBlock.trsummary+ "#"+ newBlock.nonce);
    }
  
    boolean ak=false;

    for(int i=0;i<lastBlocksList.length;i++){
      //case to check that an element is the last block to wihch the chain is added 
      if(lastBlocksList[i]==lastBlock){
        ak=true;
        lastBlocksList[i]=newBlock;
        break;
      }
    }
    
    if(!ak){
      //in this case find the min empty place in the list to insert new block
      for(int i=0;i<lastBlocksList.length;i++){
        if(lastBlocksList[i]==null){
          lastBlocksList[i]=newBlock;
          break;
        }
      }
    }
    newBlock.previous=lastBlock;
  }
}
