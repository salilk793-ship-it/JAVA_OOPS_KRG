class Student {
    String uid;
    String name;
    int fineAmount;
    int currentBorrowCount;
    void checkPolicies() throws IllegalStateException {
        if(fineAmount > 0) {
            throw new IllegalStateException();
        }        if(currentBorrowCount >= 2) {
            throw new IllegalStateException();
        }
    }
}

class Asset {
    String assetId;
    String assetName;
    boolean available;
    int securityLevel;
    void checkPolicies(String uid) throws IllegalStateException, SecurityException {
        if(!available) {
            throw new IllegalStateException();
        }
        if(securityLevel == 3 && !uid.startsWith("KRG")) {
            throw new SecurityException();
        }
    }
}

class CheckoutRequest {
    String uid;
    String assetId;
    int hoursRequested;
    void validate() throws IllegalArgumentException {}
}

class ValidationUtil {
    static void validateUid(String uid) {
                    if (uid == null || uid.length() < 8 || uid.length() > 12 || uid.contains(" "))
                throw new IllegalArgumentException("Invalid UID.");
    }
    static void validateAssetId(String assetId) { if (assetId == null || !assetId.startsWith("LAB-"))
                throw new IllegalArgumentException("Invalid AssetId.");
        }
    static void validateHours(int hrs) { if (hrs < 1 || hrs > 6)
                throw new IllegalArgumentException("Invalid hours (1–6 allowed).");}
}

import java.util.HashMap;
class AssetStore {
    HashMap<String, Asset> assets = new HashMap<>();
    Asset findAsset(String id) { 
         Asset asset = assets.get(id);
         if (asset == null)
                throw new NullPointerException("Asset not found: " + id);
            return asset;
     }
    void markBorrowed(Asset a) {
        if(a.available == false) {
            throw new IllegalStateException("Asset already borrowed: " + a.assetId);
        }
        a.available = false;
    }
}

class CheckoutService {
    AssetStore store;
    Student student;
    Asset asset;
    String checkout(CheckoutRequest req)
        throws IllegalArgumentException, IllegalStateException, SecurityException, NullPointerException {
        return null;
    }
}

class AuditLogger {
    static void log(String msg) {}
    static void logError(Exception e) {}
}

public class Main {
    public static void main(String[] args) {
        Student s = new Student();
        Asset a = new Asset();
        AssetStore store = new AssetStore();
        CheckoutRequest req = new CheckoutRequest();
        CheckoutService service = new CheckoutService();
        try {
            String receipt = service.checkout(req);
            System.out.println("Checkout successful: " + receipt);
        } catch (IllegalArgumentException e) {
            AuditLogger.logError(e);
        } catch (NullPointerException e) {
            AuditLogger.logError(e);
        } catch (SecurityException e) {
            AuditLogger.logError(e);
        } catch (IllegalStateException e) {
            AuditLogger.logError(e);
        } finally {
            AuditLogger.log("Attempt finished for UID=" + req.uid + ", asset=" + req.assetId);
        }
    }
}
