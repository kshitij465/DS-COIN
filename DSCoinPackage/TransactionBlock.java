package DSCoinPackage;

import HelperClasses.MerkleTree;
import HelperClasses.CRF;

public class TransactionBlock {

  public Transaction[] trarray;
  public TransactionBlock previous;
  public MerkleTree Tree;
  public String trsummary;
  public String nonce;
  public String dgst;

  TransactionBlock(Transaction[] t) {
    Transaction[] arr=new Transaction[t.length];
    for(int i=0;i<t.length;i++){
      arr[i]=t[i];
    }
    trarray=arr;
    MerkleTree tr=new MerkleTree();
    trsummary=tr.Build(arr);
    Tree=tr;
  }

  public boolean checkTransaction (Transaction t) {
    TransactionBlock aa=t.coinsrc_block;
    if(t.coinsrc_block==null){return true;}
    int times=-1;
    for(int i=0;i<aa.trarray.length;i++){
      if(t.coinID.equals(aa.trarray[i].coinID) && t.Source.UID.equals(aa.trarray[i].Destination.UID)){
        times=i;
        break;
      }
    }
    if(times==-1){return false;}
    TransactionBlock k=this;
    while(k!=aa){
      for(int i=0;i<k.trarray.length;i++){
        if(t.coinID.equals(k.trarray[i].coinID) ){
          return false;
        }
      }
      k=k.previous;
    }
    
    for(int i=times+1;i<aa.trarray.length;i++){
      if(t.coinID.equals(aa.trarray[i].coinID) ){
        return false;
      }
    }
    return true;


  }
}
