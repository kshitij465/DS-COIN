package DSCoinPackage;
import HelperClasses.Pair;
public class Moderator
 {

  public void initializeDSCoin(DSCoin_Honest DSObj, int coinCount) {
    int tr_count=DSObj.bChain.tr_count;
    int badd=0;
    int numbloackadded=coinCount/tr_count;
    int latestcoin=99999;
    int curmen=0;
    Members mode=new Members();
    mode.UID="Moderator";
    while(badd<numbloackadded){
      Transaction[] block=new Transaction[tr_count];
      for(int i=0;i<tr_count;i++){
        Transaction newcoin=new Transaction();
        latestcoin+=1;
        DSObj.latestCoinID=""+latestcoin;
        newcoin.coinID=DSObj.latestCoinID;
        if(curmen==DSObj.memberlist.length){curmen=0;}
        newcoin.Destination=DSObj.memberlist[curmen];
        curmen+=1;
        newcoin.Source=mode;
        block[i]=newcoin;

      }
      TransactionBlock blok=new TransactionBlock(block);
      for(Transaction i : block){
        i.Destination.mycoins.add(new Pair<>(i.coinID,blok));
       }
       DSObj.bChain.InsertBlock_Honest(blok);
       badd+=1;
    }

  }
    
  public void initializeDSCoin(DSCoin_Malicious DSObj, int coinCount) {
    int tr_count=DSObj.bChain.tr_count;
    int badd=0;
    int numbloackadded=coinCount/tr_count;
    int latestcoin=99999;
    int curmen=0;
    Members mode=new Members();
    mode.UID="Moderator";
    while(badd<numbloackadded){
      Transaction[] block=new Transaction[tr_count];
      for(int i=0;i<tr_count;i++){
        Transaction newcoin=new Transaction();
        latestcoin+=1;
        DSObj.latestCoinID=""+latestcoin;
        newcoin.coinID=DSObj.latestCoinID;
        if(curmen==DSObj.memberlist.length){curmen=0;}
        newcoin.Destination=DSObj.memberlist[curmen];
        curmen+=1;
        newcoin.Source=mode;
        block[i]=newcoin;

      }
      TransactionBlock blok=new TransactionBlock(block);
      for(Transaction i : block){
        i.Destination.mycoins.add(new Pair<>(i.coinID,blok));
       }
       DSObj.bChain.InsertBlock_Malicious(blok);
       badd+=1;
    }
  }
}
