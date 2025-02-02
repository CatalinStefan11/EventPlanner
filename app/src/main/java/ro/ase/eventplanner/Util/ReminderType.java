package ro.ase.eventplanner.Util;



public enum ReminderType {
    ALERT("alert"), NOTE("note");

  private final String mName;
  ReminderType(String name){
      mName = name;
    }

  public String getName(){
      return mName;
    }

  public static ReminderType fromString(String name){
   ReminderType[] actionTypes = values();
   for (ReminderType type : actionTypes){
     if (type.mName.equals(name)) {
       return type;
     }
   }
    return null;
  }

}
