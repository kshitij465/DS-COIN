package DSCoinPackage;

import java.util.*;
import HelperClasses.*;
public class Members
 {

  public String UID;
  public List<Pair<String, TransactionBlock>> mycoins;
  public Transaction[] in_process_trans;
  public boolean comparetrans(Transaction dcc,Transaction dddc){
    return (dcc.coinsrc_block== dddc.coinsrc_block && Objects.equals(dcc.coinID, dddc.coinID) && dcc.Destination==dddc.Destination && dcc.Source == dddc.Source);
   }
  public void initiateCoinsend(String destUID, DSCoin_Honest DSobj) {
    Pair<String, TransactionBlock> send =mycoins.remove(0);
    
    Transaction tobj=new Transaction();
    tobj.coinID=send.first;
    tobj.coinsrc_block=send.second;
    tobj.Source=this;
    for(Members i:DSobj.memberlist){
      if(i.UID.equals(destUID)){
        tobj.Destination=i;
        break;
      }
    }
    if(this.in_process_trans==null){
			this.in_process_trans = new Transaction[1000];
		}
    for(int i=0;i<in_process_trans.length;i++){
      if(in_process_trans[i]==null){
        in_process_trans[i]=tobj;
      }
    }
    DSobj.pendingTransactions.AddTransactions(tobj);

  }
  public void initiateCoinsend(String destUID, DSCoin_Malicious DSobj) {
    Pair<String, TransactionBlock> send =mycoins.remove(0);
    
    Transaction tobj=new Transaction();
    tobj.coinID=send.first;
    tobj.coinsrc_block=send.second;
    tobj.Source=this;
    for(Members i:DSobj.memberlist){
      if(i.UID.equals(destUID)){
        tobj.Destination=i;
        break;
      }
    }
    if(in_process_trans==null){
      in_process_trans=new Transaction[1000];
    }
    for(int i=0;i<in_process_trans.length;i++){
			if(this.in_process_trans[i]==null){
				this.in_process_trans[i]=tobj;
				break;
			}
		}

    DSobj.pendingTransactions.AddTransactions(tobj);

  }
  public void sortingofcoinsarray(List<Pair<String,TransactionBlock>> coin_list){
    Collections.sort(coin_list, new Comparator<Pair<String,TransactionBlock>>() {
        @Override
        //sorting operator
        public int compare(final Pair<String,TransactionBlock> qlll, final Pair<String,TransactionBlock> wkkk) {
            if (Integer.parseInt(qlll.first)==Integer.parseInt(wkkk.first)){return 0;}
            else if (Integer.parseInt(qlll.first)>Integer.parseInt(wkkk.first)){return 1;}
            else{return -1;}
        }
    });
  }


  public Pair<List<Pair<String, String>>, List<Pair<String, String>>> finalizeCoinsend (Transaction tobj, DSCoin_Honest DSObj) throws MissingTransactionException {
    TransactionBlock kurr=DSObj.bChain.lastBlock;
    int se=0;
    boolean akkk=true;
    while(kurr!=null  ){
      for(int i=0;i<kurr.trarray.length;i++){
        if(tobj==kurr.trarray[i]){
          se=i+1;akkk=false;break;
        }
      }
      if(akkk==false){break;}
      kurr=kurr.previous;
    }
    if(akkk){throw new MissingTransactionException();}
    int x=(kurr.trarray.length)/2;
    TreeNode kuuls=kurr.Tree.rootnode;
    List<Pair<String, String>> path=new ArrayList<Pair<String, String>>();
    //for a tree having 2^n nodes 2^n-1 in left rooted subtree and half in right
    while(x>=1){
      if(se<=x){
        kuuls=kuuls.left;
        x=x/2;

      }
      else{
        kuuls=kuuls.right;
        se=se-x;
        x=x/2;
      }
    }
    while(kuuls.parent!=null){
      path.add(new Pair<String, String>(kuuls.parent.left.val,kuuls.parent.right.val));
      kuuls=kuuls.parent;
    }
    path.add(new Pair<String, String>(kuuls.val,null));
    TransactionBlock moveback=DSObj.bChain.lastBlock;
    ArrayList<Pair<String, String>> h=new ArrayList<Pair<String, String>>();
    while(kurr.previous!=moveback){
      Pair<String,String> ak=new Pair<String,String>(moveback.dgst,moveback.previous.dgst+"#"+moveback.trsummary+"#"+moveback.nonce);
      h.add(0,ak);
      moveback=moveback.previous;
    }
    Pair<String,String> ak=new Pair<String,String>(moveback.dgst,null);
    h.add(0,ak);
   
  Pair<List<Pair<String, String>>, List<Pair<String, String>>> ans= new Pair<List<Pair<String, String>>, List<Pair<String, String>>>(path,h);
  Transaction[] nww=new Transaction[in_process_trans.length];
  int qw=0;
  //new array to remove occurence of this transaction from the array
  for(int i=0;i<in_process_trans.length;i++){
    if(tobj==in_process_trans[i]){
      qw=-1;
    }
    else{nww[i+qw]=in_process_trans[i];}
  }
  in_process_trans=nww;
  Pair<String, TransactionBlock> coin = new Pair<String, TransactionBlock>(tobj.coinID, kurr);
   tobj.Destination.mycoins.add(coin);
   tobj.Destination.sortingofcoinsarray(tobj.Destination.mycoins);//sorting the array of coins to maintain increasing order of UID
  return ans;
  
  }

  public void MineCoin(DSCoin_Honest DSObj) {
    int i=DSObj.bChain.tr_count-1;
    HashMap<String,String> map=new HashMap<String, String>(); 
    Transaction miner=new Transaction();
    miner.coinID=String.valueOf(Integer.parseInt(DSObj.latestCoinID)+1);
    DSObj.latestCoinID=miner.coinID;
    miner.Destination=this;
    Transaction[] arr=new Transaction[i+1];
    while(i>0){
      try {
        Transaction kk=DSObj.pendingTransactions.RemoveTransaction();
        if(map.get(kk.coinID)==null &&  DSObj.bChain.lastBlock.checkTransaction(kk)){
          map.put(kk.coinID,"hey");
          arr[DSObj.bChain.tr_count-i-1]=kk;
          i--;
        }

        
      } catch (Exception EmptyQueueException) {
      }
    }
    arr[DSObj.bChain.tr_count-1]=miner;
    TransactionBlock nw=new TransactionBlock(arr);
    mycoins.add(new Pair<String, TransactionBlock>(miner.coinID,nw));
    DSObj.bChain.InsertBlock_Honest(nw);
    this.sortingofcoinsarray(mycoins);

  }
  public void MineCoin(DSCoin_Malicious DSObj){
    int i=DSObj.bChain.tr_count-1;
    HashMap<String,String> map=new HashMap<String, String>(); 
    Transaction miner=new Transaction();
    miner.coinID=String.valueOf(Integer.parseInt(DSObj.latestCoinID)+1);
    DSObj.latestCoinID=miner.coinID;
    miner.Destination=this;
    Transaction[] arr=new Transaction[i+1];
    TransactionBlock la=DSObj.bChain.FindLongestValidChain();
    while(i>0){
      try {
        Transaction kk=DSObj.pendingTransactions.RemoveTransaction();
        if(map.get(kk.coinID)==null && la.checkTransaction(kk)){
          map.put(kk.coinID,"hey");
          arr[DSObj.bChain.tr_count-i-1]=kk;
          i--;
        }

        
      } catch (Exception EmptyQueueException) {
      }
    }
    arr[DSObj.bChain.tr_count-1]=miner;
    TransactionBlock nw=new TransactionBlock(arr);
    mycoins.add(new Pair<String, TransactionBlock>(miner.coinID,nw));
    DSObj.bChain.InsertBlock_Malicious(nw);
    this.sortingofcoinsarray(mycoins);
  }  

  
  
}
