package onemessagecompany.onemessage.model;

import java.io.Serializable;

/**
 * Created by 52Solution on 7/06/2017.
 */

public class Message implements Serializable {


  public String getBody() {
    return Body;
  }

  public void setBody(String body) {
    Body = body;
  }

  public int getDeleteAfter() {
    return DeleteAfter;
  }

  public void setDeleteAfter(int deleteAfter) {
    DeleteAfter = deleteAfter;
  }

  public int getID() {
    return ID;
  }

  public void setID(int ID) {
    this.ID = ID;
  }

  public String getRV() {
    return RV;
  }

  public void setRV(String RV) {
    this.RV = RV;
  }

  public boolean getSeen() { return Seen; }

  public void setSeen(boolean Seen) { this.Seen = Seen; }

  public boolean getHasReply(){ return HasReply; }

  public void setHasReply(boolean HasReply){ this.HasReply = HasReply; }

  private String Body;
  private int DeleteAfter;
  private int ID;
  private String RV;
  private boolean Seen;
  private boolean HasReply;


}
