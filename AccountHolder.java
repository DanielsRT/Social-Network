import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AccountHolder {
    private String idNum;
    private String fname;
    private String lname;
    private String email;
    private Set<String> friendIDs;

    public AccountHolder() {
        idNum = "";
        fname = "";
        lname = "";
        email = "";
        friendIDs = new HashSet<>();
    }

    public AccountHolder(String id, String first, String last, String email_addy) {
        idNum = id;
        fname = first;
        lname = last;
        email = email_addy;
        friendIDs = new HashSet<>();
    }

    public AccountHolder(String data) {
        String[] fields = data.split(":");
        idNum = fields[0];
        fname = fields[1];
        lname = fields[2];
        email = fields[3];
        friendIDs = new HashSet<>();
        if (fields.length >= 5) {
            String[] friend_id_list = fields[4].split(",");
            friendIDs.addAll(Arrays.asList(friend_id_list));
            /*
            for (String f : friend_id_list) {
                friendIDs.add(f);
            }
            */
        }
    }

    public String getFullName() {
        return String.format("%s %s", fname, lname);
    }

    public Set<String> getFriendIDs() {
        return friendIDs;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("ID: %s, %s %s (%s), friends with: ", idNum, fname, lname, email));
        for (String friendID : friendIDs) {
            sb.append(String.format("%s ", friendID));
        }
        return sb.toString();
    }

    public String toFileString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s:%s:%s:%s:", idNum, fname, lname, email));
        Iterator<String> fr_iter = friendIDs.iterator();
        // fence-post problem: only want commas between ids, not before 1st or after last
        if (!friendIDs.isEmpty()) {
            sb.append(String.format("%s", fr_iter.next()));
        }
        fr_iter.forEachRemaining(f -> sb.append(String.format(",%s", f)));
        return sb.toString();
    }

    public String getID() {
        return idNum;
    }
}
